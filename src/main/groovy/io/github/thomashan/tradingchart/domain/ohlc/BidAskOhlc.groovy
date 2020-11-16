package io.github.thomashan.tradingchart.domain.ohlc

import groovy.transform.PackageScope
import io.github.thomashan.tradingchart.domain.price.BidAsk

import java.time.ZonedDateTime

final class BidAskOhlc extends Ohlc<BidAsk> {
    @PackageScope
    BidAskOhlc() {
        // empty initaliser for byte watcher tests
    }

    private BidAskOhlc(ZonedDateTime dateTime, BidAsk open, BidAsk high, BidAsk low, BidAsk close, double volume) {
        this.dateTime = dateTime
        this.open = open
        this.high = high
        this.low = low
        this.close = close
        this.volume = volume
    }

    static BidAskOhlc of(ZonedDateTime dateTime, BidAsk open, BidAsk high, BidAsk low, BidAsk close, double volume) {
        return new BidAskOhlc(dateTime, open, high, low, close, volume)
    }
}
