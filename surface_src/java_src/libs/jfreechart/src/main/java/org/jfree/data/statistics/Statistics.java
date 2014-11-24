/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2014, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.]
 *
 * ---------------
 * Statistics.java
 * ---------------
 * (C) Copyright 2000-2014, by Matthew Wright and Contributors.
 *
 * Original Author:  Matthew Wright;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes (from 08-Nov-2001)
 * --------------------------
 * 08-Nov-2001 : Added standard header and tidied Javadoc comments (DG);
 *               Moved from JFreeChart to package com.jrefinery.data.* in
 *               JCommon class library (DG);
 * 24-Jun-2002 : Removed unnecessary local variable (DG);
 * 07-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 26-May-2004 : Moved calculateMean() method from BoxAndWhiskerCalculator (DG);
 * 02-Jun-2004 : Fixed bug in calculateMedian() method (DG);
 * 11-Jan-2005 : Removed deprecated code in preparation for the 1.0.0
 *               release (DG);
 *
 */

package org.jfree.data.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jfree.chart.util.ParamChecks;

/**
 * A utility class that provides some common statistical functions.
 */
public abstract class Statistics {

    /**
     * Returns the mean of an array of numbers.  This is equivalent to calling
     * {@code calculateMean(values, true)}.
     *
     * @param values  the values ({@code null} not permitted).
     *
     * @return The mean.
     */
    public static double calculateMean(Number[] values) {
        return calculateMean(values, true);
    }

    /**
     * Returns the mean of an array of numbers.
     *
     * @param values  the values ({@code null} not permitted).
     * @param includeNullAndNaN  a flag that controls whether or not
     *     {@code null} and {@code Double.NaN} values are included
     *     in the calculation (if either is present in the array, the result is
     *     {@link Double#NaN}).
     *
     * @return The mean.
     *
     * @since 1.0.3
     */
    public static double calculateMean(Number[] values,
            boolean includeNullAndNaN) {

        ParamChecks.nullNotPermitted(values, "values");
        double sum = 0.0;
        double current;
        int counter = 0;
        for (Number value : values) {
            // treat nulls the same as NaNs
            if (value != null) {
                current = value.doubleValue();
            } else {
                current = Double.NaN;
            }
            // calculate the sum and count
            if (includeNullAndNaN || !Double.isNaN(current)) {
                sum = sum + current;
                counter++;
            }
        }
        double result = (sum / counter);
        return result;
    }

    /**
     * Returns the mean of a collection of {@code Number} objects.
     *
     * @param values  the values ({@code null} not permitted).
     *
     * @return The mean.
     */
    public static double calculateMean(Collection<Number> values) {
        return calculateMean(values, true);
    }

    /**
     * Returns the mean of a collection of {@code Number} objects.
     *
     * @param values  the values ({@code null} not permitted).
     * @param includeNullAndNaN  a flag that controls whether or not
     *     {@code null} and {@code Double.NaN} values are included
     *     in the calculation (if either is present in the array, the result is
     *     {@link Double#NaN}).
     *
     * @return The mean.
     *
     * @since 1.0.3
     */
    public static double calculateMean(Collection<Number> values,
            boolean includeNullAndNaN) {

        ParamChecks.nullNotPermitted(values, "values");
        int count = 0;
        double total = 0.0;
        for (Number n : values) {
            if (n == null) {
                if (includeNullAndNaN) {
                    return Double.NaN;
                }
                else {
                    continue;
                }
            }
            double value = n.doubleValue();
            if (Double.isNaN(value)) {
                if (includeNullAndNaN) {
                    return Double.NaN;
                }
                else {
                    continue;
                }
            }
            total = total + value;
            count = count + 1;
        }
        return total / count;
    }

    /**
     * Calculates the median for a list of values ({@code Number} objects).
     * The list of values will be copied, and the copy sorted, before
     * calculating the median.  To avoid this step (if your list of values
     * is already sorted), use the {@link #calculateMedian(List, boolean)}
     * method.
     *
     * @param values  the values ({@code null} permitted).
     *
     * @return The median.
     */
    public static double calculateMedian(List<Number> values) {
        return calculateMedian(values, true);
    }

    /**
     * Calculates the median for a list of values ({@code Number} objects).
     * If {@code copyAndSort} is {@code false}, the list is assumed
     * to be presorted in ascending order by value.
     *
     * @param values  the values ({@code null} permitted).
     * @param copyAndSort  a flag that controls whether the list of values is
     *                     copied and sorted.
     *
     * @return The median.
     */
    public static double calculateMedian(List<Number> values,
            boolean copyAndSort) {

        if (values == null) {
            return Double.NaN;
        }
        double result = Double.NaN;
        if (copyAndSort) {
            List<Number> copy = new ArrayList<Number>(values);
            Collections.sort((List) copy);
            values = copy;
        }
        int count = values.size();
        if (count > 0) {
            if (count % 2 == 1) {
                if (count > 1) {
                    Number value = values.get((count - 1) / 2);
                    result = value.doubleValue();
                }
                else {
                    Number value = values.get(0);
                    result = value.doubleValue();
                }
            }
            else {
                Number value1 = values.get(count / 2 - 1);
                Number value2 = values.get(count / 2);
                result = (value1.doubleValue() + value2.doubleValue()) / 2.0;
            }
        }
        return result;
    }

    /**
     * Calculates the median for a sublist within a list of values
     * ({@code Number} objects).
     *
     * @param values  the values, in any order ({@code null} not permitted).
     * @param start  the start index.
     * @param end  the end index.
     *
     * @return The median.
     */
    public static double calculateMedian(List<Number> values, int start, int end) {
        return calculateMedian(values, start, end, true);
    }

    /**
     * Calculates the median for a sublist within a list of values
     * ({@code Number} objects).  The entire list will be sorted if the
     * {@code ascending} argument is {@code false}.
     *
     * @param values  the values ({@code null} not permitted).
     * @param start  the start index.
     * @param end  the end index.
     * @param copyAndSort  a flag that that controls whether the list of values
     *                     is copied and sorted.
     *
     * @return The median.
     */
    public static double calculateMedian(List<Number> values,
            int start, int end, boolean copyAndSort) {

        double result = Double.NaN;
        if (copyAndSort) {
            List<Number> working = new ArrayList<Number>(end - start + 1);
            for (int i = start; i <= end; i++) {
                working.add(values.get(i));
            }
            Collections.sort((List) working);
            result = calculateMedian(working, false);
        }
        else {
            int count = end - start + 1;
            if (count > 0) {
                if (count % 2 == 1) {
                    if (count > 1) {
                        Number value = values.get(start + (count - 1) / 2);
                        result = value.doubleValue();
                    }
                    else {
                        Number value = values.get(start);
                        result = value.doubleValue();
                    }
                }
                else {
                    Number v1 = values.get(start + count / 2 - 1);
                    Number v2 = values.get(start + count / 2);
                    result = (v1.doubleValue() + v2.doubleValue()) / 2.0;
                }
            }
        }
        return result;

    }

    /**
     * Returns the standard deviation of a set of numbers.
     *
     * @param data  the data ({@code null} or zero length array not
     *     permitted).
     *
     * @return The standard deviation of a set of numbers.
     */
    public static double getStdDev(Number[] data) {
        ParamChecks.nullNotPermitted(data, "data");
        if (data.length == 0) {
            throw new IllegalArgumentException("Zero length 'data' array.");
        }
        double avg = calculateMean(data);
        double sum = 0.0;

        for (Number aData : data) {
            double diff = aData.doubleValue() - avg;
            sum = sum + diff * diff;
        }
        return Math.sqrt(sum / (data.length - 1));
    }

    /**
     * Fits a straight line to a set of (x, y) data, returning the slope and
     * intercept.
     *
     * @param xData  the x-data ({@code null} not permitted).
     * @param yData  the y-data ({@code null} not permitted).
     *
     * @return A double array with the intercept in [0] and the slope in [1].
     */
    public static double[] getLinearFit(Number[] xData, Number[] yData) {

        ParamChecks.nullNotPermitted(xData, "xData");
        ParamChecks.nullNotPermitted(yData, "yData");
        if (xData.length != yData.length) {
            throw new IllegalArgumentException(
                "Statistics.getLinearFit(): array lengths must be equal.");
        }

        double[] result = new double[2];
        // slope
        result[1] = getSlope(xData, yData);
        // intercept
        result[0] = calculateMean(yData) - result[1] * calculateMean(xData);

        return result;

    }

    /**
     * Finds the slope of a regression line using least squares.
     *
     * @param xData  the x-values ({@code null} not permitted).
     * @param yData  the y-values ({@code null} not permitted).
     *
     * @return The slope.
     */
    public static double getSlope(Number[] xData, Number[] yData) {
        ParamChecks.nullNotPermitted(xData, "xData");
        ParamChecks.nullNotPermitted(yData, "yData");
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("Array lengths must be equal.");
        }

        // ********* stat function for linear slope ********
        // y = a + bx
        // a = ybar - b * xbar
        //     sum(x * y) - (sum (x) * sum(y)) / n
        // b = ------------------------------------
        //     sum (x^2) - (sum(x)^2 / n
        // *************************************************

        // sum of x, x^2, x * y, y
        double sx = 0.0, sxx = 0.0, sxy = 0.0, sy = 0.0;
        int counter;
        for (counter = 0; counter < xData.length; counter++) {
            sx = sx + xData[counter].doubleValue();
            sxx = sxx + Math.pow(xData[counter].doubleValue(), 2);
            sxy = sxy + yData[counter].doubleValue()
                      * xData[counter].doubleValue();
            sy = sy + yData[counter].doubleValue();
        }
        return (sxy - (sx * sy) / counter) / (sxx - (sx * sx) / counter);

    }

    /**
     * Calculates the correlation between two datasets.  Both arrays should
     * contain the same number of items.  Null values are treated as zero.
     * <P>
     * Information about the correlation calculation was obtained from:
     *
     * http://trochim.human.cornell.edu/kb/statcorr.htm
     *
     * @param data1  the first dataset.
     * @param data2  the second dataset.
     *
     * @return The correlation.
     */
    public static double getCorrelation(Number[] data1, Number[] data2) {
        ParamChecks.nullNotPermitted(data1, "data1");
        ParamChecks.nullNotPermitted(data2, "data2");
        if (data1.length != data2.length) {
            throw new IllegalArgumentException(
                "'data1' and 'data2' arrays must have same length.");
        }
        int n = data1.length;
        double sumX = 0.0;
        double sumY = 0.0;
        double sumX2 = 0.0;
        double sumY2 = 0.0;
        double sumXY = 0.0;
        for (int i = 0; i < n; i++) {
            double x = 0.0;
            if (data1[i] != null) {
                x = data1[i].doubleValue();
            }
            double y = 0.0;
            if (data2[i] != null) {
                y = data2[i].doubleValue();
            }
            sumX = sumX + x;
            sumY = sumY + y;
            sumXY = sumXY + (x * y);
            sumX2 = sumX2 + (x * x);
            sumY2 = sumY2 + (y * y);
        }
        return (n * sumXY - sumX * sumY) / Math.pow((n * sumX2 - sumX * sumX)
                * (n * sumY2 - sumY * sumY), 0.5);
    }

    /**
     * Returns a data set for a moving average on the data set passed in.
     *
     * @param xData  an array of the x data.
     * @param yData  an array of the y data.
     * @param period  the number of data points to average
     *
     * @return A double[][] the length of the data set in the first dimension,
     *         with two doubles for x and y in the second dimension
     */
    public static double[][] getMovingAverage(Number[] xData, Number[] yData,
            int period) {

        // check arguments...
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("Array lengths must be equal.");
        }

        if (period > xData.length) {
            throw new IllegalArgumentException(
                "Period can't be longer than dataset.");
        }

        double[][] result = new double[xData.length - period][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = xData[i + period].doubleValue();
            // holds the moving average sum
            double sum = 0.0;
            for (int j = 0; j < period; j++) {
                sum += yData[i + j].doubleValue();
            }
            sum = sum / period;
            result[i][1] = sum;
        }
        return result;

    }

}
