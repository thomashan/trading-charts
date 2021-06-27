package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.persistence.domain.ohlc.BidAskOhlcWriter
import net.openhft.chronicle.queue.ChronicleQueue

class BidAskOhlcWriterChronicle implements ObjectWriterChronicle<BidAskOhlc>, BidAskOhlcWriter<BytesOutChronicle> {
    BidAskOhlcWriterChronicle(String fileName) {
        this.io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectWriterChronicle__chronicleQueue = ChronicleQueue.singleBuilder(fileName).build()
        this.io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectWriterChronicle__bytesOut = new BytesOutChronicle(chronicleQueue.acquireAppender())
    }
}
