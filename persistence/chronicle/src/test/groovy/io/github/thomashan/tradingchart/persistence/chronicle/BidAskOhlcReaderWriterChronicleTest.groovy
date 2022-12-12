package io.github.thomashan.tradingchart.persistence.chronicle

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.price.BidAsk
import io.github.thomashan.tradingchart.time.MutableInstant
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
    @Test
    void testWriteAndRead() {
        List<BidAskOhlc> bidAskOhlcs = [
            BidAskOhlc.of(MutableInstant.EPOCH, BidAsk.of(1, 1), BidAsk.of(1, 1), BidAsk.of(1, 1), BidAsk.of(1, 1), 1),
            BidAskOhlc.of(MutableInstant.EPOCH, BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), BidAsk.of(1.1, 1.1), 1.1),
            BidAskOhlc.of(MutableInstant.EPOCH, BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), BidAsk.of(1.2, 1.2), 1.2),
            BidAskOhlc.of(MutableInstant.EPOCH, BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), BidAsk.of(1.3, 1.3), 1.3),
        ]

        objectWriter.write(bidAskOhlcs[0])
        objectWriter.write(bidAskOhlcs[1])
        objectWriter.write(bidAskOhlcs[2])
        objectWriter.write(bidAskOhlcs[3])

        BidAskOhlc bidAskOhlc = objectReader.read()
        assert bidAskOhlc == bidAskOhlcs[0]
        assert objectReader.read() == bidAskOhlcs[1]
        assert objectReader.read() == bidAskOhlcs[2]
        assert objectReader.read() == bidAskOhlcs[3]
    }
}
