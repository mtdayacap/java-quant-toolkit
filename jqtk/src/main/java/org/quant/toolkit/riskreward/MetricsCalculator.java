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

}
