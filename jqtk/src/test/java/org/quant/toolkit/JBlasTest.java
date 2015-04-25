package org.quant.toolkit;

import static org.junit.Assert.assertEquals;

import org.jblas.DoubleMatrix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JBlasTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoubleMatrixMatrixCreation() {
		DoubleMatrix matrix = new DoubleMatrix(10, 5);
		System.out.println("matrix" + matrix);

		DoubleMatrix vector = new DoubleMatrix(10);
		System.out.println("vector: " + vector);

		DoubleMatrix zeros = DoubleMatrix.zeros(10, 5);
		System.out.println("zeros: " + zeros);

		DoubleMatrix ones = DoubleMatrix.ones(3, 4);
		System.out.println("ones: " + ones);

		DoubleMatrix randn = DoubleMatrix.randn(3, 4);
		System.out.println("randn: " + randn);

		DoubleMatrix rand = DoubleMatrix.rand(3);
		System.out.println("rand: " + rand);

		// Get properties
		int row = matrix.getRows();
		System.out.println("matrix.row: " + row);

		int col = matrix.getColumns();
		System.out.println("matrix col: " + col);

		int length = matrix.length;
		System.out.println("matrix.length: " + length);

		DoubleMatrix matrixDup = matrix.dup();
		int lengthDup = matrixDup.length;
		System.out.println("matrix dup lenght: " + lengthDup);

		DoubleMatrix randnCopy = new DoubleMatrix(3, 3);
		randnCopy.copy(randn);
		System.out.println("randnCopy copy: " + randnCopy);

		DoubleMatrix randnTrans = randnCopy.transpose();
		System.out.println("randnCopy transpose: " + randnTrans);
	}

	@Test
	public void testDoubleMatrixElementAcces() {
		DoubleMatrix matrix = new DoubleMatrix(10, 5);

		// accessing elements by row and columns
		matrix.put(3, 2, 10.0);
		System.out.println("matrix.put(3,2,10.0): " + matrix.get(3, 2));
		double val = matrix.get(2, 3);
		System.out.println("matrix.get(2,3): " + val);

		// accessing elements in linear
		val = matrix.get(15);
		System.out.println("matrix.get(15): " + val);
		matrix.put(20, 1.0);
		System.out.println("matrix.put(20,1.0): " + matrix.get(20));

		// for implementing elementwise operations
		System.out.println("Multiply each element by 3: " + matrix);
		for (int i = 0; i < matrix.length; i++) {
			matrix.put(i, matrix.get(i) * 3);
		}
		System.out.println("matrix multiplied by 3: " + matrix);

		// Get column
		DoubleMatrix randn = DoubleMatrix.randn(10, 5);
		DoubleMatrix buff = new DoubleMatrix(10);
		System.out.println("randn: " + randn);
		for (int i = 0; i < randn.columns; i++) {
			randn.getColumn(i, buff);
			System.out.println("col[" + i + "]: " + buff);
		}

	}

	@Test
	public void testDoubleMatrixOperations() {
		DoubleMatrix a = new DoubleMatrix(3, 3, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		System.out.println("a: " + a);
		DoubleMatrix x = new DoubleMatrix(3, 1, 10, 11, 12);
		System.out.println("x: " + x);

		DoubleMatrix y = a.mmul(x);
		System.out.println("a.mmul(x) = " + y);
		DoubleMatrix z = y.add(x);
		System.out.println("y.add(x) = " + z);

	}

	@Test
	public void testDoubleMatrixUtilShift() {
		DoubleMatrix m = new DoubleMatrix(4, 2, 1.1, 2.2, 3.3, 4.4, 5.5, 6.6,
				7.7, 8.8);
		System.out.println("ORIG: " + m);
		DoubleMatrixUtil.shift(m, 2);
		System.out.println("SHIFTED: " + m);

		m = new DoubleMatrix(3, 3, 1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9);
		System.out.println("ORIG: " + m);
		DoubleMatrixUtil.shift(m, 1);
		System.out.println("SHIFTED: " + m);

		m = new DoubleMatrix(2, 4, 1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8);
		System.out.println("ORIG: " + m);
		DoubleMatrixUtil.shift(m, -2);
		System.out.println("SHIFTED: " + m);

		m = new DoubleMatrix(3, 1, 1.1, 2.2, 3.3);
		System.out.println("ORIG: " + m);
		DoubleMatrixUtil.shift(m, -2);
		System.out.println("SHIFTED " + -2 + ": " + m);

		m = new DoubleMatrix(1, 3, 1.1, 2.2, 3.3);
		System.out.println("ORIG: " + m);
		DoubleMatrixUtil.shift(m, -2);
		System.out.println("SHIFTED " + -2 + ": " + m);

	}

	@Test
	public void testDoubleMatrixaMeanOps() {
		DoubleMatrix m = new DoubleMatrix(2, 3, 1, 4, 2, 5, 3, 6);
		System.out.println("m: " + m);

		// Get the columns
		int[] c = new int[m.columns];
		for (int i = 0; i < m.columns; i++) {
			c[i] = i;
		}
		DoubleMatrix n = m.get(0, c);
		System.out.println("n: " + n);
		for (int r = 1; r < m.rows; r++) {
			DoubleMatrix l = m.get(r, c);
			System.out.println("l: " + l);
			n = n.addi(l);
			System.out.println("n: " + n);
		}
		n.divi(m.rows);
		System.out.println("ave n: " + n);
	}

	@Test
	public void testPutRowOrColumnOps() {
		DoubleMatrix m = new DoubleMatrix(3, 3);
		System.out.println("m: " + m);
		DoubleMatrix v = DoubleMatrix.ones(3, 1);
		System.out.println("v: " + v);
		m.putColumn(0, v);
		System.out.println("Put Col m: " + m);

		m = DoubleMatrix.zeros(3, 3);
		v = DoubleMatrix.ones(1, 3);
		m.putRow(0, v);
		System.out.println("Put Row m: " + m);
	}

	@Test
	public void testResizeOfDoubleMatrix() {
		// Along the X-axis
		DoubleMatrix m = new DoubleMatrix(1, 3, 1, 2, 3);
		System.out.println("m:" + m);
		DoubleMatrix ones = DoubleMatrix.zeros(3, 3);
		DoubleMatrix resize = ones.mulRowVector(m);
		System.out.println("ones:" + resize);
	}

	@Test
	public void testRemoveColumn() {
		DoubleMatrix m = new DoubleMatrix(new double[][] { { 1, 2, 3 },
				{ 4, 5, 6 }, { 7, 8, 9 } });
		System.out.println("m: " + m);
		DoubleMatrix n = new DoubleMatrix(3, 2);

		// Transfer columns to new matrix
		int removeColIndex = 1;
		int cols = m.columns;
		for (int i = 0, j = 0; i < cols; i++) {
			if (removeColIndex != i) {
				// Skip column to be removed
				// Copy column to new Matrix
				DoubleMatrix vCol = m.getColumn(i);
				n.putColumn(j, vCol);
				j++;
			}
		}

		DoubleMatrix o = new DoubleMatrix(new double[][] { { 1, 3 }, { 4, 6 },
				{ 7, 9 } });

		assertEquals(o, n);
	}

	@Test
	public void testPutRowVectorValues() {
		DoubleMatrix m = new DoubleMatrix(new double[][] { { 1, 2, 3 },
				{ 4, 5, 6 } });
		DoubleMatrix v = new DoubleMatrix(new double[] { 9, 9, 9 });

		// Expected
		DoubleMatrix e = new DoubleMatrix(new double[][] { { 9, 9, 9 },
				{ 4, 5, 6 } });

		m.putRow(0, v);
		assertEquals(e, m);
	}

	@Test
	public void testSubColumnVector() {
		DoubleMatrix m = new DoubleMatrix(new double[][] { { 2, 1 }, { 2, 1 },
				{ 2, 1 } });
		DoubleMatrix exp = new DoubleMatrix(new double[][]{{ 1, 0 }, { 1, 0 },
				{ 1, 0 } });
		
		DoubleMatrix act = m.subColumnVector(m.getColumn(1));
		assertEquals(exp, act);
	}
}
