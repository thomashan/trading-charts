package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.persistence.domain.ohlc.BidAskOhlcWriter;
import net.openhft.chronicle.queue.ChronicleQueue;

public class BidAskOhlcWriterChronicle extends BaseObjectToChronicle implements ObjectWriterChronicle<BidAskOhlc>, BidAskOhlcWriter<BytesOutChronicle> {
    private final BytesOutChronicle bytesOut;
    private long index;

    public BidAskOhlcWriterChronicle(String fileName) {
        super(ChronicleQueue.singleBuilder(fileName).build());
        this.bytesOut = new BytesOutChronicle(getChronicleQueue().acquireAppender());
    }

    @Override
    public long getIndex() {
        return index;
    }

    @Override
    public void setIndex(long index) {
        this.index = index;
    }

    @Override
    public BytesOutChronicle getBytesOut() {
        return bytesOut;
    }

    @Override
    public void close() {
        ObjectWriterChronicle.super.close();
        bytesOut.close();
    }
}
