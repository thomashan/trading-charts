package io.github.thomashan.tradingchart.ohlc


import io.github.thomashan.tradingchart.price.Mid

import java.time.ZonedDateTime

class MidOhlc implements Ohlc<Mid> {
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
