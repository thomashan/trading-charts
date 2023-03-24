package io.github.thomashan.tradingchart.ui.data;

import io.github.thomashan.tradingchart.Copyable;
import io.github.thomashan.tradingchart.time.MutableInstant;
import io.github.thomashan.tradingchart.time.format.DateTimeFormatters;

public class MutableInstantData implements Copyable<MutableInstantData>, AxisData<MutableInstantData> {
    private static final long NANOS_PER_SECOND = 1_000_000_000;
    private static final double NANOS_PER_SECOND_DOUBLE = (double) NANOS_PER_SECOND;
    private MutableInstant mutableInstant;
    private double value;

    private MutableInstantData(MutableInstant mutableInstant) {
        setMutableInstant(mutableInstant.newInstance());
    }

    public static MutableInstantData emptyFull() {
        return of(MutableInstant.EPOCH.newInstance());
    }

    public static MutableInstantData of(MutableInstant mutableInstant) {
        return new MutableInstantData(mutableInstant);
    }

    @Override
    public MutableInstantData newInstance() {
        return of(mutableInstant.newInstance());
    }

    public MutableInstant getMutableInstant() {
        return mutableInstant;
    }

    private void setMutableInstant(MutableInstant mutableInstant) {
        this.mutableInstant = mutableInstant;
        this.value = mutableInstant.getEpochSecond() + (mutableInstant.getNano() / NANOS_PER_SECOND_DOUBLE);
    }

    @Override
    public MutableInstantData copyFrom(MutableInstantData input) {
        setMutableInstant(mutableInstant.copyFrom(input.mutableInstant));
        return this;
    }

    public MutableInstantData copyFrom(MutableInstant input) {
        setMutableInstant(input.copyFrom(input));
        return this;
    }

    @Override
    public MutableInstantData add(double value) {
        long seconds = (long) value;
        long nanos = (long) (value - seconds) * NANOS_PER_SECOND;
        this.mutableInstant.plusSeconds(seconds);
        this.mutableInstant.plusNanos(nanos);
        this.value += value;
        return this;
    }

    @Override
    public MutableInstantData minus(double value) {
        long seconds = (long) value;
        long nanos = (long) (value - seconds) * NANOS_PER_SECOND;
        this.mutableInstant.plusSeconds(-seconds);
        this.mutableInstant.plusNanos(-nanos);
        this.value -= value;
        return this;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public double getHigh() {
        return getValue();
    }

    @Override
    public double getLow() {
        return getValue();
    }

    @Override
    public MutableInstantData setValue(double value) {
        long seconds = (long) value;
        int nanos = (int) ((value - seconds) * 1_000_000_000);
        this.mutableInstant.ofEpochSecondMutable(seconds, nanos);
        this.value = value;
        return this;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MutableInstantData mutableInstantData) {
            return this.value == mutableInstantData.value;
        }

        return false;
    }

    @Override
    public String toString() {
        return mutableInstant.toString();
    }

    @Override
    public MutableInstantData populate(CharSequence charSequence) {
        MutableInstant parsed = DateTimeFormatters.DATE_TIME_FORMATTER.parse(charSequence, MutableInstant::from);
        setMutableInstant(mutableInstant.copyFrom(parsed));
        return this;
    }
}
