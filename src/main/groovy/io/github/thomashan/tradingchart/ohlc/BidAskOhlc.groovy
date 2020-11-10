package io.github.thomashan.tradingchart.ohlc

import io.github.thomashan.tradingchart.price.BidAsk

import java.time.ZonedDateTime

class BidAskOhlc implements Ohlc<BidAsk> {
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
