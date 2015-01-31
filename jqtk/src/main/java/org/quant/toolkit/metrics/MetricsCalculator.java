package org.quant.toolkit.metrics;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jblas.DoubleMatrix;
import org.quant.toolkit.DoubleMatrixUtil;
import org.quant.toolkit.StockQuotesUtil;
import org.quant.toolkit.StockReturnsUtil;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.entity.StockReturns;
import org.quant.toolkit.exceptions.MetricsCalculatorException;
import org.quant.toolkit.exceptions.StockQuotesUtilException;

public class MetricsCalculator {

	public MetricsCalculator() {
	}

	public List<StockReturns> returnize0(List<StockQuotes> stockQuotesList,
			String quotePrice) throws StockQuotesUtilException,
			MetricsCalculatorException {
		if (CollectionUtils.isEmpty(stockQuotesList)) {
			return null;
		}
		if (StringUtils.isEmpty(quotePrice)) {
			throw new MetricsCalculatorException("Quote header not specified.");
		}

		DoubleMatrix priceMatrix = StockQuotesUtil.toQuoteDoubleMatrix(
				stockQuotesList, quotePrice);

		DoubleMatrix shiftedMatrix = priceMatrix.dup();
		DoubleMatrixUtil.shift(shiftedMatrix, -1);

		DoubleMatrix returnsMatrix = priceMatrix.mul(shiftedMatrix).subi(1);
		List<StockReturns> stockReturnsList = StockReturnsUtil
				.toStockReturnsList(returnsMatrix, stockQuotesList);
		return null;
	}

}
