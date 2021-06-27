package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.persistence.BytesIn
import net.openhft.chronicle.queue.ExcerptTailer

import java.time.Instant

class BytesInChronicle implements BytesIn {
    private final ExcerptTailer excerptTailer

    BytesInChronicle(ExcerptTailer excerptTailer) {
        this.excerptTailer = excerptTailer
    }

    // FIXME: this is going to potentially leave a lot of created objects therefore garbage lying around,
    // FIXME: should we just convert everything to epoch?
    @Override
    Instant readInstant() {
        Instant instant = null
        excerptTailer.readBytes(bytesIn -> {
            long epochSeconds = bytesIn.readLong()
            int nano = bytesIn.readInt()
            instant = Instant.ofEpochSecond(epochSeconds, nano)
        })
        return instant
    }

    @Override
    int readInt() {
        int value
        excerptTailer.readBytes(bytesIn -> {
            value = bytesIn.readInt()
        })
        return value
    }

    @Override
    long readLong() {
        long value
        excerptTailer.readBytes(bytesIn -> {
            value = bytesIn.readLong()
        })
        return value
    }

    @Override
    double readDouble() {
        double value
        excerptTailer.readBytes(bytesIn -> {
            value = bytesIn.readDouble()
        })
        return value
    }

    @Override
    void close() throws Exception {
        excerptTailer.close()
    }
}
