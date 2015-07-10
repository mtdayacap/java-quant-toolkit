package org.quant.toolkit.dao;

import static org.quant.toolkit.entity.Quote.ADJ_CLOSE;
import static org.quant.toolkit.entity.Quote.CLOSE;
import static org.quant.toolkit.entity.Quote.DATE;
import static org.quant.toolkit.entity.Quote.DATE_PATTERN;
import static org.quant.toolkit.entity.Quote.HIGH;
import static org.quant.toolkit.entity.Quote.LOW;
import static org.quant.toolkit.entity.Quote.OPEN;
import static org.quant.toolkit.entity.Quote.VOLUME;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.SystemUtils;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;

public class YahooDAO {

	private static final String DASH = "-";

	private static final String UNDERSCORE = "_";

	private static final String NEW_LINE = "\n";

	private static final String COMMA = ",";

	public static final String[] CSV_HEADERS = { DATE, OPEN, HIGH, LOW, CLOSE,
			VOLUME, ADJ_CLOSE };

	public static final String DAILY_TRADING_PERIOD = "d";

	public static final String FROM_DAY_PLACEHOLDER = "${FROM_DAY}";

	public static final String FROM_MONTH_PLACEHOLDER = "${FROM_MONTH}";

	public static final String FROM_YEAR_PLACEHOLDER = "${FROM_YEAR}";

	public static final String INTERVAL_PLACEHOLDER = "${INTERVAL}";

	public static final String MONTHLY_TRADING_PERIOD = "m";

	public static final String SYMBOL_PLACEHOLDER = "${SYMBOL}";

	public static final String TO_DAY_PLACEHOLDER = "${TO_DAY}";

	public static final String TO_MONTH_PLACEHOLDER = "${TO_MONTH}";

	public static final String TO_YEAR_PLACEHOLDER = "${TO_YEAR}";

	public static final String URL_TEMPLATE = "http://ichart.finance.yahoo.com/table.csv?s=${SYMBOL}&d=${TO_MONTH}&e=${TO_DAY}&f=${TO_YEAR}&g=${INTERVAL}&a=${FROM_MONTH}&b=${FROM_DAY}&c=${FROM_YEAR}&ignore.csv";

	public static final String WEEKLY_TRADING_PERIOD = "w";

	public static final String ENCODING = "UTF-8";

	public static final String FROM_YEAR = "fromYear";

	public static final String FROM_MONTH = "fromMonth";

	public static final String FROM_DAY = "fromDay";

	public static final String TO_YEAR = "toYear";

	public static final String TO_MONTH = "toMonth";

	public static final String TO_DAY = "toDay";

	public static final String INTERVAL = "interval";

	public static final String TRADING_DAYS = "tradingDays";

	private static final String MARKET_INDEX = "S&P";

	private String cacheDir;

	private boolean isCached = false;

	public String generateURL(Map<String, String> map, String symbol) {
		// Deduct 1 from months
		String toMonth = Integer
				.toString(Integer.valueOf(map.get(TO_MONTH)) - 1);
		String fromMonth = Integer
				.toString(Integer.valueOf(map.get(FROM_MONTH)) - 1);

		String url = URL_TEMPLATE.replace(SYMBOL_PLACEHOLDER, symbol)
				.replace(TO_MONTH_PLACEHOLDER, toMonth)
				.replace(TO_DAY_PLACEHOLDER, map.get(TO_DAY))
				.replace(TO_YEAR_PLACEHOLDER, map.get(TO_YEAR))
				.replace(INTERVAL_PLACEHOLDER, map.get(INTERVAL))
				.replace(FROM_MONTH_PLACEHOLDER, fromMonth)
				.replace(FROM_DAY_PLACEHOLDER, map.get(FROM_DAY))
				.replace(FROM_YEAR_PLACEHOLDER, map.get(FROM_YEAR));
		return url;
	}

	public StockQuotes getStockQuotes(String urlStr, String symbol,
			List<Date> tradingDays) throws UnsupportedEncodingException,
			IOException, ParseException, NumberFormatException,
			FileNotFoundException {
		StockQuotes stockQuotes = new StockQuotes(symbol, tradingDays);
		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
		URL url = new URL(urlStr);
		Reader reader = new InputStreamReader(url.openStream(), ENCODING);
		CSVParser parser = new CSVParser(reader,
				CSVFormat.DEFAULT.withHeader(CSV_HEADERS));
		boolean skipHeader = true;
		int ctr = 0;
		try {
			for (CSVRecord record : parser) {
				if (skipHeader) {
					skipHeader = false;
					continue;
				}
				String dateStr = record.get(DATE);
				String open = record.get(OPEN);
				String high = record.get(HIGH);
				String low = record.get(LOW);
				String close = record.get(CLOSE);
				String volume = record.get(VOLUME);
				String adjClose = record.get(ADJ_CLOSE);
				// Build quote
				Quote quote = new Quote(open, high, low, close, volume,
						adjClose);
				Date date = df.parse(dateStr);
				stockQuotes.set(date, quote);
				ctr++;
			}
		} finally {
			parser.close();
			reader.close();
		}

		return stockQuotes;
	}

	public List<StockQuotes> getStockQuotes(Map<String, String> map,
			List<String> symbols, List<Date> tradingDays)
			throws UnsupportedEncodingException, IOException, ParseException,
			NumberFormatException {
		List<StockQuotes> stockQuotesList = new ArrayList<StockQuotes>();
		for (String symbol : symbols) {
			String url = generateURL(map, symbol);
			StockQuotes stockQuotes = null;
			try {
				if (isCached) {
					stockQuotes = getCachedStockQuotes(map, symbol, tradingDays);
				}
				if (stockQuotes == null) {
					stockQuotes = getStockQuotes(url, symbol, tradingDays);
				}
				stockQuotesList.add(stockQuotes);
			} catch (FileNotFoundException e) {
				// Skip Symbols with quotes unavailable
				continue;
			}
		}

		if (isCached) {
			cacheStockQuotes(map, stockQuotesList);
		}

		return stockQuotesList;
	}

	public StockQuotes getStockQuotes(Map<String, String> map, String symbol,
			List<Date> tradingDays) throws IOException, ParseException,
			FileNotFoundException {
		String url = generateURL(map, symbol);
		StockQuotes stockQuotes = null;
		if (isCached) {
			stockQuotes = getCachedStockQuotes(map, symbol, tradingDays);
		}
		if (stockQuotes == null) {
			stockQuotes = getStockQuotes(url, symbol, tradingDays);

			if (isCached) {
				saveStockQuotes(map, stockQuotes);
			}
		}

		return stockQuotes;
	}

	public List<String> loadSymbolsFromFile(String path) throws IOException {
		List<String> symbols = new ArrayList<String>();
		File symbolsCSV = new File(path);

		BufferedReader reader = Files.newBufferedReader(symbolsCSV.toPath(),
				Charset.forName("US-ASCII"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			symbols.add(line.trim());
		}

		return symbols;
	}

	public void setCache(boolean isCache, String cacheDir) {
		this.isCached = isCache;
		this.cacheDir = cacheDir;
	}

	private void cacheStockQuotes(Map<String, String> map,
			List<StockQuotes> stockQuotesList) throws IOException {
		if (stockQuotesList.isEmpty()) {
			return;
		}
		for (StockQuotes stockQuotes : stockQuotesList) {
			saveStockQuotes(map, stockQuotes);
		}

	}

	private void saveStockQuotes(Map<String, String> map,
			StockQuotes stockQuotes) throws IOException {

		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
		StringBuilder sb = new StringBuilder(CSV_HEADERS[0] + COMMA
				+ CSV_HEADERS[1] + COMMA + CSV_HEADERS[2] + COMMA
				+ CSV_HEADERS[3] + COMMA + CSV_HEADERS[4] + COMMA
				+ CSV_HEADERS[5] + COMMA + CSV_HEADERS[6] + NEW_LINE);
		String symbol = stockQuotes.getSymbol();
		List<Date> dateList = stockQuotes.getDates();
		for (Date date : dateList) {
			Quote quote = stockQuotes.getQuoteByDay(date);
			if (!Double.isNaN(quote.getOpen())) {
				String dateStr = df.format(date);
				String open = Double.toString(quote.getOpen());
				String high = Double.toString(quote.getHigh());
				String low = Double.toString(quote.getLow());
				String close = Double.toString(quote.getClose());
				String volume = Long.toString(quote.getVolume());
				String adjClose = Double.toString(quote.getAdjClose());
				sb.append(dateStr + COMMA + open + COMMA + high + COMMA + low
						+ COMMA + close + COMMA + volume + COMMA + adjClose
						+ NEW_LINE);
			}
		}

		// Save file
		File cache = new File(cacheDir);
		if (!cache.exists()) {
			cache.mkdir();
		}
		String absFilePath = getFileName(map, symbol);
		File sqFile = new File(absFilePath);
		sqFile.createNewFile();
		Files.write(sqFile.toPath(), sb.toString().getBytes(),
				StandardOpenOption.CREATE);
	}

	private String getFileName(Map<String, String> map, String symbol) {
		return cacheDir + SystemUtils.FILE_SEPARATOR + symbol + UNDERSCORE
				+ map.get(FROM_YEAR) + DASH
				+ addLeadingZerosWidthTwo(map.get(FROM_MONTH)) + DASH
				+ addLeadingZerosWidthTwo(map.get(FROM_DAY)) + UNDERSCORE
				+ map.get(TO_YEAR) + DASH
				+ addLeadingZerosWidthTwo(map.get(TO_MONTH)) + DASH
				+ addLeadingZerosWidthTwo(map.get(TO_DAY)) + ".csv";
	}

	private StockQuotes getCachedStockQuotes(Map<String, String> map,
			String symbol, List<Date> tradingDays) throws IOException,
			ParseException {

		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
		StockQuotes stockQuotes = new StockQuotes(symbol, tradingDays);
		String absFileName = getFileName(map, symbol);

		File stockQuotesCached = new File(absFileName);
		if (!stockQuotesCached.exists()) {
			return null;
		}
		Reader reader = new InputStreamReader(new FileInputStream(
				stockQuotesCached), ENCODING);
		CSVParser parser = new CSVParser(reader,
				CSVFormat.DEFAULT.withHeader(CSV_HEADERS));
		boolean skipHeader = true;
		try {
			for (CSVRecord record : parser) {
				if (skipHeader) {
					skipHeader = false;
					continue;
				}
				Quote quote = new Quote(record.get(OPEN), record.get(HIGH),
						record.get(LOW), record.get(CLOSE), record.get(VOLUME),
						record.get(ADJ_CLOSE));
				Date date = df.parse(record.get(DATE));
				stockQuotes.set(date, quote);
			}
		} finally {
			parser.close();
			reader.close();
		}

		return stockQuotes;
	}

	private String addLeadingZerosWidthTwo(String str) {
		return ("00" + str).substring(str.length());
	}

	public List<Date> getTradingDays(Map<String, String> map)
			throws UnsupportedEncodingException, IOException, ParseException {
		boolean toCache = false;
		List<Date> tradingDays = new ArrayList<Date>();
		if (isCached) {
			tradingDays = getCachedTradingDays(map);
			toCache = tradingDays.isEmpty();
			if(!toCache){
				return tradingDays;
			}
		}

		String urlStr = generateURL(map, MARKET_INDEX);
		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
		URL url = new URL(urlStr);
		Reader reader = new InputStreamReader(url.openStream(), ENCODING);
		CSVParser parser = new CSVParser(reader,
				CSVFormat.DEFAULT.withHeader(CSV_HEADERS));
		boolean skipHeader = true;
		try {
			for (CSVRecord record : parser) {
				if (skipHeader) {
					skipHeader = false;
					continue;
				}
				String dateStr = record.get(DATE);
				Date tradingDay = df.parse(dateStr);
				tradingDays.add(tradingDay);
			}
		} finally {
			parser.close();
			reader.close();
		}

		// Check if need to cache
		if (toCache) {
			cacheTradingDays(tradingDays, map);
		}

		return tradingDays;
	}

	private void cacheTradingDays(List<Date> tradingDays,
			Map<String, String> map) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
		File cacheDirFile = new File(cacheDir);
		StringBuilder sb = new StringBuilder(Quote.DATE + NEW_LINE);
		for (Date tradingDay : tradingDays) {
			String dateStr = df.format(tradingDay);
			sb.append(dateStr + NEW_LINE);
		}

		if (!cacheDirFile.exists()) {
			cacheDirFile.mkdir();
		}
		String cachedTradingDaysFilename = getCachedTradingDaysFilename(map);
		File cache = new File(cachedTradingDaysFilename);
		Files.write(cache.toPath(), sb.toString().getBytes(),
				StandardOpenOption.CREATE);
	}

	private List<Date> getCachedTradingDays(Map<String, String> map)
			throws IOException, ParseException {
		List<Date> tradingDays = new ArrayList<Date>();
		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
		String tradingDaysFilename = getCachedTradingDaysFilename(map);
		File tradingDaysFile = new File(tradingDaysFilename);
		if (!tradingDaysFile.exists()) {
			return tradingDays;
		}

		Reader reader = new InputStreamReader(new FileInputStream(
				tradingDaysFile), ENCODING);
		CSVParser parser = new CSVParser(reader,
				CSVFormat.DEFAULT.withHeader(new String[] { DATE }));
		try {
			boolean skipHeader = true;
			for (CSVRecord record : parser) {
				if (skipHeader) {
					skipHeader = false;
					continue;
				}
				String dateStr = record.get(DATE);
				Date date = df.parse(dateStr);
				tradingDays.add(date);
			}
		} finally {
			reader.close();
			parser.close();
		}

		return tradingDays;
	}

	private String getCachedTradingDaysFilename(Map<String, String> map) {
		return cacheDir + SystemUtils.FILE_SEPARATOR + MARKET_INDEX
				+ UNDERSCORE + map.get(FROM_YEAR) + DASH
				+ addLeadingZerosWidthTwo(map.get(FROM_MONTH)) + DASH
				+ addLeadingZerosWidthTwo(map.get(FROM_DAY)) + UNDERSCORE
				+ map.get(TO_YEAR) + DASH
				+ addLeadingZerosWidthTwo(map.get(TO_MONTH)) + DASH
				+ addLeadingZerosWidthTwo(map.get(TO_DAY)) + "_td.csv";

	}

}
