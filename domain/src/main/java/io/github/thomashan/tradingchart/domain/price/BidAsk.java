package io.github.thomashan.tradingchart.domain.price;

import java.util.Objects;

public class BidAsk implements Price<BidAsk> {
    public double bid;
    public double ask;

    private BidAsk(double bid, double ask) {
        this.bid = bid;
        this.ask = ask;
    }

    public static BidAsk of(double bid, double ask) {
        return new BidAsk(bid, ask);
    }

    public static BidAsk empty() {
        return of(0, 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bid, ask);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BidAsk)) {
            return false;
        }
        BidAsk bidAsk = (BidAsk) obj;

        return bid == bidAsk.bid && ask == bidAsk.ask;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder
                .append(bid + "/" + ask)
                .toString();
    }

    @Override
    public BidAsk copy() {
        return of(bid, ask);
    }
}
