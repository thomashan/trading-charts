package io.github.thomashan.tradingchart.domain.ohlc

import io.github.thomashan.tradingchart.domain.AggregateRoot
import io.github.thomashan.tradingchart.domain.price.Price

import java.time.ZonedDateTime

abstract class Ohlc<P extends Price> implements AggregateRoot {
    ZonedDateTime dateTime
    P open
    P high
    P low
    P close
    double volume
}
