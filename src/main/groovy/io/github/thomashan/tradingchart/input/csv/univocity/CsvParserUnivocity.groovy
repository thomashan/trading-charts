package io.github.thomashan.tradingchart.input.csv.univocity

import com.univocity.parsers.csv.CsvParserSettings
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.input.csv.DefaultHeader
import io.github.thomashan.tradingchart.input.csv.OhlcCreator
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.Price

import java.util.function.BiFunction
import java.util.stream.Stream

class CsvParserUnivocity<P extends Price> implements CsvParser<P> {
    private final CsvParserSettings csvParserSettings
    private final com.univocity.parsers.csv.CsvParser csvParser
    private Map<String, Integer> headerIndexes
    private BiFunction<String[], Map<String, Integer>, Ohlc<P>> createFunction

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
                .map {
                    setHeaderIndexes(it)
                    setCreateFunction(it)
                    createFunction.apply(it, headerIndexes)
                }
    }

    private void setHeaderIndexes(String[] row) {
        if (!headerIndexes) {
            this.headerIndexes = DefaultHeader.getDefaultHeaders(row)
        }
    }

    private void setCreateFunction(String[] row) {
        if (!createFunction) {
            this.createFunction = OhlcCreator.getCreator(row)
        }
    }
}
