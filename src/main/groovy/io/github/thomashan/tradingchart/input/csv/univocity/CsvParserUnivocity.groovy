package io.github.thomashan.tradingchart.input.csv.univocity

import com.univocity.parsers.csv.CsvParserSettings
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.input.csv.DefaultHeader
import io.github.thomashan.tradingchart.input.csv.OhlcCreator

import java.util.function.BiFunction
import java.util.stream.Stream

class CsvParserUnivocity<O extends Ohlc> implements CsvParser<O> {
    private final CsvParserSettings csvParserSettings
    private final com.univocity.parsers.csv.CsvParser csvParser
    private Map<String, Integer> headerIndexes
    private BiFunction<String[], Map<String, Integer>, O> createFunction

    CsvParserUnivocity() {
        this.csvParserSettings = new CsvParserSettings()
        csvParserSettings.headerExtractionEnabled = true
        this.csvParser = new com.univocity.parsers.csv.CsvParser(csvParserSettings)
    }

    @Override
    Stream<O> parse(Stream<String> inputRows) {
        throw new UnsupportedOperationException("not implemented")
    }

    @Override
    Stream<O> parse(InputStream inputStream) {
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
