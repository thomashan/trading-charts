package io.github.thomashan.tradingchart.domain.ohlc;

import io.github.thomashan.tradingchart.domain.price.BidAsk;
import io.github.thomashan.tradingchart.time.MutableInstant;

public class BidAskOhlc extends Ohlc<BidAskOhlc, BidAsk> {
    public BidAskOhlc() {
        // empty initaliser for byte watcher tests
    }

    private BidAskOhlc(MutableInstant dateTime, BidAsk open, BidAsk high, BidAsk low, BidAsk close, double volume) {
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public static BidAskOhlc emptyMinimal() {
        return new BidAskOhlc();
    }

    public static BidAskOhlc emptyFull() {
        return of(MutableInstant.EPOCH, BidAsk.empty(), BidAsk.empty(), BidAsk.empty(), BidAsk.empty(), 0);
    }

    public static BidAskOhlc of(MutableInstant dateTime, BidAsk open, BidAsk high, BidAsk low, BidAsk close, double volume) {
        return new BidAskOhlc(dateTime, open, high, low, close, volume);
    }

    @Override
    public BidAskOhlc newInstance() {
        return of(dateTime, open.newInstance(), high.newInstance(), low.newInstance(), close.newInstance(), volume);
    }

    @Override
    public BidAskOhlc copyFrom(BidAskOhlc input) {
        copy(input);
        return this;
    }
}
