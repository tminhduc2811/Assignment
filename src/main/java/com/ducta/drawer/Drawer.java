package com.ducta.drawer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Drawer {

    public static void drawFromRawData(TreeMap<String, TreeMap<String, Integer>> casesByStateAndDate, String outputFolder) throws IOException {
        System.out.println("Drawing images from raw data...");
        for (Map.Entry<String, TreeMap<String, Integer>> entry : casesByStateAndDate.entrySet()) {
            String state = entry.getKey();

            TimeSeries series = new TimeSeries("Number of cases for " + state);
            TimeSeries seriesTotal = new TimeSeries("Total cases for " + state);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date date = null;
            int total = 0;
            Map<String, Integer> casesByDate = casesByStateAndDate.get(state);
            for (Map.Entry<String, Integer> entry1 : casesByDate.entrySet()) {
                try {
                    date = sdf.parse(entry1.getKey());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                assert date != null;
                series.add(new Day(date), entry1.getValue());
                total += entry1.getValue();
                seriesTotal.add(new Day(date), total);
            }

            TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection(series);
            timeSeriesCollection.addSeries(seriesTotal);

            JFreeChart timechart = ChartFactory.createTimeSeriesChart(
                    "Data",
                    "Time",
                    "Cases",
                    (XYDataset) timeSeriesCollection,
                    true,
                    false,
                    false);
            int width = 560;   /* Width of the image */
            int height = 370;  /* Height of the image */
            File timeChart = new File(outputFolder + "/" + state + ".PNG");
            ChartUtilities.saveChartAsJPEG(timeChart, timechart, width, height);
        }
        System.out.println("Done.");
    }
}
