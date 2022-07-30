package io.github.thomashan.tradingchart.persistence.domain.ohlc;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.domain.price.BidAsk;
import io.github.thomashan.tradingchart.persistence.BytesIn;

public interface BidAskOhlcReader<BI extends BytesIn> extends OhlcReader<BidAsk, BidAskOhlc, BI> {
    BidAskOhlc object = BidAskOhlc.emptyFull();

    @Override
    default BidAskOhlc readObject() {
        object.dateTime = getBytesIn().readInstant();
        object.open.bid = getBytesIn().readDouble();
        object.open.ask = getBytesIn().readDouble();
        object.high.bid = getBytesIn().readDouble();
        object.high.ask = getBytesIn().readDouble();
        object.low.bid = getBytesIn().readDouble();
        object.low.ask = getBytesIn().readDouble();
        object.close.bid = getBytesIn().readDouble();
        object.close.ask = getBytesIn().readDouble();
        object.volume = getBytesIn().readDouble();

        return object;
    }
}
