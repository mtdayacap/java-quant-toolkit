package org.quant.toolkit.metrics;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quant.toolkit.DoubleMatrixUtil;
import org.quant.toolkit.StockQuotesUtil;
import org.quant.toolkit.dao.DAO;
import org.quant.toolkit.dao.YahooDAO;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.entity.StockReturns;
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
	public void testReturnizeAAPLXOMClosingPrices() throws NumberFormatException,
			UnsupportedEncodingException, IOException, ParseException,
			InterruptedException, ExecutionException, StockQuotesUtilException,
			MetricsCalculatorException {
		DAO yahooDao = new YahooDAO();
		String fromYear = "2008";
		String fromMonth = "1";
		String fromDay = "1";
		String toYear = "2009";
		String toMonth = "12";
		String toDay = "31";

		List<String> symbols = new ArrayList<String>();
		symbols.add("AAPL");
		symbols.add("XOM");
		List<StockQuotes> stockQuotesList = yahooDao.getStockQuotes(fromYear,
				fromMonth, fromDay, toYear, toMonth, toDay,
				YahooDAO.INTERVAL_PLACEHOLDER, symbols);

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
		double xomRet1 = retsDf.get(date1, "XOM");
		System.out.println("2009-12-15, " + aaplRet1 + ", " + xomRet1);

		double aaplRet2 = retsDf.get(date2, "AAPL");
		double xomRets2 = retsDf.get(date2, "XOM");
		System.out.println("2008-01-18, " + aaplRet2 + ", " + xomRets2);
	}
}
