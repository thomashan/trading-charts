package io.github.thomashan.tradingchart.ui.data;

import io.github.thomashan.tradingchart.lang.DoubleParser;

public class MidData implements PriceData<MidData> {
    public double value;

    private MidData(double value) {
        this.value = value;
    }

    public static MidData of(double value) {
        return new MidData(value);
    }

    public static MidData empty() {
        return of(0);
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public double getHigh() {
        return value;
    }

    @Override
    public double getLow() {
        return value;
    }

    @Override
    public MidData setValue(double value) {
        this.value = value;
        return this;
    }

    @Override
    public MidData add(double value) {
        this.value += value;
        return this;
    }

    @Override
    public MidData minus(double value) {
        this.value -= value;
        return this;
    }

    @Override
    public MidData newInstance() {
        return of(value);
    }

    @Override
    public MidData copyFrom(MidData input) {
        this.value = input.value;
        return this;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MidData midData) {
            return this.value == midData.value;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public MidData populate(CharSequence charSequence) {
        this.value = DoubleParser.parseApprox(charSequence);
        return this;
    }
}
