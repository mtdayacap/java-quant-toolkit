package org.quant.toolkit.math;

import static org.jblas.MatrixFunctions.powi;
import static org.jblas.MatrixFunctions.sqrt;

import org.jblas.DoubleMatrix;

public class BasicStatCalc {

	public static final String X = "X";

	public static final String Y = "Y";

	public DoubleMatrix cumProd(DoubleMatrix m, String axis) {
		if (notValidParams(m, axis)) {
			return null;
		}

		DoubleMatrix n = new DoubleMatrix(m.rows, m.columns);
		if (X.equals(axis)) {
			// Init
			n.putRow(0, m.getRow(0));
			for (int i = 1; i < m.rows; i++) {
				n.putRow(i, m.getRow(i));
				DoubleMatrix t1 = n.getRow(i - 1);
				DoubleMatrix t2 = n.getRow(i);
				n.putRow(i, t1.mul(t2));
			}

		} else {
			// Init
			n.putColumn(0, m.getColumn(0));
			for (int i = 1; i < m.columns; i++) {
				n.putColumn(i, m.getColumn(i));
				DoubleMatrix t1 = n.getColumn(i - 1);
				DoubleMatrix t2 = n.getColumn(i);
				n.putColumn(i, t1.mul(t2));
			}
		}

		return n;
	}

	public DoubleMatrix mean(DoubleMatrix m, String axis) {
		if (notValidParams(m, axis)) {
			return null;
		}

		// Get mean along the X-axis
		if (X.equals(axis)) {
			// Get all columns indexes
			int axisLength = m.columns;
			int numOfValues = m.rows;
			int[] cols = getIndexes(axisLength);
			return mean(m, cols, axisLength, axis, numOfValues);
		}
		// Get mean along the Y-axis
		else {
			// Get all rows indexes
			int axisLength = m.rows;
			int numOfValues = m.columns;
			int[] rows = getIndexes(axisLength);
			return mean(m, rows, axisLength, axis, numOfValues)
					.transpose();
		}
	}

	public DoubleMatrix std(DoubleMatrix m, String axis) {
		if (notValidParams(m, axis) || !notValidVector(m)) {
			return null;
		}
		return sqrt(mean(
				powi(m.subi(createMeanMatrixByAxis(mean(m, axis), m.rows,
						m.columns, axis)), 2), axis));
	}

	public double stdv(DoubleMatrix v) {
		if (notValidVector(v)) {
			return 0;
		}
		return Math.sqrt(powi(v.subi(v.mean()), 2).mean());
	}

	private DoubleMatrix createMeanMatrixByAxis(DoubleMatrix meanVector,
			int rows, int cols, String axis) {
		DoubleMatrix meanM = null;
		DoubleMatrix ones = DoubleMatrix.ones(rows, cols);
		if (X.equals(axis)) {
			meanM = ones.mulRowVector(meanVector);
		} else {
			meanM = ones.mulColumnVector(meanVector);
		}
		return meanM;
	}

	private boolean empty(DoubleMatrix m) {
		return (m == null || m.length == 0);
	}

	private int[] getIndexes(int axisLength) {
		int[] index = new int[axisLength];
		for (int i = 0; i < axisLength; i++) {
			index[i] = i;
		}
		return index;
	}

	private DoubleMatrix getValuesByAxis(DoubleMatrix m, int[] indexes, int i,
			String axis) {
		DoubleMatrix vals = null;

		if (X.equals(axis)) {
			vals = m.get(i, indexes);
		} else {
			vals = m.get(indexes, i);
		}
		return vals;
	}

	private DoubleMatrix mean(DoubleMatrix m, int[] indexes, int axisLength,
			String axis, int numOfValues) {
		DoubleMatrix mean = null;
		if (X.equals(axis)) {
			mean = m.get(0, indexes);
		} else {
			mean = m.get(indexes, 0);
		}

		for (int i = 1; i < numOfValues; i++) {
			DoubleMatrix vals = getValuesByAxis(m, indexes, i, axis);
			mean = mean.addi(vals);
		}
		mean.divi(numOfValues);

		return mean;
	}

	private boolean notValidParams(DoubleMatrix m, String axis) {
		return empty(m) || (!X.equals(axis) && !Y.equals(axis));
	}

	private boolean notValidVector(DoubleMatrix v) {
		if (v == null || !v.isVector() || v.isEmpty()) {
			return true;
		}
		return false;
	}
}
