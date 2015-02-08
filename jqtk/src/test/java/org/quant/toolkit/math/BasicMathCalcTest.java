package org.quant.toolkit.math;

import static org.junit.Assert.*;

import org.jblas.DoubleMatrix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicMathCalcTest {

	private static final String ALONG_THE_Y_AXIS = "Along the y-axis";
	private static final String ALONG_THE_X_AXIS = "Along the x-axis";
	private BasicMathCalc bsc;

	@Before
	public void setUp() throws Exception {
		bsc = new BasicMathCalc();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoubleMatrixMean() {
		DoubleMatrix m = new DoubleMatrix(2, 3, 1, 4, 2, 5, 3, 6);

		DoubleMatrix n = bsc.mean(m, 0);
		DoubleMatrix expN = new DoubleMatrix(1, 3, 2.500000, 3.500000, 4.500000);
		assertEquals(ALONG_THE_X_AXIS, expN, n);

		n = bsc.mean(m, 1);
		expN = new DoubleMatrix(1, 2, 2, 5);
		assertEquals(ALONG_THE_Y_AXIS, expN, n);
	}

	@Test
	public void testVectorMean() {
		DoubleMatrix v = new DoubleMatrix(3, 1, 1, 2, 3);
		DoubleMatrix act = bsc.mean(v, 0);

		DoubleMatrix exp = new DoubleMatrix(1, 1, 2);
		assertEquals(ALONG_THE_X_AXIS, exp, act);

		act = bsc.mean(v, 1);
		exp = new DoubleMatrix(1, 3, 1, 2, 3);
		assertEquals(ALONG_THE_Y_AXIS, exp, act);
	}

	@Test
	public void testDoubleMatrixMeanEmpty() {
		DoubleMatrix m = new DoubleMatrix();
		DoubleMatrix result = bsc.mean(m, 0);
		assertEquals(null, result);
	}

	@Test
	public void testDoubleMatrixMeanZeros() {
		DoubleMatrix m = new DoubleMatrix(2, 3);
		DoubleMatrix n = bsc.mean(m, 0);
		DoubleMatrix exp = DoubleMatrix.zeros(1, 3);
		assertEquals(ALONG_THE_X_AXIS, exp, n);

		n = bsc.mean(m, 1);
		exp = DoubleMatrix.zeros(1, 2);
		assertEquals(ALONG_THE_Y_AXIS, exp, n);
	}

	@Test
	public void testCumProdAlongTheXAxis() {
		DoubleMatrix m = new DoubleMatrix(5, 3, 1, 4, 7, 10, 13, 2, 5, 8, 11,
				14, 3, 6, 9, 12, 15);
		DoubleMatrix exp = new DoubleMatrix(m.rows, m.columns, 1, 4, 28, 280,
				3640, 2, 10, 80, 880, 12320, 3, 18, 162, 1944, 29160);
		DoubleMatrix act = bsc.cumProd(m, 0);
		assertEquals(exp, act);
	}
	
	@Test
	public void testCumProdAlongTheYAxis(){
		DoubleMatrix m = new DoubleMatrix(5, 3, 1, 4, 7, 10, 13, 2, 5, 8, 11,
				14, 3, 6, 9, 12, 15);
		DoubleMatrix exp = new DoubleMatrix(m.rows, m.columns, 1, 4, 7, 10,
				13, 2, 20, 56, 110, 182, 6, 120, 504, 1320, 2730);
		DoubleMatrix act = bsc.cumProd(m, 1);
		assertEquals(exp, act);
		
	}
}
