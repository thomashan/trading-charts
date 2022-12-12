package io.github.thomashan.tradingchart.persistence;

import io.github.thomashan.tradingchart.time.MutableInstant;

public interface BytesOut<BO extends BytesOut<?>> extends AutoCloseable {
    BO writeInstant(MutableInstant value);

    BO writeInt(int value);

    BO writeLong(long value);

    BO writeDouble(double value);

    @Override
    void close();
}
