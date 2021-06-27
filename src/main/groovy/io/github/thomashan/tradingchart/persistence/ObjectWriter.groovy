package io.github.thomashan.tradingchart.persistence

import java.util.function.Consumer

trait ObjectWriter<E, BO extends BytesOut> implements AutoCloseable {
    private Header header = new Header()
    private long index

    Header getHeader() {
        return header
    }

    abstract BO getBytesOut()

    abstract Consumer<E> getObjectConsumer()

    void write(E e) {
        header.index = index
        writeHeader()
        objectConsumer.accept(e)
        index = index + 1
    }

    void writeHeader() {
        bytesOut.writeLong(header.index)
    }
}
