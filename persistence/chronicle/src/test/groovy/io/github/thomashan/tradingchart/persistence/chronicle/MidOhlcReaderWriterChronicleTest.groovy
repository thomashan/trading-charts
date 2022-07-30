package io.github.thomashan.tradingchart.persistence.chronicle


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.domain.price.Mid
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Instant

class MidOhlcReaderWriterChronicleTest implements ObjectReaderWriterChronicleTestCase<MidOhlc> {
    @BeforeEach
    void setUp() {
        ObjectReaderWriterChronicleTestCase.super.setUp()
    }

    @Override
    ObjectWriterChronicle<MidOhlc> createObjectWriter(String chronicleDir) {
        return new MidOhlcWriterChronicle(chronicleDir)
    }

    @Override
    ObjectReaderChronicle<MidOhlc> createObjectReader(String chronicleDir) {
        return new MidOhlcReaderChronicle(chronicleDir)
    }

    @Override
    @Test
    void testWriteAndRead() {
        List<MidOhlc> midOhlcs = [
            MidOhlc.of(Instant.now(), Mid.of(1), Mid.of(1), Mid.of(1), Mid.of(1), 1),
            MidOhlc.of(Instant.now(), Mid.of(1.1), Mid.of(1.1), Mid.of(1.1), Mid.of(1.1), 1.1),
            MidOhlc.of(Instant.now(), Mid.of(1.2), Mid.of(1.2), Mid.of(1.2), Mid.of(1.2), 1.2),
            MidOhlc.of(Instant.now(), Mid.of(1.3), Mid.of(1.3), Mid.of(1.3), Mid.of(1.3), 1.3),
        ]

        objectWriter.write(midOhlcs[0])
        objectWriter.write(midOhlcs[1])
        objectWriter.write(midOhlcs[2])
        objectWriter.write(midOhlcs[3])

        assert objectReader.read() == midOhlcs[0]
        assert objectReader.read() == midOhlcs[1]
        assert objectReader.read() == midOhlcs[2]
        assert objectReader.read() == midOhlcs[3]
    }
}
