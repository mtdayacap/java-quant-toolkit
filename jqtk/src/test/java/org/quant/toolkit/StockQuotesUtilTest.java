package org.quant.toolkit;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.jblas.DoubleMatrix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quant.toolkit.dao.YahooDAO;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.exceptions.StockQuotesUtilException;

public class StockQuotesUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReturnizeAAPLClosingPrices() throws NumberFormatException,
			UnsupportedEncodingException, FileNotFoundException, IOException,
			ParseException, InterruptedException, ExecutionException,
			StockQuotesUtilException {
		YahooDAO yahooDao = new YahooDAO();
		String fromYear = "2008";
		String fromMonth = "1";
		String fromDay = "1";
		String toYear = "2009";
		String toMonth = "12";
		String toDay = "31";

		Map<String, String> map = new HashMap<String, String>();
		map.put(YahooDAO.FROM_YEAR, fromYear);
		map.put(YahooDAO.FROM_MONTH, fromMonth);
		map.put(YahooDAO.FROM_DAY, fromDay);
		map.put(YahooDAO.TO_YEAR, toYear);
		map.put(YahooDAO.TO_MONTH, toMonth);
		map.put(YahooDAO.TO_DAY, toDay);
		map.put(YahooDAO.INTERVAL, YahooDAO.DAILY_TRADING_PERIOD);

		List<String> symbols = new ArrayList<String>();
		symbols.add("AAPL");
		symbols.add("XOM");
		List<StockQuotes> stockQuotesList = yahooDao.getStockQuotes(map,
				symbols, yahooDao.getTradingDays(map));
		int colsExp = stockQuotesList.size();
		System.out.println(colsExp);
		StockQuotes stockQuotes1 = stockQuotesList.get(0);
		StockQuotes stockQuotes2 = stockQuotesList.get(1);
		assertEquals("stockQuotes sizes not equal", stockQuotes1.size,
				stockQuotes2.size);
		int rowsExp = stockQuotes1.size;

		DoubleMatrix stockQuotesMatrix = StockQuotesUtil.toQuoteDoubleMatrix(
				stockQuotesList, Quote.CLOSE);
		int rowsAct = stockQuotesMatrix.rows;
		int colsAct = stockQuotesMatrix.columns;
		assertEquals(rowsExp, rowsAct);
		assertEquals(colsExp, colsAct);

	}

}
