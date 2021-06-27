package io.github.thomashan.tradingchart.persistence.impl.size

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Instant

class BytesOutSizeTest {
    private BytesOutSize bytesOutSize

    @BeforeEach
    void setUp() {
        this.bytesOutSize = BytesOutSize.instance.clear()
    }

    @Test
    void testWriteInstant() {
        bytesOutSize.writeInstant(Instant.now())
        assert 12 == bytesOutSize.size
    }

    @Test
    void testWriteInt() {
        bytesOutSize.writeInt(0)
        assert 4 == bytesOutSize.size
    }

    @Test
    void testWriteLong() {
        bytesOutSize.writeLong(0)
        assert 8 == bytesOutSize.size
    }

    @Test
    void testWriteDouble() {
        bytesOutSize.writeDouble(0)
        assert 8 == bytesOutSize.size
    }
}
