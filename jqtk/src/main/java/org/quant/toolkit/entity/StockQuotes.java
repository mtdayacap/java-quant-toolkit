package org.quant.toolkit.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.collections.MapUtils;

public class StockQuotes {
	private String symbol;

	private TreeMap<Date, Quote> quotes;

	public int size;

	public StockQuotes() {
		size = 0;
	}

	public StockQuotes(String symbol, List<Date> tradingDays) {
		this();
		this.symbol = symbol;
		initQuotes(tradingDays);
	}

	private void initQuotes(List<Date> tradingDays) {
		quotes = new TreeMap<Date, Quote>();
		for (Date date : tradingDays) {
			quotes.put(date, new Quote(Double.NaN, Double.NaN, Double.NaN,
					Double.NaN, 0, Double.NaN));
		}
		size = quotes.size();
	}

	public String getSymbol() {
		return symbol;
	}

	public Quote getQuoteByDay(Date date) {
		return quotes.get(date);
	}

	public void add(Date date, Quote quote) {
		if (MapUtils.isEmpty(quotes) && size == 0) {
			quotes = new TreeMap<Date, Quote>();
		}
		quotes.put(date, quote);
		size += 1;
	}

	public Map<Date, Quote> getQuotes() {
		return quotes;
	}

	public List<Date> getDates() {
		return new ArrayList<Date>(quotes.keySet());
	}

	public Quote getQuoteFromPreviousDay(Date date) {
		Entry<Date, Quote> entry = quotes.lowerEntry(date);
		if (entry != null) {
			return entry.getValue();
		}
		return null;
	}

	public List<Double> getOpenPrices() {
		List<Double> prices = new ArrayList<Double>();
		for (Date date : quotes.keySet()) {
			prices.add(quotes.get(date).getOpen());
		}
		return prices;
	}

	public List<Double> getClosingPrices() {
		List<Double> prices = new ArrayList<Double>();
		for (Date date : quotes.keySet()) {
			prices.add(quotes.get(date).getClose());
		}
		return prices;
	}

	public List<Double> getLowPrices() {
		List<Double> prices = new ArrayList<Double>();
		for (Date date : quotes.keySet()) {
			prices.add(quotes.get(date).getLow());
		}
		return prices;

	}

	public List<Double> getHighPrices() {
		List<Double> prices = new ArrayList<Double>();
		for (Date date : quotes.keySet()) {
			prices.add(quotes.get(date).getHigh());
		}
		return prices;
	}

	public void set(Date date, Quote quote) {
		// Record only prices same as Market Index
		// trading days
		if (quotes.containsKey(date)) {
			quotes.put(date, quote);
		}
	}

	public List<Double> getAdjClosePrices() {
		List<Double> prices = new ArrayList<Double>();
		for (Date date : quotes.keySet()) {
			prices.add(quotes.get(date).getAdjClose());
		}
		return prices;
	}
}
