package org.quant.toolkit.study;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.quant.toolkit.entity.Order;

public class MarketSimulator {

	private Map<Date, List<Order>> orderMap;

	private DoubleDataFrame<Date> prices;

	private DoubleDataFrame<Date> dailyPortfolioValues;

	public MarketSimulator(Map<Date, List<Order>> orderMap,
			DoubleDataFrame<Date> prices) {
		this.orderMap = orderMap;
		this.prices = prices;
	}

	public void run(double cash) {
		DoubleMatrix initVals = DoubleMatrix.zeros(prices.rows, 1);
		dailyPortfolioValues = new DoubleDataFrame<Date>(prices.getIndexes(),
				initVals);

		Map<String, Double> ownership = new HashMap<String, Double>();
		for (String label : prices.getLabels()) {
			ownership.put(label, 0d);
		}
		List<Date> dateIndexes = prices.getIndexes();
		for (Date di : dateIndexes) {

			List<Order> orders = orderMap.get(di);
			// Check for market orders and update ownership
			if (orders != null) {
				for (Order order : orders) {
					String symbol = order.getSymbol();
					String orderType = order.getOrder();
					double shares = order.getShares();

					Double currentOwnerhsip = ownership.get(symbol);
					if (currentOwnerhsip == null) {
						// Initialize symbol
						currentOwnerhsip = 0d;
						ownership.put(symbol, currentOwnerhsip);
					}

					if (orderType.equals(Order.BUY)) {
						// Buy
						currentOwnerhsip = currentOwnerhsip + shares;
						cash = cash - (shares * prices.get(di, symbol));
					} else {
						// Sell
						currentOwnerhsip = currentOwnerhsip - shares;
						cash = cash + (shares * prices.get(di, symbol));
					}

					ownership.put(symbol, currentOwnerhsip);
				}
			}

			// Compute daily portfolio value
			double dailyCashValue = cash;
			for (String symbol : prices.getLabels()) {
				Double shares = ownership.get(symbol);
				Double price = prices.get(di, symbol);
				dailyCashValue = (shares * price) + dailyCashValue;
			}

			// Update daily portfolio values
			dailyPortfolioValues.set("0", di, dailyCashValue);
		}
	}

	public DoubleDataFrame<Date> getDailyPortfolioValues() {
		return dailyPortfolioValues;
	}

}
