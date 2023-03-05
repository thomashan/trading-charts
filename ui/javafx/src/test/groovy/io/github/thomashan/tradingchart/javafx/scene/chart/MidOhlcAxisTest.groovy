package io.github.thomashan.tradingchart.javafx.scene.chart

import io.github.thomashan.tradingchart.ui.data.Granularity
import io.github.thomashan.tradingchart.ui.data.MidData
import io.github.thomashan.tradingchart.ui.data.MidOhlcData

class MidOhlcAxisTest extends OhlcDataAxisTestCase<MidOhlcData, MidData> {
    @Override
    protected OhlcDataAxis<MidOhlcData> createOhlcDataAxis() {
        return new MidOhlcAxis()
    }

    @Override
    protected MidOhlcData createOhlcData(double open, double high, double low, double close) {
        return MidOhlcData.of(
            MidData.of(open),
            MidData.of(high),
            MidData.of(low),
            MidData.of(close),
            0,
            Granularity.D1
        )
    }
}
