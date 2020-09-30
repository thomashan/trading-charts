package io.github.thomashan.tradingchart.price


class BidAsk implements Price {
    double ask
    double bid

    private BidAsk(double bid, double ask) {
        this.bid = bid
        this.ask = ask
    }

    static of(double bid, double ask) {
        return new BidAsk(bid, ask)
    }
}
