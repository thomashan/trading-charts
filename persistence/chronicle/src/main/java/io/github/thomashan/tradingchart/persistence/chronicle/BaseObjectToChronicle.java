package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.persistence.Header;
import net.openhft.chronicle.queue.ChronicleQueue;

public abstract class BaseObjectToChronicle implements ObjectToChronicle {
    private final ChronicleQueue chronicleQueue;
    private final Header header = new Header();

    public BaseObjectToChronicle(ChronicleQueue chronicleQueue) {
        this.chronicleQueue = chronicleQueue;
    }

    @Override
    public ChronicleQueue getChronicleQueue() {
        return chronicleQueue;
    }

    @Override
    public Header getHeader() {
        return header;
    }
}
