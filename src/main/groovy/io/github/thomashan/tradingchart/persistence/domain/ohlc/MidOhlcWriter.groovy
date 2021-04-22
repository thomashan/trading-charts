package io.github.thomashan.tradingchart.persistence.domain.ohlc


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.domain.price.Mid
import io.github.thomashan.tradingchart.persistence.BytesOut

import java.util.function.Consumer

trait MidOhlcWriter<BO extends BytesOut> extends OhlcWriter<Mid, MidOhlc, BO> {
    private final Consumer<MidOhlc> midOhlcConsumer = (MidOhlc midOhlc) -> {
        bytesOut.writeInstant(midOhlc.dateTime)
                .writeDouble(midOhlc.open.value)
                .writeDouble(midOhlc.high.value)
                .writeDouble(midOhlc.low.value)
                .writeDouble(midOhlc.close.value)
                .writeDouble(midOhlc.volume)
    }

    @Override
    Consumer<MidOhlc> getObjectConsumer() {
        return midOhlcConsumer
    }
}
