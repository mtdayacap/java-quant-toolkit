package org.quant.toolkit.math;

import static org.quant.toolkit.math.BasicStatCalc.X;
import static org.quant.toolkit.math.BasicStatCalc.Y;
import static org.junit.Assert.assertEquals;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicStatCalcTest {

	private static final String ALONG_THE_Y_AXIS = "Along the y-axis";
	private static final String ALONG_THE_X_AXIS = "Along the x-axis";
	private BasicStatCalc bsc;

	@Before
	public void setUp() throws Exception {
		bsc = new BasicStatCalc();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoubleMatrixMean() {
		DoubleMatrix m = new DoubleMatrix(2, 3, 1, 4, 2, 5, 3, 6);

		DoubleMatrix n = bsc.mean(m, X);
		DoubleMatrix expN = new DoubleMatrix(1, 3, 2.500000, 3.500000, 4.500000);
		assertEquals(ALONG_THE_X_AXIS, expN, n);

		n = bsc.mean(m, Y);
		expN = new DoubleMatrix(1, 2, 2, 5);
		assertEquals(ALONG_THE_Y_AXIS, expN, n);
	}

	@Test
	public void testVectorMean() {
		DoubleMatrix v = new DoubleMatrix(3, 1, 1, 2, 3);
		DoubleMatrix act = bsc.mean(v, X);

		DoubleMatrix exp = new DoubleMatrix(1, 1, 2);
		assertEquals(ALONG_THE_X_AXIS, exp, act);

		act = bsc.mean(v, Y);
		exp = new DoubleMatrix(1, 3, 1, 2, 3);
		assertEquals(ALONG_THE_Y_AXIS, exp, act);
	}

	@Test
	public void testDoubleMatrixMeanEmpty() {
		DoubleMatrix m = new DoubleMatrix();
		DoubleMatrix result = bsc.mean(m, X);
		assertEquals(null, result);
	}

	@Test
	public void testDoubleMatrixMeanZeros() {
		DoubleMatrix m = new DoubleMatrix(2, 3);
		DoubleMatrix n = bsc.mean(m, X);
		DoubleMatrix exp = DoubleMatrix.zeros(1, 3);
		assertEquals(ALONG_THE_X_AXIS, exp, n);

		n = bsc.mean(m, Y);
		exp = DoubleMatrix.zeros(1, 2);
		assertEquals(ALONG_THE_Y_AXIS, exp, n);
	}

	@Test
	public void testCumProdAlongTheXAxis() {
		DoubleMatrix m = new DoubleMatrix(5, 3, 1, 4, 7, 10, 13, 2, 5, 8, 11,
				14, 3, 6, 9, 12, 15);
		DoubleMatrix exp = new DoubleMatrix(m.rows, m.columns, 1, 4, 28, 280,
				3640, 2, 10, 80, 880, 12320, 3, 18, 162, 1944, 29160);
		DoubleMatrix act = bsc.cumProd(m, X);
		assertEquals(exp, act);
	}

	@Test
	public void testCumProdAlongTheYAxis() {
		DoubleMatrix m = new DoubleMatrix(5, 3, 1, 4, 7, 10, 13, 2, 5, 8, 11,
				14, 3, 6, 9, 12, 15);
		DoubleMatrix exp = new DoubleMatrix(m.rows, m.columns, 1, 4, 7, 10, 13,
				2, 20, 56, 110, 182, 6, 120, 504, 1320, 2730);
		DoubleMatrix act = bsc.cumProd(m, Y);
		assertEquals(exp, act);

	}

	@Test
	public void testCumProdVectorAlongTheXAxis() {
		DoubleMatrix v = new DoubleMatrix(5, 1, 1, 2, 3, 4, 5);
		DoubleMatrix act = bsc.cumProd(v, X);
		DoubleMatrix exp = new DoubleMatrix(5, 1, 1, 2, 6, 24, 120);
		assertEquals(exp, act);
	}

	@Test
	public void testCumProdVectorAlongYAxis() {
		DoubleMatrix v = new DoubleMatrix(5, 1, 1, 2, 3, 4, 5);
		DoubleMatrix act = bsc.cumProd(v, Y);
		DoubleMatrix exp = v.dup();
		assertEquals(exp, act);
	}

	@Test
	public void testCumProdEmptyDoubleMatrix() {
		DoubleMatrix m = new DoubleMatrix();
		DoubleMatrix act = bsc.cumProd(m, X);
		assertEquals(null, act);
	}

	@Test
	public void testCumProdZerosDoubleMatrix() {
		DoubleMatrix z = new DoubleMatrix(3, 3);
		DoubleMatrix act = bsc.cumProd(z, X);
		DoubleMatrix exp = DoubleMatrix.zeros(z.rows, z.columns);
		assertEquals(exp, act);
	}

	@Test
	public void testStdDoubleMatrixAlongX() {
		DoubleMatrix m = new DoubleMatrix(8, 2, 2, 4, 4, 4, 5, 5, 7, 9, 2, 4,
				4, 4, 5, 5, 7, 9);
		DoubleMatrix act = bsc.std(m, X);
		DoubleMatrix exp = new DoubleMatrix(1, 2, 2, 2);
		assertEquals(exp, act);

	}

	@Test
	public void testStdVector(){
		DoubleMatrix v = new DoubleMatrix(new double[]{2, 4, 4, 4, 5, 5, 7, 9});
		double act = bsc.stdv(v);
		double exp = 2d;
		assertEquals(exp, act, 0d);
	}
	
	@Test
	public void testStdEmptyMatrix(){
		DoubleMatrix e = new DoubleMatrix();
		DoubleMatrix act = bsc.std(e, X);
		assertEquals(null, act);
	}
	
	@Test
	public void testStdZerosMatrix(){
		DoubleMatrix z = DoubleMatrix.zeros(3,3);
		DoubleMatrix act = bsc.std(z, X);
		DoubleMatrix exp = DoubleMatrix.zeros(1, 3);
		assertEquals(exp, act);
	}
}
