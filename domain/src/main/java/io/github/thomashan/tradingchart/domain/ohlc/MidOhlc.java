package io.github.thomashan.tradingchart.domain.ohlc;

import io.github.thomashan.tradingchart.domain.price.Mid;
import io.github.thomashan.tradingchart.time.MutableInstant;

public class MidOhlc extends Ohlc<MidOhlc, Mid> {
    public MidOhlc() {
        // empty initaliser for byte watcher tests
    }

    private MidOhlc(MutableInstant dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public static MidOhlc emptyMinimal() {
        return new MidOhlc();
    }

    public static MidOhlc emptyFull() {
        return of(MutableInstant.EPOCH, Mid.empty(), Mid.empty(), Mid.empty(), Mid.empty(), 0);
    }

    public static MidOhlc of(MutableInstant dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
        return new MidOhlc(dateTime, open, high, low, close, volume);
    }

    @Override
    public MidOhlc newInstance() {
        return of(dateTime, open.newInstance(), high.newInstance(), low.newInstance(), close.newInstance(), volume);
    }

    @Override
    public MidOhlc copyFrom(MidOhlc input) {
        copy(input);
        return this;
    }
}
