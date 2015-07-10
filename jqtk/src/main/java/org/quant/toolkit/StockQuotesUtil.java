package org.quant.toolkit;

import static org.quant.toolkit.entity.Quote.CLOSE;
import static org.quant.toolkit.entity.Quote.HIGH;
import static org.quant.toolkit.entity.Quote.LOW;
import static org.quant.toolkit.entity.Quote.OPEN;
import static org.quant.toolkit.entity.Quote.ADJ_CLOSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.exceptions.StockQuotesUtilException;

public class StockQuotesUtil {

	public static DoubleMatrix toQuoteDoubleMatrix(
			List<StockQuotes> stockQuotesList, String price)
			throws StockQuotesUtilException {
		if (CollectionUtils.isEmpty(stockQuotesList)) {
			return null;
		}

		int columns = stockQuotesList.size();
		int rows = 0;
		int tempLength = 0;
		boolean isFirst = true;
		for (StockQuotes stockQuotes : stockQuotesList) {
			if (isFirst) {
				rows = stockQuotes.size;
				isFirst = false;
			} else {
				tempLength = stockQuotes.size;
				if (rows != tempLength) {
					throw new StockQuotesUtilException(
							"List of StockQuotes does not have equal sizes");
				}
			}
		}

		double[] prices = new double[rows * columns];
		int startIndex = 0;
		for (StockQuotes stockQuotes : stockQuotesList) {
			switch (price) {
			case OPEN:
				prices = addAllPricesToArray(stockQuotes.getOpenPrices(),
						prices, startIndex);
				startIndex += rows;
				break;
			case CLOSE:
				prices = addAllPricesToArray(stockQuotes.getClosingPrices(),
						prices, startIndex);
				startIndex += rows;
				break;
			case LOW:
				prices = addAllPricesToArray(stockQuotes.getLowPrices(),
						prices, startIndex);
				startIndex += rows;
				break;
			case HIGH:
				prices = addAllPricesToArray(stockQuotes.getHighPrices(),
						prices, startIndex);
				startIndex += rows;
				break;
			case ADJ_CLOSE:
				prices = addAllPricesToArray(stockQuotes.getAdjClosePrices(), prices, startIndex);
				break;
			default:
				throw new StockQuotesUtilException(
						"Quote prices not available for " + price);
			}
		}
		DoubleMatrix matrix = new DoubleMatrix(rows, columns, prices);
		return matrix;
	}

	private static double[] addAllPricesToArray(List<Double> quotePrices,
			double[] prices, int startIndex) {
		for (Double price : quotePrices) {
			prices[startIndex] = price;
			startIndex++;
		}

		return prices;
	}

	public static DoubleDataFrame<Date> toDoubleDataFrame(
			List<StockQuotes> stockQuotesList, String price)
			throws StockQuotesUtilException {
		if (CollectionUtils.isEmpty(stockQuotesList)) {
			return null;
		}
		// Validate number of rows are equal
		StockQuotes sq = stockQuotesList.get(0);
		int rows = sq.getDates().size();
		for (int i = 1; i < stockQuotesList.size(); i++) {
			StockQuotes q = stockQuotesList.get(i);
			if (q.getDates().size() != rows) {
				return null;
			}
		}

		// Construct DoubleDataFrame
		List<Date> indexes = sq.getDates();
		DoubleMatrix prices = toQuoteDoubleMatrix(stockQuotesList, price);
		List<String> labels = new ArrayList<String>();
		for (StockQuotes stockQuotes : stockQuotesList) {
			String symbol = stockQuotes.getSymbol();
			labels.add(symbol);
		}
		DoubleDataFrame<Date> df = new DoubleDataFrame<Date>(indexes, prices,
				labels);
		return df;
	}

}
