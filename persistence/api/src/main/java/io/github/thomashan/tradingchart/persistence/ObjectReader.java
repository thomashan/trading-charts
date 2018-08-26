package io.github.thomashan.tradingchart.persistence;

public interface ObjectReader<E, BI extends BytesIn> extends AutoCloseable {
    Header getHeader();

    BI getBytesIn();

    default void readHeader() {
        getHeader().index = getBytesIn().readLong();
    }

    default E read() {
        readHeader();
        return readObject();
    }

    E readObject();

    @Override
    void close();
}
