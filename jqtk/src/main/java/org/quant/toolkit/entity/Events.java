package org.quant.toolkit.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

public class Events {

	private Map<String, List<Date>> eventMap;

	public int getNumberOfEvents() {
		int numberOfEvents = 0;
		for (String symbol : eventMap.keySet()) {
			int size = eventMap.get(symbol).size();
			numberOfEvents = numberOfEvents + size;
		}
		return numberOfEvents;
	}

	public void addSymbolEvent(String symbol, Date date) {
		if (MapUtils.isEmpty(eventMap)) {
			eventMap = new HashMap<String, List<Date>>();
			// Add first
			List<Date> dates = new ArrayList<Date>();
			dates.add(date);
			eventMap.put(symbol, dates);
		} else {
			List<Date> dates = eventMap.get(symbol);
			if (CollectionUtils.isEmpty(dates)) {
				dates = new ArrayList<Date>();
				dates.add(date);
				eventMap.put(symbol, dates);
			} else {
				eventMap.get(symbol).add(date);
			}
		}
	}

	public void remove(String marketSymbol) {
		eventMap.remove(marketSymbol);
	}

	public Set<String> getSymbols() {
		return eventMap.keySet();
	}

	public List<Date> getDates(String symbol) {
		return eventMap.get(symbol);
	}

	public boolean contains(String symbol, Date date) {
		List<Date> events = eventMap.get(symbol);
		if (CollectionUtils.isEmpty(events)) {
			return false;
		}
		return events.contains(date);
	}

}
