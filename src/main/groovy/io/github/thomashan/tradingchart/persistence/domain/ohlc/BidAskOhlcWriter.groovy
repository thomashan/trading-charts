package io.github.thomashan.tradingchart.persistence.domain.ohlc

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.price.BidAsk
import io.github.thomashan.tradingchart.persistence.BytesOut

import java.util.function.Consumer

trait BidAskOhlcWriter<BO extends BytesOut> extends OhlcWriter<BidAsk, BidAskOhlc, BO> {
    private final Consumer<BidAskOhlc> bidAskOhlcConsumer = (BidAskOhlc bidAskOhlc) -> {
        bytesOut.writeInstant(bidAskOhlc.dateTime)
                .writeDouble(bidAskOhlc.open.bid)
                .writeDouble(bidAskOhlc.open.ask)
                .writeDouble(bidAskOhlc.high.bid)
                .writeDouble(bidAskOhlc.high.ask)
                .writeDouble(bidAskOhlc.low.bid)
                .writeDouble(bidAskOhlc.low.ask)
                .writeDouble(bidAskOhlc.close.bid)
                .writeDouble(bidAskOhlc.close.ask)
                .writeDouble(bidAskOhlc.volume)
    }

    @Override
    Consumer<BidAskOhlc> getObjectConsumer() {
        return bidAskOhlcConsumer
    }
}
