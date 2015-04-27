package org.quant.toolkit.technicalanalysis;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		String label = "AA";
		List<String> symbols = new ArrayList<String>();
		symbols.add(label);
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
		DoubleDataFrame<Date> dfBollingerBands = bb.getBollingerBands();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		// Check for NaN
		Date date1 = df.parse("2008-01-24");
		double nan = dfBollingerBands.get(date1, label);
		assertTrue(Double.isNaN(nan));
		
		// Check for bollinger band values
		Date date2 = df.parse("2008-01-30");
		double value2 = dfBollingerBands.get(date2, label);
		assertEquals(0.446779059738d, value2, 1e6);
		
		Date date3 = df.parse("2009-12-30");
		double value3 = dfBollingerBands.get(date3, label);
		assertEquals(1.34010386987d, value3, 1e6);
		
		Date date4 = df.parse("2009-11-30");
		double value4 = dfBollingerBands.get(date4, label);
		assertEquals(-1.40895882875d, value4, 1e6);

		System.out.println(value2);
		System.out.println(value3);
		System.out.println(value4);
	}

}
