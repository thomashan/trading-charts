package io.github.thomashan.tradingchart.persistence.impl.size

import io.github.thomashan.tradingchart.persistence.BytesOut

import java.time.Instant

@Singleton
class BytesOutSize implements BytesOut<BytesOutSize> {
    private int size = 0

    @Override
    BytesOutSize writeInstant(Instant value) {
        writeLong(0).writeInt(0)
        return this
    }

    @Override
    BytesOutSize writeInt(int value) {
        size += 4
        return this
    }

    @Override
    BytesOutSize writeLong(long value) {
        size += 8
        return this
    }

    @Override
    BytesOutSize writeDouble(double value) {
        size += 8
        return this
    }

    @Override
    void close() throws Exception {
        // nothing to close
    }

    BytesOutSize clear() {
        this.size = 0
        return this
    }

    int getSize() {
        return size
    }
}
