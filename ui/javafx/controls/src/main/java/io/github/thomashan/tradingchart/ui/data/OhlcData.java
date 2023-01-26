package io.github.thomashan.tradingchart.ui.data;

import io.github.thomashan.tradingchart.lang.DoubleParser;

import java.util.regex.Pattern;

public abstract class OhlcData<O extends OhlcData<O, P>, P extends PriceData<P>> implements AxisData<O> {
    private static final String DELIMITER = ";";
    private static final Pattern PATTERN = Pattern.compile(DELIMITER);
    public P open;
    public P high;
    public P low;
    public P close;
    public double volume;
    // FIXME: do we really need the granularity in the ohlcData? It can be embedded in the axis
    public Granularity granularity;

    @Override
    public String toString() {
        if (open.equals(high) && open.equals(low) && open.equals(close)) {
            return Double.toString(open.getValue());
        }
        StringBuilderFactory.INSTANCE.setLength(0);
        return StringBuilderFactory.INSTANCE
                .append(granularity + DELIMITER)
                .append(open + DELIMITER)
                .append(high + DELIMITER)
                .append(low + DELIMITER)
                .append(close + DELIMITER)
                .append(volume)
                .toString();
    }

    protected void copy(O input) {
        open.copyFrom(input.open);
        high.copyFrom(input.high);
        low.copyFrom(input.low);
        close.copyFrom(input.close);
        volume = input.volume;
    }

    protected void addToCurrent(double value) {
        open.add(value);
        high.add(value);
        low.add(value);
        close.add(value);
    }

    protected void minusFromCurrent(double value) {
        open.minus(value);
        high.minus(value);
        low.minus(value);
        close.minus(value);
    }

    @Override
    public double getValue() {
        return open.getValue();
    }

    @Override
    public double getHigh() {
        return high.getValue();
    }

    @Override
    public double getLow() {
        return low.getValue();
    }

    protected void setFromValue(double value) {
        open.setValue(value);
        high.setValue(value);
        low.setValue(value);
        close.setValue(value);
    }

    protected void populateFromString(CharSequence charSequence) {
        String[] split = PATTERN.split(charSequence);
        this.granularity = Granularity.valueOf(split[0]);
        this.open = open.populate(split[1]);
        this.high = high.populate(split[2]);
        this.low = low.populate(split[3]);
        this.close = close.populate(split[4]);
        this.volume = DoubleParser.parseApprox(split[5]);
    }
}
