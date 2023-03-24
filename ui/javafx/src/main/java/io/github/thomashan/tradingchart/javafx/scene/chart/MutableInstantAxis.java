package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.lang.DoubleUtil;
import io.github.thomashan.tradingchart.time.MutableInstant;
import io.github.thomashan.tradingchart.ui.data.Granularity;
import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.thomashan.tradingchart.javafx.scene.chart.MutableInstantFormatter.YEAR_MONTH_DAY;
import static io.github.thomashan.tradingchart.javafx.scene.chart.MutableInstantFormatter.ZONE_ID_UTC;
import static io.github.thomashan.tradingchart.time.format.DateTimeFormatters.DATE_TIME_FORMATTER;
import static io.github.thomashan.tradingchart.ui.data.Granularity.D1;

public class MutableInstantAxis extends DataAxis<MutableInstantData> {
    private static final MutableInstantData ZERO_POSITION = MutableInstantData.emptyFull();
    private static final DefaultFormatter DEFAULT_FORMATTER = new DefaultFormatter();
    private final Map<Font, Map<Granularity, Double>> tickLabelSizes = new HashMap<>();
    private final Text text = new Text();
    private final double[] rangeObject = new double[]{0, 0, 0, 0, 0, 0};
    private final Object[] autoRangeObject = new Object[]{rangeObject, null};
    private final Function<Font, Map<Granularity, Double>> labelSizeCreator = font -> {
        text.setFont(font);
        return MutableInstantFormatter.TICK_FORMATTER_STRINGS.entrySet().stream()
                .map(entry -> {
                    text.setText(entry.getValue());
                    return Map.entry(entry.getKey(), text.prefWidth(-1));
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    };

    private Granularity granularity = D1;
    private double granularityWidth = calculateGranularityWidth(granularity);

    public MutableInstantAxis() {
        super();
        text.setWrappingWidth(0);
        text.setLineSpacing(0);
    }

    @Override
    protected MutableInstantData createMajor() {
        return MutableInstantData.emptyFull();
    }

    @Override
    protected MutableInstantData createCurrentValue() {
        return MutableInstantData.emptyFull();
    }

    @Override
    protected MutableInstantData getZeroPositionValue() {
        return ZERO_POSITION;
    }

    @Override
    protected StringConverter<MutableInstantData> createDefaultFormatter() {
        return DEFAULT_FORMATTER;
    }

    @Override
    protected String getFormatterString(double tickUnit, double ratio) {
        return MutableInstantFormatter.TICK_FORMATTER_STRINGS.get(granularity);
    }

    @Override
    protected MutableInstantData createTickValue() {
        return MutableInstantData.emptyFull();
    }

    @Override
    protected double calculateLabelSize() {
        return tickLabelSizes.computeIfAbsent(getTickLabelFont(), labelSizeCreator).get(granularity);
    }

    @Override
    protected Object autoRange(double minValue, double maxValue, double length, double labelSize) {
        double tickUnit = getGranularityValue();
        double padding = tickUnit / 2;
        // if min and max are not zero then add padding to them
        double paddedMin = minValue - padding;
        double paddedMax = maxValue + padding;
        // calculate tick unit for the number of ticks can have in the given data range
        // search for the best tick unit that fits
        String formatter = getFormatterString(0, 0);
        // calculate new scale
        double newScale = calculateNewScale(length, paddedMin, paddedMax);
        // return new range
        double[] rangeObject = (double[]) autoRangeObject[0];
        rangeObject[0] = paddedMin;
        rangeObject[1] = paddedMax;
        rangeObject[2] = tickUnit;
        rangeObject[3] = newScale;
        rangeObject[4] = minValue;
        rangeObject[5] = maxValue;
        autoRangeObject[1] = formatter;
        return autoRangeObject;
    }

    @Override
    protected List<MutableInstantData> calculateTickValues(double length, Object range) {
        Object[] rangeProps = (Object[]) range;
        double[] rangeObject = (double[]) rangeProps[0];
        double lowerBound = rangeObject[4];
        double upperBound = rangeObject[5];
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
                MutableInstantData major = getTickValue().setValue(majorInDoubleValue);
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

    double getGranularityValue() {
        return GranularityToMutableInstant.getMutableInstant(granularity).getValue();
    }

    public Granularity getGranularity() {
        return granularity;
    }

    public void setGranularity(Granularity granularity) {
        this.granularity = granularity;
        this.granularityWidth = calculateGranularityWidth(granularity);
    }

    public double getGranularityWidth() {
        return granularityWidth;
    }

    public void setGranularityWidth() {
        this.granularityWidth = calculateGranularityWidth(granularity);
    }

    public double calculateGranularityWidth(Granularity granularity) {
        MutableInstantData mutableInstant = GranularityToMutableInstant.getMutableInstant(granularity);
        return getDisplayPosition(mutableInstant);
    }

    static class DefaultFormatter extends StringConverter<MutableInstantData> {
        private static final Map<String, DateTimeFormatter> FORMATTERS = new HashMap<>();
        private static final Function<String, DateTimeFormatter> FORMATTER_CREATOR = pattern -> DateTimeFormatter.ofPattern(pattern);
        private final MutableInstantData mutableInstant = MutableInstantData.of(MutableInstant.EPOCH.newInstance());

        protected DefaultFormatter() {
            super();
        }

        public String toString(MutableInstantData object, String formatter, ZoneId zoneId) {
            return FORMATTERS.computeIfAbsent(formatter, FORMATTER_CREATOR)
                    .withZone(zoneId)
                    .format(object.getMutableInstant());
        }

        @Override
        public String toString(MutableInstantData object) {
            return toString(object, YEAR_MONTH_DAY, ZONE_ID_UTC);
        }

        @Override
        public MutableInstantData fromString(String string) {
            MutableInstant parsed = DATE_TIME_FORMATTER.parse(string, MutableInstant::from);
            return mutableInstant.copyFrom(parsed);
        }
    }
}
