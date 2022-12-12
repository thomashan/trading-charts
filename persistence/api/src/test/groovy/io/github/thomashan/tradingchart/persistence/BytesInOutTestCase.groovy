package io.github.thomashan.tradingchart.persistence

import io.github.thomashan.tradingchart.time.MutableInstant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

trait BytesInOutTestCase<BO extends BytesOut, BI extends BytesIn> {
    private BI bytesIn
    private BO bytesOut

    abstract BO createBytesOut()

    abstract BI createBytesIn()

    @BeforeEach
    void setUp() {
        this.bytesOut = createBytesOut()
        this.bytesIn = createBytesIn()
    }

    @Test
    void testWriteReadInstant() {
        MutableInstant value = MutableInstant.EPOCH
        bytesOut.writeInstant(value)
        MutableInstant read = bytesIn.readInstant()
        assert read == value
    }

    @Test
    void testWriteReadInt() {
        int value = 10000
        bytesOut.writeInt(value)
        int read = bytesIn.readInt()
        assert read == value
    }

    @Test
    void testWriteReadLong() {
        long value = 10000
        bytesOut.writeLong(value)
        long read = bytesIn.readLong()
        assert read == value
    }

    @Test
    void testWriteReadDouble() {
        double value = 10000.0
        bytesOut.writeDouble(value)
        double read = bytesIn.readDouble()
        assert read == value
    }
}
