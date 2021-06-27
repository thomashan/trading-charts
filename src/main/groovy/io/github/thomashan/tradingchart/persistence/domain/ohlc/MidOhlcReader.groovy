package io.github.thomashan.tradingchart.persistence.domain.ohlc


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.domain.price.Mid
import io.github.thomashan.tradingchart.persistence.BytesIn

trait MidOhlcReader<BI extends BytesIn> extends OhlcReader<Mid, MidOhlc, BI> {
    final MidOhlc object = MidOhlc.emptyFull()

    @Override
    MidOhlc readObject() {
        object.dateTime = bytesIn.readInstant()
        object.open.value = bytesIn.readDouble()
        object.high.value = bytesIn.readDouble()
        object.low.value = bytesIn.readDouble()
        object.close.value = bytesIn.readDouble()
        object.volume = bytesIn.readDouble()

        return object
    }
}
