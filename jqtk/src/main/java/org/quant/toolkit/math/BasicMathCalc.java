package org.quant.toolkit.math;

import org.jblas.DoubleMatrix;

public class BasicMathCalc {

	private static final int X = 0;

	private static final int Y = 1;

	public DoubleMatrix cumProd(DoubleMatrix m, int axis) {
		if (empty(m)) {
			return null;
		}

		DoubleMatrix n = new DoubleMatrix(m.rows, m.columns);
		if (axis == X) {
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

	private boolean empty(DoubleMatrix m) {
		return (m == null || m.length == 0);
	}

	public DoubleMatrix mean(DoubleMatrix m, int axis) {
		if (empty(m)) {
			return null;
		}

		// Get mean along the X-axis
		if (axis == X) {
			// Get all columns indexes
			int axisLength = m.columns;
			int numOfValues = m.rows;
			int[] cols = getIndexes(axisLength);
			return mean(m, cols, axisLength, axis, numOfValues);
		}
		// Get mean along the Y-axis
		else if (axis == Y) {
			// Get all rows indexes
			int axisLength = m.rows;
			int numOfValues = m.columns;
			int[] rows = getIndexes(axisLength);
			return mean(m, rows, axisLength, axisLength, numOfValues)
					.transpose();
		}

		return null;
	}

	private int[] getIndexes(int axisLength) {
		int[] index = new int[axisLength];
		for (int i = 0; i < axisLength; i++) {
			index[i] = i;
		}
		return index;
	}

	private DoubleMatrix getValuesByAxis(DoubleMatrix m, int[] indexes, int i,
			int axis) {
		DoubleMatrix vals = null;

		if (axis == X) {
			vals = m.get(i, indexes);
		} else {
			vals = m.get(indexes, i);
		}
		return vals;
	}

	private DoubleMatrix mean(DoubleMatrix m, int[] indexes, int axisLength,
			int axis, int numOfValues) {
		DoubleMatrix mean = null;
		if (axis == X) {
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

}
