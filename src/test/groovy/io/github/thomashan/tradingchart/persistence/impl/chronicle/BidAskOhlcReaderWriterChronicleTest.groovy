package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.price.BidAsk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Instant

class BidAskOhlcReaderWriterChronicleTest implements ObjectReaderWriterChronicleTestCase<BidAskOhlc> {
    private BidAskOhlcWriterChronicle objectWriter
    private BidAskOhlcReaderChronicle objectReader

    @BeforeEach
    void setUp() {
        ObjectReaderWriterChronicleTestCase.super.setUp()
        this.objectWriter = io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectReaderWriterChronicleTestCase__objectWriter
        this.objectReader = io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectReaderWriterChronicleTestCase__objectReader
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
    @Test
    void testWriteAndRead() {
        List<BidAskOhlc> bidAskOhlcs = [
                BidAskOhlc.of(Instant.now(), BidAsk.of(1, 1), BidAsk.of(1, 1), BidAsk.of(1, 1), BidAsk.of(1, 1), 1),
                BidAskOhlc.of(Instant.now(), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), 1.1),
                BidAskOhlc.of(Instant.now(), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), 1.2),
                BidAskOhlc.of(Instant.now(), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), 1.3),
        ]

        objectWriter.write(bidAskOhlcs[0])
        objectWriter.write(bidAskOhlcs[1])
        objectWriter.write(bidAskOhlcs[2])
        objectWriter.write(bidAskOhlcs[3])

        assert objectReader.read() == bidAskOhlcs[0]
        assert objectReader.read() == bidAskOhlcs[1]
        assert objectReader.read() == bidAskOhlcs[2]
        assert objectReader.read() == bidAskOhlcs[3]
    }
}
