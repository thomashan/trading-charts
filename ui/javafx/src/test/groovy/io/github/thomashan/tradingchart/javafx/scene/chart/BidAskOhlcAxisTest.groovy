package io.github.thomashan.tradingchart.javafx.scene.chart

import io.github.thomashan.tradingchart.ui.data.BidAskData
import io.github.thomashan.tradingchart.ui.data.BidAskOhlcData
import io.github.thomashan.tradingchart.ui.data.Granularity

class BidAskOhlcAxisTest extends OhlcDataAxisTestCase<BidAskOhlcData, BidAskData> {
    @Override
    protected OhlcDataAxis<BidAskOhlcData> createOhlcDataAxis() {
        return new BidAskOhlcAxis()
    }

    @Override
    protected BidAskOhlcData createOhlcData(double open, double high, double low, double close) {
        return BidAskOhlcData.of(
            BidAskData.of(open, open),
            BidAskData.of(high, high),
            BidAskData.of(low, low),
            BidAskData.of(close, close),
            0,
            Granularity.D1
        )
    }
}
