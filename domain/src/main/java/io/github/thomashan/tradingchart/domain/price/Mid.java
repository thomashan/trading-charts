package io.github.thomashan.tradingchart.domain.price;

public class Mid implements Price<Mid> {
    public double value;

    private Mid(double value) {
        this.value = value;
    }

    public static Mid of(double value) {
        return new Mid(value);
    }

    public static Mid empty() {
        return of(0);
    }

    @Override
    public int hashCode() {
        return (int) value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Mid)) {
            return false;
        }
        Mid mid = (Mid) obj;

        return value == mid.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Mid copy() {
        return of(value);
    }
}
