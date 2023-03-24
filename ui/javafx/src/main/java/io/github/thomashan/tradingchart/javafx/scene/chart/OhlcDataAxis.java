package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.lang.DoubleUtil;
import io.github.thomashan.tradingchart.ui.data.OhlcData;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;

import java.util.List;

import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.NUMBER_FORMAT_DEFAULT;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.NUMBER_FORMAT_HIGH_EXPONENT;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.NUMBER_FORMAT_LOW_EXPONENT;

public abstract class OhlcDataAxis<O extends OhlcData<O, ?>> extends DataAxis<O> {
    private final double[] rangeObject = new double[]{0, 0, 0, 0};
    private final Object[] autoRangeObject = new Object[]{rangeObject, null};

    protected OhlcDataAxis() {
        super();
    }

    @Override
    protected String getFormatterString(double tickUnit, double ratio) {
        int exp = (int) Math.floor(Math.log10(tickUnit));
        if (exp > 1) {
            return NUMBER_FORMAT_HIGH_EXPONENT;
        } else if (exp == 1) {
            return NUMBER_FORMAT_LOW_EXPONENT;
        }
        final boolean ratioHasFrac = Math.rint(ratio) != ratio;
        final StringBuilder formatterB = new StringBuilder(NUMBER_FORMAT_LOW_EXPONENT);
        int n = ratioHasFrac ? Math.abs(exp) + 1 : Math.abs(exp);
        if (n > 0) formatterB.append(".");
        for (int i = 0; i < n; ++i) {
            formatterB.append("0");
        }
        return formatterB.toString();
    }

    @Override
    protected Object autoRange(double minValue, double maxValue, double length, double labelSize) {
        Side side = getSide();
        // check if we need to force zero into range
        if (isForceZeroInRange()) {
            if (maxValue < 0) {
                maxValue = 0;
            } else if (minValue > 0) {
                minValue = 0;
            }
        }
        // calculate the number of tick-marks we can fit in the given length
        int numOfTickMarks = (int) Math.floor(length / labelSize);
        // can never have less than 2 tick marks one for each end
        numOfTickMarks = Math.max(numOfTickMarks, 2);
        int minorTickCount = Math.max(getMinorTickCount(), 1);

        double range = maxValue - minValue;

        if (range != 0 && range / (numOfTickMarks * minorTickCount) <= Math.ulp(minValue)) {
            range = 0;
        }
        // pad min and max by 2%, checking if the range is zero
        double paddedRange = (range == 0) ? minValue == 0 ? 2 : Math.abs(minValue) * 0.02 : Math.abs(range) * 1.02;
        double padding = (paddedRange - range) / 2;
        // if min and max are not zero then add padding to them
        double paddedMin = minValue - padding;
        double paddedMax = maxValue + padding;
        // check padding has not pushed min or max over zero line
        if ((paddedMin < 0 && minValue >= 0) || (paddedMin > 0 && minValue <= 0)) {
            // padding pushed min above or below zero so clamp to 0
            paddedMin = 0;
        }
        if ((paddedMax < 0 && maxValue >= 0) || (paddedMax > 0 && maxValue <= 0)) {
            // padding pushed min above or below zero so clamp to 0
            paddedMax = 0;
        }
        // calculate tick unit for the number of ticks can have in the given data range
        double tickUnit = paddedRange / (double) numOfTickMarks;
        // search for the best tick unit that fits
        double tickUnitRounded = 0;
        double minRounded = 0;
        double maxRounded = 0;
        int count = 0;
        double reqLength = Double.MAX_VALUE;
        String formatter = NUMBER_FORMAT_DEFAULT;
        // loop till we find a set of ticks that fit length and result in a total of less than 20 tick marks
        while (reqLength > length || count > 20) {
            int exp = (int) Math.floor(Math.log10(tickUnit));
            double mant = tickUnit / Math.pow(10, exp);
            double ratio = mant;
            if (mant > 5d) {
                exp++;
                ratio = 1;
            } else if (mant > 1d) {
                ratio = mant > 2.5 ? 5 : 2.5;
            }
            formatter = getFormatterString(tickUnit, ratio);
            tickUnitRounded = ratio * Math.pow(10, exp);
            // move min and max to nearest tick mark
            minRounded = Math.floor(paddedMin / tickUnitRounded) * tickUnitRounded;
            maxRounded = Math.ceil(paddedMax / tickUnitRounded) * tickUnitRounded;
            // calculate the required length to display the chosen tick marks for real, this will handle if there are
            // huge numbers involved etc or special formatting of the tick mark label text
            double maxReqTickGap = 0;
            double last = 0;
            count = (int) Math.ceil((maxRounded - minRounded) / tickUnitRounded);
            for (int i = 0; getMajor().getLow() <= maxRounded && i < count; getMajor().add(tickUnitRounded), i++) {
                Dimension2D markSize = measureTickMarkSize(getMajor(), getTickLabelRotation(), formatter);
                double size = side.isVertical() ? markSize.getHeight() : markSize.getWidth();
                if (i == 0) { // first
                    last = size / 2;
                } else {
                    maxReqTickGap = Math.max(maxReqTickGap, last + 6 + (size / 2));
                }
            }
            reqLength = (count - 1) * maxReqTickGap;
            tickUnit = tickUnitRounded;

            // fix for RT-35600 where a massive tick unit was being selected
            // unnecessarily. There is probably a better solution, but this works
            // well enough for now.
            if (numOfTickMarks == 2 && reqLength > length) {
                break;
            }
            if (reqLength > length || count > 20) {
                tickUnit *= 2; // This is just for the while loop, if there are still too many ticks
            }
        }
        // calculate new scale
        double newScale = calculateNewScale(length, minRounded, maxRounded);
        // return new range
        double[] rangeObject = (double[]) autoRangeObject[0];
        rangeObject[0] = paddedMin;
        rangeObject[1] = paddedMax;
        rangeObject[2] = tickUnit;
        rangeObject[3] = newScale;
        autoRangeObject[1] = formatter;
        return autoRangeObject;
    }

    @Override
    protected List<O> calculateTickValues(double length, Object range) {
        Object[] rangeProps = (Object[]) range;
        double[] rangeObject = (double[]) rangeProps[0];
        double lowerBound = rangeObject[0];
        double upperBound = rangeObject[1];
        double tickUnit = rangeObject[2];
        getTickValues().clear();
        if (lowerBound == upperBound) {
            getTickValues().add(getTickValue().setValue(lowerBound).newInstance());
        } else if (tickUnit <= 0) {
            getTickValues().add(getTickValue().setValue(lowerBound).newInstance());
            getTickValues().add(getTickValue().setValue(upperBound).newInstance());
        } else if (tickUnit > 0) {
            getTickValues().add(getTickValue().setValue(lowerBound).newInstance());
            if (((upperBound - lowerBound) / tickUnit) > 2000) {
                // This is a ridiculous amount of major tick marks, something has probably gone wrong
                System.err.println("Warning we tried to create more than 2000 major tick marks on a ObjectAxis. " + "Lower Bound=" + lowerBound + ", Upper Bound=" + upperBound + ", Tick Unit=" + tickUnit);
            } else if (lowerBound + tickUnit < upperBound) {
                // If tickUnit is integer, start with the nearest integer
                double majorInDoubleValue = Math.rint(tickUnit) == tickUnit ? Math.ceil(lowerBound) : lowerBound + tickUnit;
                O major = getTickValue().setValue(majorInDoubleValue);
                int count = (int) Math.ceil((upperBound - majorInDoubleValue) / tickUnit);
                for (int i = 0; majorInDoubleValue < upperBound && i < count; majorInDoubleValue = DoubleUtil.round(majorInDoubleValue + tickUnit, 15), i++) {
                    major.setValue(majorInDoubleValue);
                    if (!getTickValues().contains(major)) {
                        getTickValues().add(major.newInstance());
                    }
                }
            }
            getTickValues().add(getTickValue().setValue(upperBound).newInstance());
        }
        return getTickValues();
    }
}
