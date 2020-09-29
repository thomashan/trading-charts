package io.github.thomashan.tradingchart.price

import io.github.thomashan.tradingchart.price.Price

class BidAsk implements Price {
    double ask
    double bid

    BidAsk(double bid, double ask) {
        this.bid = bid
        this.ask = ask
    }
}
