package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.domain.price.Mid
import org.junit.jupiter.api.BeforeEach

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
    List<MidOhlc> createObjects() {
        return [
                MidOhlc.of(Instant.now(), Mid.of(1), Mid.of(1), Mid.of(1), Mid.of(1), 1),
                MidOhlc.of(Instant.now(), Mid.of(1.1), Mid.of(1.1), Mid.of(1.1), Mid.of(1.1), 1.1),
                MidOhlc.of(Instant.now(), Mid.of(1.2), Mid.of(1.2), Mid.of(1.2), Mid.of(1.2), 1.2),
                MidOhlc.of(Instant.now(), Mid.of(1.3), Mid.of(1.3), Mid.of(1.3), Mid.of(1.3), 1.3),
        ]
    }
}
