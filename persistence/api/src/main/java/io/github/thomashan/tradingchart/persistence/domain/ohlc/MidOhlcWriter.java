package io.github.thomashan.tradingchart.persistence.domain.ohlc;

import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.domain.price.Mid;
import io.github.thomashan.tradingchart.persistence.BytesOut;

import java.util.function.Consumer;

public interface MidOhlcWriter<BO extends BytesOut<BO>> extends OhlcWriter<Mid, MidOhlc, BO> {
    @Override
    default Consumer<MidOhlc> getObjectConsumer() {
        return (MidOhlc midOhlc) -> getBytesOut()
                .writeInstant(midOhlc.dateTime)
                .writeDouble(midOhlc.open.value)
                .writeDouble(midOhlc.high.value)
                .writeDouble(midOhlc.low.value)
                .writeDouble(midOhlc.close.value)
                .writeDouble(midOhlc.volume);
    }
}
