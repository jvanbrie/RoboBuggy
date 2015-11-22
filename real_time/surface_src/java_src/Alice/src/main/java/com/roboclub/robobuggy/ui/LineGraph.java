package com.roboclub.robobuggy.ui;

import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.roboclub.robobuggy.map.Point;

/**
 * Object used for creating line graphs in the GUI
 */
public class LineGraph extends JPanel {
	
	private static final long serialVersionUID = 170388164155408874L;
	private static final int WIDTH = 300;
	private static final int HEIGHT = 300;
	
	private XYSeriesCollection dataset;
	private final XYSeries series1;
	
	/**
	 * Constructs a new LineGraph object
	 * @param title of the graph
	 * @param xLabel label of the x axis
	 * @param yLabel label of the y axis
	 */
	public LineGraph(String title, String xLabel, String yLabel) {
        dataset = new XYSeriesCollection();
		series1 = new XYSeries("series1");
		dataset.addSeries(series1);
        boolean legend = false, tooltips = false, url = false;
        JFreeChart chart = ChartFactory.createXYLineChart(title,
                xLabel, yLabel, dataset, PlotOrientation.VERTICAL,
                legend, tooltips, url);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.add(chartPanel);
	}
	
	/**
	 * Update the data being shown on the line graph
	 * @param point next point to display on the graph
	 */
	public void updateGraph(Point point) {
		series1.add(point.getX(), point.getY());
	}
}
