package io.github.thomashan.tradingchart.domain.price

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class BidAsk implements Price {
    double bid
    double ask

    private BidAsk(double bid, double ask) {
        this.bid = bid
        this.ask = ask
    }

    static BidAsk of(double bid, double ask) {
        return new BidAsk(bid, ask)
    }

    static BidAsk empty() {
        return of(0, 0)
    }

    @Override
    String toString() {
        return "${bid}/${ask}"
    }
}
