package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.persistence.BytesOut;
import io.github.thomashan.tradingchart.time.MutableInstant;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ExcerptAppender;

import java.nio.ByteBuffer;
import java.time.Instant;

public class BytesOutChronicle implements BytesOut<BytesOutChronicle> {
    private final ExcerptAppender excerptAppender;
    private final Bytes<ByteBuffer> bytesOut = Bytes.elasticByteBuffer();

    BytesOutChronicle(ExcerptAppender excerptAppender) {
        this.excerptAppender = excerptAppender;
    }

    @Override
    public BytesOutChronicle writeInstant(MutableInstant value) {
        bytesOut.clear();
        bytesOut.writeLong(value.getEpochSecond());
        bytesOut.writeInt(value.getNano());
        excerptAppender.writeBytes(bytesOut);
        return this;
    }

    @Override
    public BytesOutChronicle writeInt(int value) {
        bytesOut.clear();
        bytesOut.writeInt(value);
        excerptAppender.writeBytes(bytesOut);
        return this;
    }

    @Override
    public BytesOutChronicle writeLong(long value) {
        bytesOut.clear();
        bytesOut.writeLong(value);
        excerptAppender.writeBytes(bytesOut);
        return this;
    }

    @Override
    public BytesOutChronicle writeDouble(double value) {
        bytesOut.clear();
        bytesOut.writeDouble(value);
        excerptAppender.writeBytes(bytesOut);
        return this;
    }

    @Override
    public void close() {
        excerptAppender.close();
    }
}
