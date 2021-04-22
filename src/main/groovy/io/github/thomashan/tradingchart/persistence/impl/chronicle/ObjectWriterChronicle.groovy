package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.persistence.ObjectWriter
import net.openhft.chronicle.queue.ChronicleQueue

trait ObjectWriterChronicle<E> extends ObjectWriter<E, BytesOutChronicle> {
    final ChronicleQueue chronicleQueue
    final BytesOutChronicle bytesOut

    @Override
    void close() throws Exception {
        bytesOut.close()
        chronicleQueue.close()
    }
}
