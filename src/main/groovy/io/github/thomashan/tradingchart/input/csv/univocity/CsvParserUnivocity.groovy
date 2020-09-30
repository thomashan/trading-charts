package io.github.thomashan.tradingchart.input.csv.univocity

import com.univocity.parsers.csv.CsvParserSettings
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.ohlc.Ohlc

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class CsvParserUnivocity implements CsvParser {
    private final CsvParserSettings csvParserSettings
    private final com.univocity.parsers.csv.CsvParser csvParser

    CsvParserUnivocity() {
        this.csvParserSettings = new CsvParserSettings()
        csvParserSettings.rowProcessor
        this.csvParser = new com.univocity.parsers.csv.CsvParser(csvParserSettings)
    }

    @Override
    Stream<Ohlc> parse(Stream<String> inputRows) {
        Files.lines(Paths.get(""))
        List<String[]> allRows = csvParser.parse(new FileInputStream())

        return csvParser.parse()
    }

    @Override
    Stream<Ohlc> parse(InputStream inputStream) {
        return null
    }
}
