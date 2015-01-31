package org.quant.toolkit.entity;

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

public class StockReturns {
	private String symbol;
	
	private Map<Date, Double> returns;

	public double get(Date date) {
		if(MapUtils.isEmpty(returns)){
			return 0;
		}
		return returns.get(date);
	}

	public String getSymbol() {
		return symbol;
	}
}
