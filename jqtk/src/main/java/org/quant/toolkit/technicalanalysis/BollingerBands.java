package org.quant.toolkit.technicalanalysis;

import java.util.Date;

import org.jdaf.DoubleDataFrame;

public class BollingerBands {

	private DoubleDataFrame<Date> prices;
	
	private DoubleDataFrame<Date> dfBollingerBands;
	
	public BollingerBands(DoubleDataFrame<Date> prices) {
		this.prices = prices;
	}

	public void calculate(int period) {
		// TODO Auto-generated method stub
		
	}

	public DoubleDataFrame<Date> getDataFrameValues() {
		return dfBollingerBands;
	}
	
	
}
