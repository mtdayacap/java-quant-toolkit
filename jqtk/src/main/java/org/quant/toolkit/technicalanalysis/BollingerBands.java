package org.quant.toolkit.technicalanalysis;

import java.util.Date;

import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.quant.toolkit.DoubleMatrixUtil;
import org.quant.toolkit.math.BasicStatFunctions;

public class BollingerBands {

	private DoubleDataFrame<Date> bollingerBands;

	private BasicStatFunctions bsc;

	private DoubleDataFrame<Date> prices;

	public BollingerBands(DoubleDataFrame<Date> prices) {
		this.prices = prices;
		this.bsc = new BasicStatFunctions();
	}

	public void calculate(int period) {
		DoubleDataFrame<Date> pricesCopy = prices.dup();
		DoubleMatrix pricesMx = pricesCopy.getValues();

		// Get rolling mean and std
		DoubleMatrix rollingMeanMx = bsc.rollingMean(pricesMx, period);
		DoubleMatrix rollingStdMx = bsc.rollingStd(pricesMx, period);

		// Compute bollinger bands
		DoubleMatrix bollingerBandsMx = pricesMx.subi(rollingMeanMx).divi(
				rollingStdMx);

		// Store bollinger bands in dataframe
		DoubleDataFrame<Date> tempBollingerBandsDf = new DoubleDataFrame<Date>(
				pricesCopy.getIndexes(), bollingerBandsMx,
				pricesCopy.getLabels());
		
		setBollingerBands(tempBollingerBandsDf);

	}

	public DoubleDataFrame<Date> getBollingerBands() {
		return bollingerBands;
	}

	private void setBollingerBands(DoubleDataFrame<Date> bollingerBandsValues) {
		this.bollingerBands = bollingerBandsValues;
	}

}
