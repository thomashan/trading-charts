package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.BidAsk
import io.github.thomashan.tradingchart.price.Price

import java.nio.charset.Charset
import java.util.function.BiFunction
import java.util.stream.Stream

trait CsvParserStream<P extends Price> implements CsvParser<P> {
    private Optional<Map<String, Integer>> headerIndexes = Optional.empty()

    @Override
    Stream<Ohlc<P>> parse(InputStream inputStream) {
        return parse(new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset())).lines())
    }

    @Override
    Stream<Ohlc<P>> parse(Stream<String> inputRows) {
        int index = 0
        BiFunction<String[], Map<String, Integer>, Ohlc<BidAsk>> createFunction = createOhlcFunction

        return inputRows.map {
            index++
            if (isHeader(index, it)) {
                String[] header = split(it)
                Map<String, Integer> headerMap = [:]
                for (int i = 0; i < header.size(); i++) {
                    headerMap[header[i]] = i
                }
                this.headerIndexes = Optional.of(headerMap)
                return Optional.empty()
            }
            return Optional.of(split(it))
        }
                .flatMap(Optional::stream)
                .map {
                    if (headerIndexes.present) {
                        return createFunction.apply(it, headerIndexes.get())
                    }
                    return createFunction.apply(it, defaultHeaders)
                }
    }

    private boolean isHeader(int index, String inputRow) {
        int character = inputRow.charAt(0)
        if (index == 1 && Character.isAlphabetic(character)) {
            return true
        }

        return false
    }

    abstract String[] split(String string)
}
