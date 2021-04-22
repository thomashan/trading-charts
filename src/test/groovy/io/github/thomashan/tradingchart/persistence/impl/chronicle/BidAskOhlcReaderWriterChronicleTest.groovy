package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.price.BidAsk
import org.junit.jupiter.api.BeforeEach

import java.time.Instant

class BidAskOhlcReaderWriterChronicleTest implements ObjectReaderWriterChronicleTestCase<BidAskOhlc> {
    @BeforeEach
    void setUp() {
        ObjectReaderWriterChronicleTestCase.super.setUp()
    }

    @Override
    ObjectWriterChronicle<BidAskOhlc> createObjectWriter(String chronicleDir) {
        return new BidAskOhlcWriterChronicle(chronicleDir)
    }

    @Override
    ObjectReaderChronicle<BidAskOhlc> createObjectReader(String chronicleDir) {
        return new BidAskOhlcReaderChronicle(chronicleDir)
    }

    @Override
    List<BidAskOhlc> createObjects() {
        return [
                BidAskOhlc.of(Instant.now(), BidAsk.of(1, 1), BidAsk.of(1, 1), BidAsk.of(1, 1), BidAsk.of(1, 1), 1),
                BidAskOhlc.of(Instant.now(), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), 1.1),
                BidAskOhlc.of(Instant.now(), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), 1.2),
                BidAskOhlc.of(Instant.now(), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), 1.3),
        ]
    }
}
