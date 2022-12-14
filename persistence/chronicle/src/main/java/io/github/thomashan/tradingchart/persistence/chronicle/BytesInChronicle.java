package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.persistence.BytesIn;
import io.github.thomashan.tradingchart.time.MutableInstant;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ExcerptTailer;

import java.nio.ByteBuffer;
import java.time.Instant;

public class BytesInChronicle implements BytesIn {
    private final ExcerptTailer excerptTailer;
    private final Bytes<ByteBuffer> bytesIn = Bytes.elasticByteBuffer();
    private final MutableInstant instant = MutableInstant.EPOCH;

    public BytesInChronicle(ExcerptTailer excerptTailer) {
        this.excerptTailer = excerptTailer;
    }

    private void initialiseBytesIn() {
        bytesIn.clear();
        excerptTailer.readBytes(bytesIn);
    }

    @Override
    public MutableInstant readInstant() {
        initialiseBytesIn();
        long epochSeconds = bytesIn.readLong();
        int nano = bytesIn.readInt();
        return instant.ofEpochSecondMutable(epochSeconds, nano);
    }

    @Override
    public int readInt() {
        initialiseBytesIn();
        int value = bytesIn.readInt();
        return value;
    }

    @Override
    public long readLong() {
        initialiseBytesIn();
        return bytesIn.readLong();
    }

    @Override
    public double readDouble() {
        initialiseBytesIn();
        return bytesIn.readDouble();
    }

    @Override
    public void close() {
        excerptTailer.close();
    }
}
