package org.quant.toolkit.math;

import static org.junit.Assert.assertEquals;
import static org.quant.toolkit.math.BasicStatFunctions.X;
import static org.quant.toolkit.math.BasicStatFunctions.Y;

import org.jblas.DoubleMatrix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicStatFunctionsTest {

	private static final String ALONG_THE_Y_AXIS = "Along the y-axis";
	private static final String ALONG_THE_X_AXIS = "Along the x-axis";
	private BasicStatFunctions bsc;

	@Before
	public void setUp() throws Exception {
		bsc = new BasicStatFunctions();
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
	}

	@Test
	public void testMatrixMeanAlongY() {
		DoubleMatrix m = new DoubleMatrix(2, 3, 1, 4, 2, 5, 3, 6);
		DoubleMatrix act = bsc.mean(m, Y);
		DoubleMatrix exp = new DoubleMatrix(1, 2, 2, 5);
		assertEquals(exp, act);
	}

	@Test
	public void testColumnVectorMean() {
		DoubleMatrix v = new DoubleMatrix(3, 1, 1, 2, 3);
		double act = bsc.meanv(v);
		double exp = 2d;
		assertEquals(exp, act, 1e-6);
	}

	@Test
	public void testRowVectorMean() {
		DoubleMatrix v = new DoubleMatrix(new double[] { 1, 2, 3 });
		double act = bsc.meanv(v);
		double exp = 2d;
		assertEquals(exp, act, 1e-6);
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
		DoubleMatrix act = bsc.cumProdv(v);
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
	public void testStdAlongY() {
		DoubleMatrix m = new DoubleMatrix(new double[][] { { 1, 2, 3 },
				{ 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 }, { 13, 14, 15 } });
		DoubleMatrix act = bsc.std(m, Y);
		DoubleMatrix exp = new DoubleMatrix(new double[][] { { 0.81649658,
				0.81649658, 0.81649658, 0.81649658, 0.81649658 } });
		assertEquals(exp, act);
	}

	@Test
	public void testStdVector() {
		DoubleMatrix v = new DoubleMatrix(
				new double[] { 2, 4, 4, 4, 5, 5, 7, 9 });
		double act = bsc.stdv(v);
		double exp = 2.13808994d;
		assertEquals(exp, act, 1e-8);
	}

	@Test
	public void testStdZerosMatrix() {
		DoubleMatrix z = DoubleMatrix.zeros(3, 3);
		DoubleMatrix act = bsc.std(z, X);
		DoubleMatrix exp = DoubleMatrix.zeros(1, 3);
		assertEquals(exp, act);
	}

	@Test
	public void testStdScalarWithAxis() {
		DoubleMatrix s = new DoubleMatrix(new double[] { 2d });
		DoubleMatrix act = bsc.std(s, Y);
		DoubleMatrix exp = s.dup();
		assertEquals(exp, act);
	}

	@Test
	public void testRollingMeanVector() {
		DoubleMatrix v = new DoubleMatrix(new double[] { -0.057580, 0.423319,
				0.325680, 0.735018, -0.773159, -1.036093, 0.111009, -1.234435,
				-1.354545, -0.334807, -0.338397, -1.172990 });

		DoubleMatrix act = bsc.rollingMean(v, 3);

		DoubleMatrix exp = new DoubleMatrix(new double[] { Double.NaN,
				Double.NaN, 0.230473, 0.494672, 0.095846, -0.358078, -0.566081,
				-0.719840, -0.825990, -0.974596, -0.675916, -0.615398 });

		assertEquals(exp, act);
	}

	@Test
	public void testRollingMeanMatrix() {
		DoubleMatrix m = new DoubleMatrix(new double[][] {
				{ -0.279129, -1.685572, -0.641290 },
				{ -2.016775, -1.384767, 0.428416 },
				{ 0.218845, -0.332732, -0.296181 },
				{ -0.297286, 0.199395, -0.224958 },
				{ 0.653653, 0.014353, -0.395487 },
				{ -1.619624, 1.401901, -0.002540 },
				{ -0.208399, 1.638171, -0.382625 },
				{ 1.017794, -1.233941, 0.669265 },
				{ 0.215117, 0.175358, -0.536862 },
				{ 0.153521, -1.194821, 0.357967 },
				{ -0.073379, -0.198996, 0.819588 },
				{ 0.746991, -1.942960, 0.579532 } });

		DoubleMatrix act = bsc.rollingMean(m, 3);

		DoubleMatrix exp = new DoubleMatrix(new double[][] {
				{ Double.NaN, Double.NaN, Double.NaN },
				{ Double.NaN, Double.NaN, Double.NaN },
				{ -0.692353, -1.134357, -0.169685 },
				{ -0.698406, -0.506035, -0.030908 },
				{ 0.191737, -0.039661, -0.305542 },
				{ -0.421086, 0.538549, -0.207662 },
				{ -0.391457, 1.018141, -0.260217 },
				{ -0.270077, 0.602043, 0.094700 },
				{ 0.341504, 0.193196, -0.083408 },
				{ 0.462144, -0.751134, 0.163457 },
				{ 0.098420, -0.406153, 0.213564 },
				{ 0.275711, -1.112259, 0.585696 } });

		assertEquals(exp, act);
	}

	@Test
	public void testRollingStd() {

		DoubleMatrix m = new DoubleMatrix(new double[][] {
				{ -0.086437, -1.717556, 0.912794 },
				{ -0.171935, -0.626212, -1.279311 },
				{ -0.151074, -0.712169, -0.754326 },
				{ 0.801602, -0.631674, -0.837987 },
				{ 0.213153, -1.061656, 0.191456 },
				{ 0.304227, -1.749945, -2.286783 },
				{ -0.782282, 0.610939, -0.092666 },
				{ 0.301908, -0.258025, -0.047517 },
				{ -0.231921, 0.029943, -0.590623 },
				{ -1.614179, 0.350191, 0.615890 },
				{ -0.069763, -0.902364, 0.628749 },
				{ -0.258155, 0.281268, -0.632680 }, });

		DoubleMatrix act = bsc.rollingStd(m, 3);

		DoubleMatrix exp = new DoubleMatrix(new double[][] {
				{ Double.NaN, Double.NaN, Double.NaN },
				{ Double.NaN, Double.NaN, Double.NaN },
				{ 0.044578, 0.606798, 1.144569 },
				{ 0.556148, 0.048128, 0.282068 },
				{ 0.480716, 0.228584, 0.571730 },
				{ 0.316741, 0.564086, 1.245019 },
				{ 0.602728, 1.214159, 1.356254 },
				{ 0.626628, 1.194062, 1.280007 },
				{ 0.542116, 0.442640, 0.301376 },
				{ 0.988855, 0.304251, 0.604256 },
				{ 0.848740, 0.650724, 0.700322 },
				{ 0.842567, 0.704111, 0.724603 }, });

		assertEquals(exp, act);
	}
}
