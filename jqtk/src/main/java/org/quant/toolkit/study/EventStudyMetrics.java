package org.quant.toolkit.study;

import org.jdaf.DoubleDataFrame;

public class EventStudyMetrics {

	private DoubleDataFrame<Integer> meanReturns;

	private DoubleDataFrame<Integer> stdReturns;

	public DoubleDataFrame<Integer> getMeanReturns() {
		return meanReturns;
	}

	public DoubleDataFrame<Integer> getStdReturns() {
		return stdReturns;
	}

}
