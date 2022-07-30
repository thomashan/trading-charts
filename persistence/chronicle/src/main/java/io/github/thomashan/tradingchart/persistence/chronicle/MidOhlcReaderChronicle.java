package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.persistence.domain.ohlc.MidOhlcReader;
import net.openhft.chronicle.queue.ChronicleQueue;

public class MidOhlcReaderChronicle extends BaseObjectToChronicle implements ObjectReaderChronicle<MidOhlc>, MidOhlcReader<BytesInChronicle> {
    private final BytesInChronicle bytesIn;

    public MidOhlcReaderChronicle(String fileName) {
        super(ChronicleQueue.singleBuilder(fileName).build());
        this.bytesIn = new BytesInChronicle(getChronicleQueue().createTailer());
    }

    @Override
    public BytesInChronicle getBytesIn() {
        return bytesIn;
    }
}
