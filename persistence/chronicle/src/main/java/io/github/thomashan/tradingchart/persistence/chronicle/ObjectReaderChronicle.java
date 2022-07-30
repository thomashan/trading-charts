package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.persistence.ObjectReader;

public interface ObjectReaderChronicle<E> extends ObjectToChronicle, ObjectReader<E, BytesInChronicle> {
    @Override
    default void close() {
        getBytesIn().close();
        getChronicleQueue().close();
    }
}
