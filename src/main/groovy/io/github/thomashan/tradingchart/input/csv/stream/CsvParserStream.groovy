package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.input.csv.DefaultHeader
import io.github.thomashan.tradingchart.input.csv.OhlcCreator
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.Price

import java.nio.charset.Charset
import java.util.function.BiFunction
import java.util.stream.Stream

trait CsvParserStream<P extends Price> implements CsvParser<P> {
    private Map<String, Integer> headerIndexes
    private BiFunction<String[], Map<String, Integer>, Ohlc<P>> createFunction

    @Override
    Stream<Ohlc<P>> parse(InputStream inputStream) {
        return parse(new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset())).lines())
    }

    @Override
    Stream<Ohlc<P>> parse(Stream<String> inputRows) {
        int index = 0
        return inputRows.map {
            index++
            if (isHeader(index, it)) {
                setHeaderInfo(it)
                return Optional.empty()
            }
            return Optional.of(split(it))
        }
                .flatMap(Optional::stream)
                .map { String[] row ->
                    setDefaultHeaderIndexes(row)
                    setCreateFunction(row)
                    createFunction.apply(row, headerIndexes)
                }
    }

    private void setDefaultHeaderIndexes(String[] row) {
        if (!headerIndexes) {
            this.headerIndexes = DefaultHeader.getDefaultHeaders(row)
        }
    }

    private void setCreateFunction(String[] headers) {
        if (!createFunction) {
            this.createFunction = OhlcCreator.getCreator(headers)
        }
    }

    private void setHeaderInfo(String headerString) {
        String[] header = split(headerString)
        Map<String, Integer> headerMap = [:]
        for (int i = 0; i < header.size(); i++) {
            headerMap[header[i]] = i
        }
        this.headerIndexes = headerMap
    }

    private boolean isHeader(int index, String inputRow) {
        if (index == 1 && Character.isAlphabetic((int) inputRow.charAt(0))) {
            return true
        }

        return false
    }

    abstract String[] split(String string)
}
