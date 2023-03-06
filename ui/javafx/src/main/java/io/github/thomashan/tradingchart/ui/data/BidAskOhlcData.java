package io.github.thomashan.tradingchart.ui.data;

import static io.github.thomashan.tradingchart.ui.data.BidAskData.empty;
import static io.github.thomashan.tradingchart.ui.data.Granularity.D1;

public class BidAskOhlcData extends OhlcData<BidAskOhlcData, BidAskData> {
    private BidAskOhlcData() {
    }

    public static BidAskOhlcData of(BidAskData open, BidAskData high, BidAskData low, BidAskData close, double volume, Granularity granularity) {
        BidAskOhlcData bidAskOhlcData = new BidAskOhlcData();
        bidAskOhlcData.open = open;
        bidAskOhlcData.high = high;
        bidAskOhlcData.low = low;
        bidAskOhlcData.close = close;
        bidAskOhlcData.volume = volume;
        bidAskOhlcData.granularity = granularity;
        return bidAskOhlcData;
    }

    public static BidAskOhlcData emptyFull() {
        return of(empty(), empty(), empty(), empty(), 0, D1);
    }

    @Override
    public BidAskOhlcData newInstance() {
        return of(open, high, low, close, volume, granularity);
    }

    @Override
    public BidAskOhlcData copyFrom(BidAskOhlcData input) {
        copy(input);
        return this;
    }

    @Override
    public BidAskOhlcData add(double value) {
        addToCurrent(value);
        return this;
    }

    @Override
    public BidAskOhlcData minus(double value) {
        minusFromCurrent(value);
        return this;
    }

    @Override
    public BidAskOhlcData setValue(double value) {
        setFromValue(value);
        return this;
    }

    @Override
    public BidAskOhlcData populate(CharSequence charSequence) {
        populateFromString(charSequence);
        return this;
    }
}
