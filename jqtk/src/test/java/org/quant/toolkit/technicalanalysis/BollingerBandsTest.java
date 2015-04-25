package org.quant.toolkit.technicalanalysis;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdaf.DoubleDataFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quant.toolkit.StockQuotesUtil;
import org.quant.toolkit.dao.YahooDAO;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.exceptions.StockQuotesUtilException;

public class BollingerBandsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateBollingerBands() throws NumberFormatException,
			UnsupportedEncodingException, IOException, ParseException, StockQuotesUtilException {
		List<String> symbols = new ArrayList<String>();
		symbols.add("AA");
		Map<String, String> map = new HashMap<String, String>();
		map.put(YahooDAO.FROM_YEAR, "2008");
		map.put(YahooDAO.FROM_MONTH, "01");
		map.put(YahooDAO.FROM_DAY, "01");
		map.put(YahooDAO.TO_YEAR, "2009");
		map.put(YahooDAO.TO_MONTH, "12");
		map.put(YahooDAO.TO_DAY, "31");
		map.put(YahooDAO.INTERVAL, YahooDAO.DAILY_TRADING_PERIOD);

		// Get closing prices
		String cacheDirPath = "/Users/mike/Documents/workspace/StockSATSBackTester/cache";
		YahooDAO yahooDAO = new YahooDAO();
		yahooDAO.setCache(true, cacheDirPath);
		List<StockQuotes> stockQuotesList = yahooDAO.getStockQuotes(map,
				symbols, yahooDAO.getTradingDays(map));
		DoubleDataFrame<Date> closingPrices = StockQuotesUtil
				.toDoubleDataFrame(stockQuotesList, Quote.CLOSE);
		
		// Compute Bollinger Bands
		BollingerBands bb = new BollingerBands(closingPrices);
		bb.calculate(20);
		DoubleDataFrame<Date> dfBollingerBands = bb.getDataFrameValues();
		
		
	}

}
