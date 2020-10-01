package io.github.thomashan.tradingchart.input.csv.univocity

import com.univocity.parsers.csv.CsvParserSettings
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.Price

import java.util.stream.Stream

class CsvParserUnivocity<P extends Price> implements CsvParser<P> {
    private final CsvParserSettings csvParserSettings
    private final com.univocity.parsers.csv.CsvParser csvParser

    CsvParserUnivocity() {
        this.csvParserSettings = new CsvParserSettings()
        csvParserSettings.headerExtractionEnabled = true
        this.csvParser = new com.univocity.parsers.csv.CsvParser(csvParserSettings)
    }

    @Override
    Stream<Ohlc<P>> parse(Stream<String> inputRows) {
        throw new UnsupportedOperationException("not implemented")
    }

    @Override
    Stream<Ohlc<P>> parse(InputStream inputStream) {
        return csvParser.parseAll(inputStream).stream()
                .map(createOhlc)
    }
}
