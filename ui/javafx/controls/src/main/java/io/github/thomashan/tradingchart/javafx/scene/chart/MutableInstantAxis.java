package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.time.MutableInstant;
import io.github.thomashan.tradingchart.ui.data.Granularity;
import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

    public static class DefaultFormatter extends StringConverter<MutableInstantData> {
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
