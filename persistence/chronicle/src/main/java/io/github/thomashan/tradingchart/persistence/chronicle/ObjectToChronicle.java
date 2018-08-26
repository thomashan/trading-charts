package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.persistence.Header;
import net.openhft.chronicle.queue.ChronicleQueue;

public interface ObjectToChronicle {
    ChronicleQueue getChronicleQueue();

    Header getHeader();
}
