package io.github.thomashan.tradingchart.persistence;

import java.time.Instant;

public interface BytesOut<BO extends BytesOut<?>> extends AutoCloseable {
    BO writeInstant(Instant value);

    BO writeInt(int value);

    BO writeLong(long value);

    BO writeDouble(double value);

    @Override
    void close();
}
