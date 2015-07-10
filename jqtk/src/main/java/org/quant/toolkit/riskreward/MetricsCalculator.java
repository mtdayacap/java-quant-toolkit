package org.quant.toolkit.riskreward;

import org.jblas.DoubleMatrix;
import org.quant.toolkit.DoubleMatrixUtil;

public class MetricsCalculator {
	public DoubleMatrix returnize0(DoubleMatrix values) {
		if (values == null || values.length == 0) {
			return null;
		}

		DoubleMatrix shiftedMatrix = values.dup();
		DoubleMatrixUtil.shift(shiftedMatrix, -1);
		DoubleMatrix rets = values.div(shiftedMatrix).subi(1);
		return rets;
	}

	public DoubleMatrix cumulativeReturn(DoubleMatrix m) {
		DoubleMatrix cumRet = m.divi(m.get(0));
		return cumRet;
	}

	public double sharpeRatio(double avg, double std) {
		return (Math.sqrt(250) * avg) / std;
	}

	/**
	 * Calculate returns base from the formula: R = (V(i) / V(i - 1)) - 1
	 * 
	 * e.g. Compute daily fund returns base from daily portfolio values
	 * 
	 * @param values
	 *            {@link DoubleMatrix}
	 * */
	public DoubleMatrix returnize1(DoubleMatrix values) {
		// values divided by the negative shift of the duplicate
		DoubleMatrix shiftedCopy = values.dup();
		DoubleMatrixUtil.shift(shiftedCopy, -1);
		DoubleMatrix returns = values.divi(shiftedCopy).subi(1);

		// Replace first row with zero values
		DoubleMatrix v = DoubleMatrix.zeros(returns.columns);
		returns.putRow(0, v);

		return returns;
	}

}
