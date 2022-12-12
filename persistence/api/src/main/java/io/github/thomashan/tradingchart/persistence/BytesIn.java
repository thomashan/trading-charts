package io.github.thomashan.tradingchart.persistence;

import io.github.thomashan.tradingchart.time.MutableInstant;

@SuppressWarnings("try")
public interface BytesIn extends AutoCloseable {
    MutableInstant readInstant();

    int readInt();

    long readLong();

    double readDouble();
}
