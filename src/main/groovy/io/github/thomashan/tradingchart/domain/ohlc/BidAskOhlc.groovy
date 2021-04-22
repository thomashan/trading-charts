package io.github.thomashan.tradingchart.domain.ohlc

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import io.github.thomashan.tradingchart.domain.price.BidAsk

import java.time.Instant

@EqualsAndHashCode
final class BidAskOhlc extends Ohlc<BidAsk> {
    @PackageScope
    BidAskOhlc() {
        // empty initaliser for byte watcher tests
    }

    private BidAskOhlc(Instant dateTime, BidAsk open, BidAsk high, BidAsk low, BidAsk close, double volume) {
        this.dateTime = dateTime
        this.open = open
        this.high = high
        this.low = low
        this.close = close
        this.volume = volume
    }

    static BidAskOhlc emptyMinimal() {
        return new BidAskOhlc()
    }

    static BidAskOhlc emptyFull() {
        return of(Instant.EPOCH, BidAsk.empty(), BidAsk.empty(), BidAsk.empty(), BidAsk.empty(), 0)
    }

    static BidAskOhlc of(Instant dateTime, BidAsk open, BidAsk high, BidAsk low, BidAsk close, double volume) {
        return new BidAskOhlc(dateTime, open, high, low, close, volume)
    }
}
