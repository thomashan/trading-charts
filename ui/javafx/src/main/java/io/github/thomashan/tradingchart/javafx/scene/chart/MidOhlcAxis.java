package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.MidOhlcData;
import javafx.util.StringConverter;

public class MidOhlcAxis extends OhlcDataAxis<MidOhlcData> {
    private static final MidOhlcData ZERO_POSITION = MidOhlcData.emptyFull();
    private static final DefaultFormatter DEFAULT_FORMATTER = new DefaultFormatter();

    public MidOhlcAxis() {
        super();
    }

    @Override
    protected MidOhlcData createMajor() {
        return MidOhlcData.emptyFull();
    }

    @Override
    protected MidOhlcData createCurrentValue() {
        return MidOhlcData.emptyFull();
    }

    @Override
    protected StringConverter<MidOhlcData> createDefaultFormatter() {
        return DEFAULT_FORMATTER;
    }

    @Override
    protected MidOhlcData createTickValue() {
        return MidOhlcData.emptyFull();
    }

    @Override
    protected MidOhlcData getZeroPositionValue() {
        return ZERO_POSITION;
    }

    @Override
    protected double calculateLabelSize() {
        return getTickLabelFont().getSize() * 2;
    }

    private static class DefaultFormatter extends StringConverter<MidOhlcData> {
        private final MidOhlcData midOhlcData = MidOhlcData.emptyFull();

        @Override
        public String toString(MidOhlcData object) {
            return object.toString();
        }

        @Override
        public MidOhlcData fromString(String string) {
            return midOhlcData.populate(string);
        }
    }
}
