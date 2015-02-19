package org.quant.toolkit.chart;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class EventProfilerXYSeriesDemo extends ApplicationFrame {
	private static final long serialVersionUID = 1L;

	public EventProfilerXYSeriesDemo() {
		super("Event Profiler Demo Chart");
		JPanel jPanel = createDemoPanel();
		jPanel.setPreferredSize(new Dimension(1024, 720));
		setContentPane(jPanel);
	}

	private JPanel createDemoPanel() {
		JFreeChart jfreechart = createChart(createDataset());
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	private JFreeChart createChart(XYDataset xydataset) {
		JFreeChart jfreechart = ChartFactory.createXYLineChart(
				"XY Series Demo 1", "X", "Y", xydataset,
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);
		NumberAxis numberaxis = new NumberAxis(null);
		xyplot.setDomainAxis(1, numberaxis);
		NumberAxis numberaxis1 = new NumberAxis(null);
		xyplot.setRangeAxis(1, numberaxis1);
		List<Integer> list = Arrays.asList(new Integer[] { new Integer(0),
				new Integer(1) });
		xyplot.mapDatasetToDomainAxes(0, list);
		xyplot.mapDatasetToRangeAxes(0, list);
		ChartUtilities.applyCurrentTheme(jfreechart);
		return jfreechart;
	}

	private XYDataset createDataset() {
		XYSeries xySeries1 = new XYSeries(
				"Market Relative mean return of 414 events");
		xySeries1.add(-10, 1.08D);
		xySeries1.add(-9, 1.07D);
		xySeries1.add(-8, 1.09D);
		xySeries1.add(-7, 1.06D);
		xySeries1.add(-6, 1.07D);
		xySeries1.add(-5, 1.09D);
		xySeries1.add(-4, 1.08D);
		xySeries1.add(-3, 1.09D);
		xySeries1.add(-2, 1.07D);
		xySeries1.add(-1, 1.08D);
		xySeries1.add(0, 1.0D);
		xySeries1.add(1, 1.01D);
		xySeries1.add(2, 1.02D);
		xySeries1.add(3, 1.04D);
		xySeries1.add(4, 1.05D);
		xySeries1.add(5, 1.06D);
		xySeries1.add(6, 1.05D);
		xySeries1.add(7, 1.07D);
		xySeries1.add(8, 1.09D);
		xySeries1.add(9, 1.08D);
		xySeries1.add(10, 1.10D);

		XYSeries xySeries2 = new XYSeries("Benchmark");
		xySeries2.add(-10, 1.0D);
		xySeries2.add(-9, 1.0D);
		xySeries2.add(-8, 1.0D);
		xySeries2.add(-7, 1.0D);
		xySeries2.add(-6, 1.0D);
		xySeries2.add(-5, 1.0D);
		xySeries2.add(-4, 1.0D);
		xySeries2.add(-3, 1.0D);
		xySeries2.add(-2, 1.0D);
		xySeries2.add(-1, 1.0D);
		xySeries2.add(0, 1.0D);
		xySeries2.add(1, 1.0D);
		xySeries2.add(2, 1.0D);
		xySeries2.add(3, 1.0D);
		xySeries2.add(4, 1.0D);
		xySeries2.add(5, 1.0D);
		xySeries2.add(6, 1.0D);
		xySeries2.add(7, 1.0D);
		xySeries2.add(8, 1.0D);
		xySeries2.add(9, 1.0D);
		xySeries2.add(10, 1.0D);

		XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(xySeries1);
		 collection.addSeries(xySeries2);

		return collection;
	}

	public static void main(String[] args) {
		EventProfilerXYSeriesDemo epDemoChart = new EventProfilerXYSeriesDemo();
		epDemoChart.pack();
		RefineryUtilities.centerFrameOnScreen(epDemoChart);
		epDemoChart.setVisible(true);

	}
}
