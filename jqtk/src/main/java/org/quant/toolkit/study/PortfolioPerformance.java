package org.quant.toolkit.study;

import java.util.Date;

import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.quant.toolkit.math.BasicStatFunctions;
import org.quant.toolkit.riskreward.MetricsCalculator;

public class PortfolioPerformance {

	private double sharpeRatio;

	private double totalReturnFund;

	private double stdFund;

	private double avgDailyReturnFund;

	private MetricsCalculator mc;

	private BasicStatFunctions bsf;

	public PortfolioPerformance() {
		mc = new MetricsCalculator();
		bsf = new BasicStatFunctions();
	}

	public void calculateMetrics(DoubleDataFrame<Date> dailyPortValue) {
		// Sharpe Ratio
		DoubleMatrix dailyPortValueMx = dailyPortValue.getValues();
		System.out.println("dailyPortValueMx=" + dailyPortValueMx);
		DoubleMatrix cumRetMx = mc.cumulativeReturn(dailyPortValueMx);
		DoubleMatrix dailyReturnMx = mc.returnize1(cumRetMx.dup());
		double avgDailyReturnFund = dailyReturnMx.mean();
		double stdDailyReturnFund = bsf.stdv(dailyReturnMx);
		System.out.println("cumRetMx=" + cumRetMx);
		System.out.println("avg=" + avgDailyReturnFund);
		System.out.println("stdev=" + stdDailyReturnFund);
		System.out.println("fund_daily_ret=" + dailyReturnMx);
		double sharpeRatio = mc.sharpeRatio(avgDailyReturnFund,
				stdDailyReturnFund);
		setSharpeRatio(sharpeRatio);

		// Total return of fund
		double totalReturnFund = cumRetMx.get(cumRetMx.rows - 1);
		setTotalReturnFund(totalReturnFund);

		// Std of Fund
		setStdFund(stdDailyReturnFund);
		setAvgDailyReturnFund(avgDailyReturnFund);
	}

	private void setStdFund(double stdFund) {
		this.stdFund = stdFund;
	}

	private void setAvgDailyReturnFund(double avgDailyReturnFund) {
		this.avgDailyReturnFund = avgDailyReturnFund;
	}

	private void setTotalReturnFund(double totalReturnFund) {
		this.totalReturnFund = totalReturnFund;
	}

	private void setSharpeRatio(double sharpeRatio) {
		this.sharpeRatio = sharpeRatio;
	}

	public double getSharpeRatio() {
		return sharpeRatio;
	}

	public double getTotalReturnFund() {
		return totalReturnFund;
	}

	public double getFundStd() {
		return stdFund;
	}

	public double getAvgDailyReturn() {
		return avgDailyReturnFund;
	}

}
