package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.persistence.ObjectWriter;

public interface ObjectWriterChronicle<E> extends ObjectToChronicle, ObjectWriter<E, BytesOutChronicle> {
    @Override
    default void close() {
        getBytesOut().close();
        getChronicleQueue().close();
    }
}
