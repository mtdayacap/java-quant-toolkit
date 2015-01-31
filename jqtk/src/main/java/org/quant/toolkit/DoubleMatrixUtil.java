package org.quant.toolkit;

import org.jblas.DoubleMatrix;

public class DoubleMatrixUtil {

	/**
	 * Shift the matrix along the x-axis. 
	 * 
	 * @param m DoubleMatrix to shift values
	 * @param shift int - number of shifts from the x-axis
	 * 
	 * @TODO Shift along the y-axis
	 * 
	 * */
	public static void shift(DoubleMatrix m, int shift) {
		if (m == null || m.length == 0) {
			return;
		}
		
		DoubleMatrix n = new DoubleMatrix(m.rows, m.columns);
		boolean isNegativeShift = (shift < 0);
		boolean isPositiveShift = (shift > 0);
		shift = Math.abs(shift);
		boolean shiftEqualsRows = (shift == m.rows);
		boolean shiftGreaterThanRows = (shift > m.rows);
		
		int offset = m.rows;
		int colIndex = 0;
		if (shiftEqualsRows || shiftGreaterThanRows) {
			// copy zeros to matrix
			// see last line of method
		}  else if (isNegativeShift) {
			for (int i = shift, j = 0 ; i <= m.length - 1;) {
				double v = m.get(j);
				n.put(i, v);
				if (i == (colIndex + offset) - 1) {
					colIndex = colIndex + offset;
					i = i + shift + 1;
					j = colIndex;
				} else {
					j++;
					i++;
				}
			}
		
		} else if (isPositiveShift) {
			for (int i = 0, j = shift; j <= m.length - 1;) {
				double v = m.get(j);
				n.put(i, v);
				if (j == (colIndex + offset) - 1) {
					colIndex = colIndex + offset;
					j = colIndex + shift;
					i = colIndex;
				} else {
					j++;
					i++;
				}
			}
			
		} else {
			// shift equal to zero
			return;
		}
		
		m.copy(n);
	}

}
