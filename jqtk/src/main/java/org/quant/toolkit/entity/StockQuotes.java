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

	public StockQuotes(String symbol) {
		this();
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public Quote getQuoteByDay(Date date) {
		return quotes.get(date);
	}

	public void add(Date date, Quote quote) {
		if(MapUtils.isEmpty(quotes) && size == 0){
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
}
