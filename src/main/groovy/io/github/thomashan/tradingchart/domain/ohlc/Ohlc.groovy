package io.github.thomashan.tradingchart.domain.ohlc

import io.github.thomashan.tradingchart.domain.AggregateRoot
import io.github.thomashan.tradingchart.domain.price.Price

import java.time.Instant

// must be defined as abstract class and not trait some fields won't be visible in the java world (e.g. java byte watcher)
abstract class Ohlc<P extends Price> implements AggregateRoot {
    Instant dateTime
    P open
    P high
    P low
    P close
    double volume

    @Override
    String toString() {
        return "${dateTime}, ${open}, ${high}, ${low}, ${close}, ${volume}"
    }
}
