package io.github.thomashan.tradingchart.persistence.domain.ohlc;

import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.domain.price.Mid;
import io.github.thomashan.tradingchart.persistence.BytesIn;

public interface MidOhlcReader<BI extends BytesIn> extends OhlcReader<Mid, MidOhlc, BI> {
    MidOhlc object = MidOhlc.emptyFull();

    @Override
    default MidOhlc readObject() {
        object.dateTime = getBytesIn().readInstant();
        object.open.value = getBytesIn().readDouble();
        object.high.value = getBytesIn().readDouble();
        object.low.value = getBytesIn().readDouble();
        object.close.value = getBytesIn().readDouble();
        object.volume = getBytesIn().readDouble();

        return object;
    }
}
