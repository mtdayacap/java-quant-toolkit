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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;

public class YahooDAO implements DAO {

	public static final String DAILY_TRADING_PERIOD = "d";

	public static final String MONTHLY_TRADING_PERIOD = "m";

	public static final String WEEKLY_TRADING_PERIOD = "w";

	private static final String ENCODING = "UTF-8";

	public static final String FROM_DAY_PLACEHOLDER = "${FROM_DAY}";

	public static final String FROM_MONTH_PLACEHOLDER = "${FROM_MONTH}";

	public static final String FROM_YEAR_PLACEHOLDER = "${FROM_YEAR}";

	public static final String INTERVAL_PLACEHOLDER = "${INTERVAL}";

	public static final String SYMBOL_PLACEHOLDER = "${SYMBOL}";

	public static final String TO_DAY_PLACEHOLDER = "${TO_DAY}";

	public static final String TO_MONTH_PLACEHOLDER = "${TO_MONTH}";

	public static final String TO_YEAR_PLACEHOLDER = "${TO_YEAR}";

	public static final String URL_TEMPLATE = "http://ichart.finance.yahoo.com/table.csv?s=${SYMBOL}&d=${TO_MONTH}&e=${TO_DAY}&f=${TO_YEAR}&g=${INTERVAL}&a=${FROM_MONTH}&b=${FROM_DAY}&c=${FROM_YEAR}&ignore.csv";

	public static final String[] CSV_HEADERS = { DATE, OPEN,
			HIGH, LOW, CLOSE, VOLUME,
			ADJ_CLOSE };

	public String generateURL(String fromYear, String fromMonth,
			String fromDay, String toYear, String toMonth, String toDay,
			String interval, String symbol) {
		// Deduct 1 from months
		toMonth = Integer.toString(Integer.valueOf(toMonth) - 1);
		fromMonth = Integer.toString(Integer.valueOf(fromMonth) - 1);

		String url = URL_TEMPLATE.replace(SYMBOL_PLACEHOLDER, symbol)
				.replace(TO_MONTH_PLACEHOLDER, toMonth)
				.replace(TO_DAY_PLACEHOLDER, toDay)
				.replace(TO_YEAR_PLACEHOLDER, toYear)
				.replace(INTERVAL_PLACEHOLDER, interval)
				.replace(FROM_MONTH_PLACEHOLDER, fromMonth)
				.replace(FROM_DAY_PLACEHOLDER, fromDay)
				.replace(FROM_YEAR_PLACEHOLDER, fromYear);
		return url;
	}

	public StockQuotes getStockQuotes(String urlStr, String symbol)
			throws UnsupportedEncodingException, IOException, ParseException,
			NumberFormatException, FileNotFoundException {
		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
		URL url = new URL(urlStr);
		Reader reader = new InputStreamReader(url.openStream(), ENCODING);
		CSVParser parser = new CSVParser(reader,
				CSVFormat.DEFAULT.withHeader(CSV_HEADERS));
		StockQuotes stockQuotes = new StockQuotes(symbol);
		boolean skipHeader = true;
		try {
			for (CSVRecord record : parser) {
				if (skipHeader) {
					skipHeader = false;
					continue;
				}
				Quote quote = new Quote(record.get(OPEN),
						record.get(HIGH), record.get(LOW),
						record.get(CLOSE));
				Date date = df.parse(record.get(DATE));
				stockQuotes.add(date, quote);
			}
		} finally {
			parser.close();
			reader.close();
		}

		return stockQuotes;
	}

	public List<StockQuotes> getStockQuotes(String fromYear, String fromMonth,
			String fromDay, String toYear, String toMonth, String toDay,
			String interval, List<String> symbols)
			throws UnsupportedEncodingException, IOException, ParseException,
			NumberFormatException {
		// TODO Validate params

		List<StockQuotes> stockQuotesList = new ArrayList<StockQuotes>();
		for (String symbol : symbols) {
			String url = generateURL(fromYear, fromMonth, fromDay, toYear,
					toMonth, toDay, interval, symbol);
			StockQuotes stockQuotes = null;
			try {
				stockQuotes = getStockQuotes(url, symbol);
				stockQuotesList.add(stockQuotes);
			} catch (FileNotFoundException e) {
				// Skip Symbols with quotes unavailable
				System.out.println("Not available");
				continue;
			}
		}

		return stockQuotesList;
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

}
