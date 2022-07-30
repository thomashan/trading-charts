package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.persistence.domain.ohlc.BidAskOhlcReader;
import net.openhft.chronicle.queue.ChronicleQueue;

public class BidAskOhlcReaderChronicle extends BaseObjectToChronicle implements ObjectReaderChronicle<BidAskOhlc>, BidAskOhlcReader<BytesInChronicle> {
    private final BytesInChronicle bytesIn;

    public BidAskOhlcReaderChronicle(String fileName) {
        super(ChronicleQueue.singleBuilder(fileName).build());
        this.bytesIn = new BytesInChronicle(getChronicleQueue().createTailer());
    }

    @Override
    public BytesInChronicle getBytesIn() {
        return bytesIn;
    }
}
