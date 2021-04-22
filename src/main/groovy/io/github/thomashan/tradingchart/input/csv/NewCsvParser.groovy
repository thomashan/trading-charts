package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc

import java.nio.CharBuffer
import java.nio.DirectByteBuffer
import java.util.function.Consumer

trait NewCsvParser<O extends Ohlc> {
    private static final char COMMA = ','
    private long index = 0
    private final CharBuffer charBuffer = DirectByteBuffer.allocateDirect(128).asCharBuffer()
    private final Map<String, Consumer<CharSequence>> setters = createSetter()
    private final Consumer<O> consumer
    final Map<Integer, String> headerIndexes = [:]
    final O ohlc = emptyOhlc()

    abstract O emptyOhlc()

    abstract Map<String, Consumer<CharSequence>> createSetter()

    void parse(CharSequence line) {
        if (isHeader(line)) {
            readHeader(line)
        } else {
            readOhlc(line)
        }
        this.index = index + 1
    }

    private O readOhlc(CharSequence line) {
        parseOhlc(line)
        consumer.accept(ohlc)
        return ohlc
    }

    private void parseOhlc(CharSequence row) {
        int index = 0
        for (int i = 0; i < row.length(); i++) {
            if (COMMA != row.charAt(i)) {
                charBuffer.put(row.charAt(i))
            } else {
                setFieldValue(index)
                index++
            }
        }
        setFieldValue(index)
    }

    private void setFieldValue(int index) {
        charBuffer.flip()
        Consumer<CharSequence> consumer = setters[headerIndexes[index]]
        consumer.accept(charBuffer)
        charBuffer.clear()
    }

    private void readHeader(CharSequence line) {
        headerIndexes.clear()
        int headerIndex = 0
        for (int i = 0; i < line.length(); i++) {
            if (COMMA != line.charAt(i)) {
                charBuffer.put(line.charAt(i))
            } else {
                setHeaderValue(headerIndex)
                headerIndex++
            }
        }
        setHeaderValue(headerIndex)
        if (headerIndexes.size() != expectedHeaderLength) {
            throw new IllegalArgumentException("expected " + expectedHeaderLength + " fields but got " + headerIndexes.size())
        }
        if (!headerIndexes.entrySet().stream()
                .allMatch(headerIndexColumn -> setters.containsKey(headerIndexColumn.value))) {
            throw new IllegalArgumentException("unexpected columns, please name the columns " + setters.keySet().join(","))
        }
    }

    private setHeaderValue(int headerIndex) {
        charBuffer.flip()
        headerIndexes.put(headerIndex, charBuffer.toString())
        charBuffer.clear()
    }

    abstract int getExpectedHeaderLength()

    private boolean isHeader(CharSequence row) {
        return index == 0 && Character.isAlphabetic((int) row.charAt(0))
    }

    void clear() {
        index = 0
        headerIndexes.clear()
        charBuffer.clear()
    }
}
