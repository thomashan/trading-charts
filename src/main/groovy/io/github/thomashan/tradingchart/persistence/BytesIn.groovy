package io.github.thomashan.tradingchart.persistence

import java.time.Instant

interface BytesIn extends AutoCloseable {
    Instant readInstant()

    int readInt()

    long readLong()

    double readDouble()
}
