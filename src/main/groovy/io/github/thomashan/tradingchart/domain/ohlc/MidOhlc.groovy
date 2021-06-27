package io.github.thomashan.tradingchart.domain.ohlc

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import io.github.thomashan.tradingchart.domain.price.Mid

import java.time.Instant

@EqualsAndHashCode
final class MidOhlc extends Ohlc<Mid> {
    @PackageScope
    MidOhlc() {
        // empty initaliser for byte watcher tests
    }

    private MidOhlc(Instant dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
        this.dateTime = dateTime
        this.open = open
        this.high = high
        this.low = low
        this.close = close
        this.volume = volume
    }

    static MidOhlc emptyMinimal() {
        return new MidOhlc()
    }

    static MidOhlc emptyFull() {
        return of(Instant.EPOCH, Mid.empty(), Mid.empty(), Mid.empty(), Mid.empty(), 0)
    }

    static MidOhlc of(Instant dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
        return new MidOhlc(dateTime, open, high, low, close, volume)
    }
}
