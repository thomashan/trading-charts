package io.github.thomashan.tradingchart.domain.ohlc;

import io.github.thomashan.tradingchart.domain.price.Mid;

import java.time.Instant;

public class MidOhlc extends Ohlc<Mid> {
    public MidOhlc() {
        // empty initaliser for byte watcher tests
    }

    private MidOhlc(Instant dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
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
        return of(Instant.EPOCH, Mid.empty(), Mid.empty(), Mid.empty(), Mid.empty(), 0);
    }

    public static MidOhlc of(Instant dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
        return new MidOhlc(dateTime, open, high, low, close, volume);
    }
}
