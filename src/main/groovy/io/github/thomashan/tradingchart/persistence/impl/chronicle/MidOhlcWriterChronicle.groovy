package io.github.thomashan.tradingchart.persistence.impl.chronicle


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.persistence.domain.ohlc.MidOhlcWriter
import net.openhft.chronicle.queue.ChronicleQueue

class MidOhlcWriterChronicle implements ObjectWriterChronicle<MidOhlc>, MidOhlcWriter<BytesOutChronicle> {
    MidOhlcWriterChronicle(String fileName) {
        this.io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectWriterChronicle__chronicleQueue = ChronicleQueue.singleBuilder(fileName).build()
        this.io_github_thomashan_tradingchart_persistence_impl_chronicle_ObjectWriterChronicle__bytesOut = new BytesOutChronicle(chronicleQueue.acquireAppender())
    }
}
