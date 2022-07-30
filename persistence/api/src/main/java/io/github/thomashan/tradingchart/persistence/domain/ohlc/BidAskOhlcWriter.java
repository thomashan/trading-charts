package io.github.thomashan.tradingchart.persistence.domain.ohlc;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.domain.price.BidAsk;
import io.github.thomashan.tradingchart.persistence.BytesOut;

import java.util.function.Consumer;

public interface BidAskOhlcWriter<BO extends BytesOut<?>> extends OhlcWriter<BidAsk, BidAskOhlc, BO> {
    @Override
    default Consumer<BidAskOhlc> getObjectConsumer() {
        return bidAskOhlc -> getBytesOut()
                .writeInstant(bidAskOhlc.dateTime)
                .writeDouble(bidAskOhlc.open.bid)
                .writeDouble(bidAskOhlc.open.ask)
                .writeDouble(bidAskOhlc.high.bid)
                .writeDouble(bidAskOhlc.high.ask)
                .writeDouble(bidAskOhlc.low.bid)
                .writeDouble(bidAskOhlc.low.ask)
                .writeDouble(bidAskOhlc.close.bid)
                .writeDouble(bidAskOhlc.close.ask)
                .writeDouble(bidAskOhlc.volume);
    }

    @Override
    void close();
}
