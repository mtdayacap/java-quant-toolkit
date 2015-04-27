package org.quant.toolkit.math;

import static org.jblas.MatrixFunctions.powi;
import static org.jblas.MatrixFunctions.sqrt;

import org.jblas.DoubleMatrix;

public class BasicStatFunctions {

	public static final String X = "X";

	public static final String Y = "Y";

	public DoubleMatrix cumProd(DoubleMatrix m, String axis) {
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

	public DoubleMatrix cumProdv(DoubleMatrix v) {
		if (v.isColumnVector()) {
			return cumProd(v, X);
		} else {
			return cumProd(v, Y);
		}
	}

	public DoubleMatrix mean(DoubleMatrix m, String axis) {
		if (X.equals(axis)) {
			return m.columnMeans();
		} else {
			return m.rowMeans().transpose();
		}
	}

	public double meanv(DoubleMatrix v) {
		return v.mean();
	}

	public DoubleMatrix std(DoubleMatrix m, String axis) {
		return sqrt(mean(
				powi(m.subi(createMeanMatrixByAxis(mean(m, axis), m.rows,
						m.columns, axis)), 2), axis));
	}

	public double stdv(DoubleMatrix v) {
		int n = v.length;
		DoubleMatrix copy = v.dup();
		double mean = v.mean();
		DoubleMatrix sqrdDiffs = powi(copy.subi(mean), 2);
		double sumSqrdDiffs = sqrdDiffs.sum();
		double meanSqrdDiffs = sumSqrdDiffs / (n - 1);
		return Math.sqrt(meanSqrdDiffs);
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

	public DoubleMatrix rollingMean(DoubleMatrix data, int period) {
		DoubleMatrix rollingMean = data.dup();
		rollingMean.fill(Double.NaN);

		int cols = data.columns;
		for (int i = period; i <= data.rows; i++) {
			int start = i - period;
			int end = i;
			for (int c = 0; c < cols; c++) {
				DoubleMatrix rowSubset = data.getRowRange(start, end, c);
				double mean = rowSubset.mean();
				rollingMean.put(end - 1, c, mean);
			}
		}

		return rollingMean;
	}

	public DoubleMatrix rollingStd(DoubleMatrix data, int period) {
		DoubleMatrix rollingStd = data.dup();
		rollingStd.fill(Double.NaN);

		int cols = data.columns;
		for (int i = period; i <= data.rows; i++) {
			int start = i - period;
			int end = i;
			for (int c = 0; c < cols; c++) {
				DoubleMatrix rowSubset = data.getRowRange(start, end, c);
				double std = stdv(rowSubset);
				rollingStd.put(end - 1, c, std);
			}
		}

		return rollingStd;
	}
}
