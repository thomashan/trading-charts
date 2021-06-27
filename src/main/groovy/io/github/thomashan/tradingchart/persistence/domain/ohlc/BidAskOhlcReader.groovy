package io.github.thomashan.tradingchart.persistence.domain.ohlc

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.price.BidAsk
import io.github.thomashan.tradingchart.persistence.BytesIn

trait BidAskOhlcReader<BI extends BytesIn> extends OhlcReader<BidAsk, BidAskOhlc, BI> {
    final BidAskOhlc object = BidAskOhlc.emptyFull()

    @Override
    BidAskOhlc readObject() {
        object.dateTime = bytesIn.readInstant()
        object.open.bid = bytesIn.readDouble()
        object.open.ask = bytesIn.readDouble()
        object.high.bid = bytesIn.readDouble()
        object.high.ask = bytesIn.readDouble()
        object.low.bid = bytesIn.readDouble()
        object.low.ask = bytesIn.readDouble()
        object.close.bid = bytesIn.readDouble()
        object.close.ask = bytesIn.readDouble()
        object.volume = bytesIn.readDouble()

        return object
    }
}
