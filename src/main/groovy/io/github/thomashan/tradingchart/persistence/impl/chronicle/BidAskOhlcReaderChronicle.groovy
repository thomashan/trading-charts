package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.persistence.domain.ohlc.BidAskOhlcReader
import net.openhft.chronicle.queue.ChronicleQueue

class BidAskOhlcReaderChronicle implements ObjectReaderChronicle<BidAskOhlc>, BidAskOhlcReader {
    BidAskOhlcReaderChronicle(String fileName) {
        this.io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectReaderChronicle__chronicleQueue = ChronicleQueue.singleBuilder(fileName).build()
        this.io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectReaderChronicle__bytesIn = new BytesInChronicle(chronicleQueue.createTailer())
    }
}
