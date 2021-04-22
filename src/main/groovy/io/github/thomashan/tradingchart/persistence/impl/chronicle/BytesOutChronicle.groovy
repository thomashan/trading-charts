package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.persistence.BytesOut
import net.openhft.chronicle.queue.ExcerptAppender

import java.time.Instant

class BytesOutChronicle implements BytesOut<BytesOutChronicle> {
    private final ExcerptAppender excerptAppender

    BytesOutChronicle(ExcerptAppender excerptAppender) {
        this.excerptAppender = excerptAppender
    }

    @Override
    BytesOutChronicle writeInstant(Instant value) {
        excerptAppender.writeBytes(bytes -> bytes.writeLong(value.epochSecond).writeInt(value.nano))
        return this
    }

    @Override
    BytesOutChronicle writeInt(int value) {
        excerptAppender.writeBytes(bytes -> bytes.writeInt(value))
        return this
    }

    @Override
    BytesOutChronicle writeLong(long value) {
        excerptAppender.writeBytes(bytes -> bytes.writeLong(value))
        return this
    }

    @Override
    BytesOutChronicle writeDouble(double value) {
        excerptAppender.writeBytes(bytes -> bytes.writeDouble(value))
        return this
    }

    @Override
    void close() throws Exception {
        excerptAppender.close()
    }
}
