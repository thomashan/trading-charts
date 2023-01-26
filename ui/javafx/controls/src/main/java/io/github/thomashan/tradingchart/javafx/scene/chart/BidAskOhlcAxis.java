package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.BidAskOhlcData;
import javafx.util.StringConverter;

public class BidAskOhlcAxis extends OhlcDataAxis<BidAskOhlcData> {
    private static final BidAskOhlcData ZERO_POSITION = BidAskOhlcData.emptyFull();
    private static final DefaultFormatter DEFAULT_FORMATTER = new DefaultFormatter();

    @Override
    protected BidAskOhlcData createMajor() {
        return BidAskOhlcData.emptyFull();
    }

    @Override
    protected BidAskOhlcData createCurrentValue() {
        return BidAskOhlcData.emptyFull();
    }

    @Override
    protected BidAskOhlcData getZeroPositionValue() {
        return ZERO_POSITION;
    }

    @Override
    protected StringConverter<BidAskOhlcData> createDefaultFormatter() {
        return DEFAULT_FORMATTER;
    }

    @Override
    protected BidAskOhlcData createTickValue() {
        return BidAskOhlcData.emptyFull();
    }

    @Override
    protected double calculateLabelSize() {
        return getTickLabelFont().getSize() * 2;
    }

    private static class DefaultFormatter extends StringConverter<BidAskOhlcData> {
        private final BidAskOhlcData bidAskOhlcData = BidAskOhlcData.emptyFull();

        @Override
        public String toString(BidAskOhlcData object) {
            return object.toString();
        }

        @Override
        public BidAskOhlcData fromString(String string) {
            return bidAskOhlcData.populate(string);
        }
    }
}
