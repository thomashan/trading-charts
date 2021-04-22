package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.persistence.ObjectReader
import net.openhft.chronicle.queue.ChronicleQueue

trait ObjectReaderChronicle<E> extends ObjectReader<E, BytesInChronicle> {
    final ChronicleQueue chronicleQueue
    final BytesInChronicle bytesIn

    @Override
    void close() throws Exception {
        bytesIn.close()
        chronicleQueue.close()
    }
}
