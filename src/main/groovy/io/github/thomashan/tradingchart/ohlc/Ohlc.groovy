package io.github.thomashan.tradingchart.ohlc

import io.github.thomashan.tradingchart.price.Price

import java.time.ZonedDateTime

trait Ohlc<P extends Price> {
    ZonedDateTime dateTime
    P open
    P high
    P low
    P close
    double volume
}
