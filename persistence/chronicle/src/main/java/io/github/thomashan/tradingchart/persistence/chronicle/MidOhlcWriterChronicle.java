package io.github.thomashan.tradingchart.persistence.chronicle;

import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.persistence.domain.ohlc.MidOhlcWriter;
import net.openhft.chronicle.queue.ChronicleQueue;

public class MidOhlcWriterChronicle extends BaseObjectToChronicle implements ObjectWriterChronicle<MidOhlc>, MidOhlcWriter<BytesOutChronicle> {
    private final BytesOutChronicle bytesOut;
    private long index;

    public MidOhlcWriterChronicle(String fileName) {
        super(ChronicleQueue.singleBuilder(fileName).build());
        this.bytesOut = new BytesOutChronicle(getChronicleQueue().acquireAppender());
    }

    @Override
    public void close() {
        ObjectWriterChronicle.super.close();
        bytesOut.close();
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
}
