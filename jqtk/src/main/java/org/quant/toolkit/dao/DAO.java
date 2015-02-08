package org.quant.toolkit.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jdaf.DoubleDataFrame;
import org.quant.toolkit.entity.StockQuotes;

public interface DAO {

	public List<StockQuotes> getStockQuotes(String fromYear, String fromMonth,
			String fromDay, String toYear, String toMonth, String toDay,
			String interval, List<String> symbols)
			throws UnsupportedEncodingException, IOException, ParseException,
			NumberFormatException, InterruptedException, ExecutionException;

	public List<String> loadSymbolsFromFile(String path) throws IOException;

	public String generateURL(String fromYear, String fromMonth,
			String fromDay, String toYear, String toMonth, String toDay,
			String interval, String symbol);
	
	public StockQuotes getStockQuotes(final String urlStr,final String symbol)
			throws UnsupportedEncodingException, IOException, ParseException,
			NumberFormatException, FileNotFoundException;	
}
