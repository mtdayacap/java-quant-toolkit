package org.quant.toolkit.study;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.quant.toolkit.DoubleMatrixUtil;
import org.quant.toolkit.math.BasicStatFunctions;
import org.quant.toolkit.riskreward.MetricsCalculator;

public class EventProfiler {

	private BasicStatFunctions bsf;

	private DoubleDataFrame<Date> events;

	private MetricsCalculator mc;

	private DoubleMatrix meanReturns;

	private DoubleDataFrame<Date> prices;

	private DoubleMatrix stdReturns;

	public EventProfiler() {
		mc = new MetricsCalculator();
		bsf = new BasicStatFunctions();
	}

	public EventProfiler(DoubleDataFrame<Date> events,
			DoubleDataFrame<Date> prices) {
		this();
		this.events = events;
		this.prices = prices;
	}

	public DoubleDataFrame<Date> calcAveDailyReturns() {
		return null;
	}

	public DoubleMatrix getMeanReturns() {
		return meanReturns;
	}

	public DoubleMatrix getStandardReturns() {
		return stdReturns;
	}

	public void profileEvents(int lookback, int lookforward,
			boolean isMarketNeutral, String marketSymbol) {
		DoubleDataFrame<Date> dupEvents = events.dup();
		DoubleDataFrame<Date> dupPrices = prices.dup();

		DoubleMatrix priceRetsM = mc.returnize0(dupPrices.getValues());
		DoubleDataFrame<Date> pricesRets = new DoubleDataFrame<Date>(
				prices.getIndexes(), priceRetsM, prices.getLabels());

		if (isMarketNeutral) {
			priceRetsM = priceRetsM.subColumnVector(pricesRets
					.getColumnValues(marketSymbol));
			dupEvents.removeColumn(marketSymbol);
			dupPrices.removeColumn(marketSymbol);
		}

		// Remove starting and end events
		dupEvents.putRowValue(0, lookback + 1, Double.NaN);
		dupEvents.putRowValue(dupEvents.rows - lookforward, lookforward,
				Double.NaN);

		// Collect the price returns relative from the event
		int rows = dupEvents.rows;
		int cols = dupEvents.columns;
		List<DoubleMatrix> eventPriceReturnList = new ArrayList<DoubleMatrix>();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				double event = dupEvents.get(r, c);
				if (event == 1) {
					int rowStart = r - lookback;
					int rowEnd = r + lookforward + 1;
					DoubleMatrix v = priceRetsM
							.getRowRange(rowStart, rowEnd, c);
					eventPriceReturnList.add(v);
				}
			}
		}

		// Convert List to DoubleMatrix
		int numOfCols = lookback + lookforward + 1;
		int numOfRows = eventPriceReturnList.size();
		DoubleMatrix returnsM = new DoubleMatrix(numOfRows, numOfCols);
		int r = 0;
		int[] indices = getRangeIndices(0, numOfCols);
		for (DoubleMatrix v : eventPriceReturnList) {
			returnsM.put(r, indices, v.transpose());
			r++;
		}

		// Compute the daily rets and returns
		returnsM = bsf.cumProd(returnsM.addi(1), BasicStatFunctions.Y);
		returnsM = (returnsM.transpose().divRowVector(returnsM
				.getColumn(lookback))).transpose();

		// Get the mean and std of returns
		DoubleMatrix meanRets = bsf.mean(returnsM, BasicStatFunctions.X);
		DoubleMatrix stdRets = bsf.std(returnsM, BasicStatFunctions.X);
		

		setMeanReturns(meanRets);
		setStdReturns(stdRets);
	}

	private int[] getRangeIndices(int start, int end) {
		int size = end - start;
		int[] indices = new int[size];
		for (int j = 0; j < size; j++) {
			indices[j] = j;
		}
		return indices;
	}

	private void setMeanReturns(DoubleMatrix meanReturns) {
		this.meanReturns = meanReturns;
	}

	private void setStdReturns(DoubleMatrix stdReturns) {
		this.stdReturns = stdReturns;
	}

}
