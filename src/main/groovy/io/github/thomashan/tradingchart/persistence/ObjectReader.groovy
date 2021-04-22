package io.github.thomashan.tradingchart.persistence

trait ObjectReader<E, BI extends BytesIn> implements AutoCloseable {
    private Header header = new Header()

    abstract BI getBytesIn()

    void readHeader() {
        header.index = bytesIn.readLong()
    }

    E read() {
        readHeader()
        return readObject()
    }

    abstract E readObject()
}
