package org.quant.toolkit.study;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ArrayUtils;
import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PortfolioPerformanceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAnalyze() throws IOException, ParseException {
		DoubleDataFrame<Date> dailyPortValue = parseDailyPortValueCSV("values.csv");
		
		PortfolioPerformance portPerf = new PortfolioPerformance();
		portPerf.calculateMetrics(dailyPortValue);
		double sharpeRatio = portPerf.getSharpeRatio();
		double fundTotalReturn = portPerf.getTotalReturnFund();
		double fundStd = portPerf.getFundStd();
		double fundAvgDailyReturn = portPerf.getAvgDailyReturn();
		
		assertEquals("Sharpe Ratio not equal", 1.20804311656, sharpeRatio, 1e-2);
		assertEquals("Fund Total Return not equal", 1.13386, fundTotalReturn, 1e-2);
		assertEquals("Fund Std not equal", 0.00716033053973, fundStd, 1e-2);
		assertEquals("Avg Daily Return not equal", 0.000547073277579, fundAvgDailyReturn, 1e-2);
	}

	private DoubleDataFrame<Date> parseDailyPortValueCSV(String csv)
			throws IOException, ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
		Reader reader = new InputStreamReader(
				PortfolioPerformanceTest.class.getClassLoader()
						.getResourceAsStream("values.csv"));
		CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
		List<Date> indexes = new ArrayList<Date>();
		List<Double> values = new ArrayList<Double>();
		for (CSVRecord record : parser) {
			String year = record.get(0);
			String month = record.get(1);
			String day = record.get(2);
			String valueStr = record.get(3);
			
			Double value = Double.valueOf(valueStr);
			Date date = df.parse(year + "-" + month + "-" + day);
			
			indexes.add(date);
			values.add(value);
		}
		Double[] valuesArray = values.toArray(new Double[values.size()]);
		DoubleMatrix valuesMx = new DoubleMatrix(ArrayUtils.toPrimitive(valuesArray));
		DoubleDataFrame<Date> dailyPortValue = new DoubleDataFrame<Date>(indexes, valuesMx);

		return dailyPortValue;
	}

}
