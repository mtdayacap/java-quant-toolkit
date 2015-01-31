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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quant.toolkit.DoubleMatrixUtil;
import org.quant.toolkit.dao.DAO;
import org.quant.toolkit.dao.YahooDAO;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.entity.StockReturns;
import org.quant.toolkit.exceptions.MetricsCalculatorException;
import org.quant.toolkit.exceptions.StockQuotesUtilException;
import org.quant.toolkit.metrics.MetricsCalculator;

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

	public void testReturnizeAAPLClosingPrices() throws NumberFormatException,
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

		MetricsCalculator mc = new MetricsCalculator();
		List<StockReturns> stockReturnList = mc.returnize0(stockQuotesList,
				Quote.CLOSE);
		StockReturns sr = null;
		for (StockReturns stockReturns : stockReturnList) {
			if ("AAPL".equals(stockReturns.getSymbol())) {
				sr = stockReturns;
				break;
			}
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = df.parse("2008-01-16");
		double retVal = sr.get(date);
		System.out.println("EXP = -0.055608365019 : ACT = " + retVal);
	}
}
