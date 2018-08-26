package io.github.thomashan.tradingchart.persistence;

import java.util.function.Consumer;

public interface ObjectWriter<E, BO extends BytesOut<?>> extends AutoCloseable {
    @Override
    void close();

    long getIndex();

    void setIndex(long index);

    Header getHeader();

    BO getBytesOut();

    Consumer<E> getObjectConsumer();

    default void write(E e) {
        getHeader().index = getIndex();
        writeHeader();
        getObjectConsumer().accept(e);
        setIndex(getIndex() + 1);
    }

    default void writeHeader() {
        getBytesOut().writeLong(getHeader().index);
    }
}
