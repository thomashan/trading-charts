package io.github.thomashan.tradingchart.ohlc

import io.github.thomashan.tradingchart.price.Price

import java.time.ZonedDateTime

class Ohlc<P extends Price> {
    ZonedDateTime dateTime
    P open
    P high
    P low
    P close
    double volume

    static <P extends Price> Ohlc<P> of(ZonedDateTime dateTime, P open, P high, P low, P close, double volume) {
        Ohlc<P> ohlc = new Ohlc<P>()
        ohlc.dateTime = dateTime
        ohlc.open = open
        ohlc.high = high
        ohlc.low = low
        ohlc.close = close
        ohlc.volume = volume

        return ohlc
    }
}
