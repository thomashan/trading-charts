package io.github.thomashan.tradingchart.domain.ohlc

import groovy.transform.PackageScope
import io.github.thomashan.tradingchart.domain.price.Mid

import java.time.ZonedDateTime

final class MidOhlc extends Ohlc<Mid> {
    @PackageScope
    MidOhlc() {
        // empty initaliser for byte watcher tests
    }

    private MidOhlc(ZonedDateTime dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
        this.dateTime = dateTime
        this.open = open
        this.high = high
        this.low = low
        this.close = close
        this.volume = volume
    }

    static MidOhlc of(ZonedDateTime dateTime, Mid open, Mid high, Mid low, Mid close, double volume) {
        return new MidOhlc(dateTime, open, high, low, close, volume)
    }
}
