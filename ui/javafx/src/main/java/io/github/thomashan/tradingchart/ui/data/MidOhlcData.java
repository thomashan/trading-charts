package io.github.thomashan.tradingchart.ui.data;

import static io.github.thomashan.tradingchart.ui.data.Granularity.D1;
import static io.github.thomashan.tradingchart.ui.data.MidData.empty;

public class MidOhlcData extends OhlcData<MidOhlcData, MidData> {
    private MidOhlcData() {
    }

    public static MidOhlcData of(double open,
                                 double high,
                                 double low,
                                 double close,
                                 double volume,
                                 Granularity granularity) {
        MidOhlcData midOhlcData = new MidOhlcData();
        midOhlcData.open = MidData.of(open);
        midOhlcData.high = MidData.of(high);
        midOhlcData.low = MidData.of(low);
        midOhlcData.close = MidData.of(close);
        midOhlcData.volume = volume;
        midOhlcData.granularity = granularity;
        return midOhlcData;
    }

    public static MidOhlcData of(MidData open,
                                 MidData high,
                                 MidData low,
                                 MidData close,
                                 double volume,
                                 Granularity granularity) {
        MidOhlcData midOhlcData = new MidOhlcData();
        midOhlcData.open = open;
        midOhlcData.high = high;
        midOhlcData.low = low;
        midOhlcData.close = close;
        midOhlcData.volume = volume;
        midOhlcData.granularity = granularity;
        return midOhlcData;
    }

    public static MidOhlcData emptyFull() {
        return of(empty(), empty(), empty(), empty(), 0, D1);
    }

    @Override
    public MidOhlcData newInstance() {
        return of(open, high, low, close, volume, granularity);
    }

    @Override
    public MidOhlcData copyFrom(MidOhlcData input) {
        copy(input);
        return this;
    }

    @Override
    public MidOhlcData add(double value) {
        addToCurrent(value);
        return this;
    }

    @Override
    public MidOhlcData minus(double value) {
        minusFromCurrent(value);
        return this;
    }

    @Override
    public MidOhlcData setValue(double value) {
        setFromValue(value);
        return this;
    }

    @Override
    public MidOhlcData populate(CharSequence charSequence) {
        populateFromString(charSequence);
        return this;
    }
}
