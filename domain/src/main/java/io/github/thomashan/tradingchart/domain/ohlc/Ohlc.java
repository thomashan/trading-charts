package io.github.thomashan.tradingchart.domain.ohlc;

import io.github.thomashan.tradingchart.domain.AggregateRoot;
import io.github.thomashan.tradingchart.domain.Copyable;
import io.github.thomashan.tradingchart.domain.price.Price;

import java.time.Instant;
import java.util.Objects;

@SuppressWarnings({"missing-explicit-ctor", "exports"})
public abstract class Ohlc<O extends Ohlc<?, P>, P extends Price<P>> implements AggregateRoot, Copyable<O> {
    // FIXME: using Instant is not garbage free. Possible make MutableInstant which is garbage free
    public Instant dateTime;
    public P open;
    public P high;
    public P low;
    public P close;
    public double volume;

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, open, high, low, close, volume);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Ohlc)) {
            return false;
        }
        Ohlc<O, P> ohlc = (Ohlc<O, P>) obj;

        return Objects.deepEquals(dateTime, ohlc.dateTime) &&
                Objects.deepEquals(open, ohlc.open) &&
                Objects.deepEquals(high, ohlc.high) &&
                Objects.deepEquals(low, ohlc.low) &&
                Objects.deepEquals(close, ohlc.close) &&
                volume == ohlc.volume;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder
                .append(dateTime.toString() + ", ")
                .append(open.toString() + ", ")
                .append(high.toString() + ", ")
                .append(low.toString() + ", ")
                .append(close.toString() + ", ")
                .append(volume)
                .toString();
    }
}
