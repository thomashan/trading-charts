package io.github.thomashan.tradingchart.ui.data;

import io.github.thomashan.tradingchart.lang.DoubleParser;

import java.util.regex.Pattern;

public class BidAskData implements PriceData<BidAskData> {
    private static final String DELIMITER = "/";
    private static final Pattern PATTERN = Pattern.compile(DELIMITER);
    public double bid;
    public double ask;
    private double mid;

    private BidAskData(double bid, double ask) {
        this.bid = bid;
        this.ask = ask;
        this.mid = (bid + ask) / 2;
    }

    public static BidAskData of(double bid, double ask) {
        return new BidAskData(bid, ask);
    }

    public static BidAskData empty() {
        return of(0, 0);
    }

    @Override
    public double getValue() {
        return mid;
    }

    @Override
    public double getHigh() {
        return ask;
    }

    @Override
    public double getLow() {
        return bid;
    }

    @Override
    public BidAskData setValue(double value) {
        this.bid = value;
        this.ask = value;
        this.mid = value;
        return this;
    }

    @Override
    public BidAskData add(double value) {
        bid += value;
        ask += value;
        return this;
    }

    @Override
    public BidAskData minus(double value) {
        bid -= value;
        ask -= value;
        return this;
    }

    @Override
    public BidAskData newInstance() {
        return of(bid, ask);
    }

    @Override
    public BidAskData copyFrom(BidAskData input) {
        this.bid = input.bid;
        this.ask = input.ask;
        return this;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(bid) + 2 * Double.hashCode(ask);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BidAskData bidAskData) {
            return this.bid == bidAskData.bid &&
                    this.ask == bidAskData.ask;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilderFactory.INSTANCE.setLength(0);
        return StringBuilderFactory.INSTANCE
                .append(bid + "/" + ask)
                .toString();
    }

    @Override
    public BidAskData populate(CharSequence charSequence) {
        // FIXME: don't create String[] as it will produce garbage
        String[] split = PATTERN.split(charSequence);
        this.bid = DoubleParser.parseApprox(split[0]);
        this.ask = DoubleParser.parseApprox(split[1]);
        this.mid = (bid + ask) / 2;
        return this;
    }
}
