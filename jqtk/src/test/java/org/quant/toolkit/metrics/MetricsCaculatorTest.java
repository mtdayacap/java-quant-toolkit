package org.quant.toolkit.metrics;

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
import java.util.concurrent.ExecutionException;

import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quant.toolkit.DoubleMatrixUtil;
import org.quant.toolkit.StockQuotesUtil;
import org.quant.toolkit.dao.YahooDAO;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.exceptions.MetricsCalculatorException;
import org.quant.toolkit.exceptions.StockQuotesUtilException;
import org.quant.toolkit.riskreward.MetricsCalculator;

public class MetricsCaculatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReturnize0() {
		DoubleMatrix m = new DoubleMatrix(6, 2, -2.167820, -1.170450,
				-0.927672, 1.656815, 0.795391, 1.333311, -1.821621, 0.281833,
				-1.709447, -1.041129, 0.418404, -0.210406);
		DoubleMatrix sm = m.dup();
		DoubleMatrixUtil.shift(sm, -1);
		DoubleMatrix rets = m.div(sm).subi(1);
		// Fill infinite with 0
		for (int i = 0; i < rets.length; i++) {
			rets.put(i, Double.isInfinite(rets.get(i)) ? 0.0 : rets.get(i));
		}
		System.out.println("m: " + m);
		System.out.println("sm: " + sm);
		System.out.println("rets: " + rets);

	}

	@Test
	public void testReturnizeAAPLXOMClosingPrices()
			throws NumberFormatException, UnsupportedEncodingException,
			IOException, ParseException, InterruptedException,
			ExecutionException, StockQuotesUtilException,
			MetricsCalculatorException {
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

		DoubleDataFrame<Date> closingPricesDf = StockQuotesUtil
				.toDoubleDataFrame(stockQuotesList, Quote.CLOSE);

		MetricsCalculator mc = new MetricsCalculator();
		DoubleMatrix retsDm = mc.returnize0(closingPricesDf.getValues());
		DoubleDataFrame<Date> retsDf = new DoubleDataFrame<Date>(
				closingPricesDf.getIndexes(), retsDm,
				closingPricesDf.getLabels());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// Date,AAPL,XOM
		// 2009-12-15 16:00:00,-0.0142654076556,-0.00746161572679
		Date date1 = sdf.parse("2009-12-15");
		// 2008-01-18 16:00:00,0.00292125054385,0.0139435109045
		Date date2 = sdf.parse("2008-01-18");

		double aaplRet1 = retsDf.get(date1, "AAPL");
		assertEquals(-0.0142654076556d, aaplRet1, 1e6);
		double xomRet1 = retsDf.get(date1, "XOM");
		assertEquals(-0.00746161572679d, xomRet1, 1e6);
		System.out.println("2009-12-15, " + aaplRet1 + ", " + xomRet1);

		double aaplRet2 = retsDf.get(date2, "AAPL");
		assertEquals(0.00292125054385d, aaplRet2, 1e6);
		double xomRets2 = retsDf.get(date2, "XOM");
		assertEquals(0.0139435109045d, xomRets2, 1e6);
		System.out.println("2008-01-18, " + aaplRet2 + ", " + xomRets2);
	}

	@Test
	public void testReturnize0AA() throws NumberFormatException,
			UnsupportedEncodingException, IOException, ParseException,
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
		symbols.add("AA");
		List<StockQuotes> stockQuotesList = yahooDao.getStockQuotes(map,
				symbols, yahooDao.getTradingDays(map));

		DoubleDataFrame<Date> closingPricesDf = StockQuotesUtil
				.toDoubleDataFrame(stockQuotesList, Quote.CLOSE);

		MetricsCalculator mc = new MetricsCalculator();
		DoubleMatrix retsDm = mc.returnize0(closingPricesDf.getValues());
		DoubleDataFrame<Date> retsDf = new DoubleDataFrame<Date>(
				closingPricesDf.getIndexes(), retsDm,
				closingPricesDf.getLabels());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//	Date,AA
		//	2008-01-28,0.0254154447703
		//	2008-01-29,0.037813790912
		double val1 = retsDf.get(sdf.parse("2008-01-28"), "AA");
		assertEquals(0.0254154447703d, val1, 1e6);
		double val2 = retsDf.get(sdf.parse("2008-01-29"), "AA");
		assertEquals(0.037813790912d, val2, 1e6);
		
		//	2009-06-23,-0.00199600798403
		//	2009-06-24,0.021
		double val3 = retsDf.get(sdf.parse("2009-06-23"), "AA");
		assertEquals(-0.00199600798403d, val3, 1e6);
		double val4 = retsDf.get(sdf.parse("2009-06-24"), "AA");
		assertEquals(0.021d, val4, 1e6);
	}
}
