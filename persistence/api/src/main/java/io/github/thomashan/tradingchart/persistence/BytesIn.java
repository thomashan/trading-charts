package io.github.thomashan.tradingchart.persistence;

import java.time.Instant;

@SuppressWarnings("try")
public interface BytesIn extends AutoCloseable {
    Instant readInstant();

    int readInt();

    long readLong();

    double readDouble();
}
