package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.Price

import java.nio.charset.Charset
import java.util.stream.Stream

trait CsvParserStream<P extends Price> implements CsvParser<P> {
    @Override
    Stream<Ohlc<P>> parse(InputStream inputStream) {
        return parse(new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset())).lines())
    }

    @Override
    Stream<Ohlc<P>> parse(Stream<String> inputRows) {
        return inputRows.map({ isHeader(it) ? Optional.empty() : Optional.of(split(it)) })
                .flatMap(Optional::stream)
                .map(CsvParser.createOhlc)
    }

    private boolean isHeader(String inputRow) {
        return inputRow.startsWith("date")
    }

    abstract String[] split(String string)
}
