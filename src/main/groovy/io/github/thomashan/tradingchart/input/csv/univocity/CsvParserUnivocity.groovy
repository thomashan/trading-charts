package io.github.thomashan.tradingchart.input.csv.univocity

import com.univocity.parsers.csv.CsvParserSettings
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.BidAsk

import java.time.ZonedDateTime
import java.util.function.Function
import java.util.stream.Stream

class CsvParserUnivocity implements CsvParser {
    private final CsvParserSettings csvParserSettings
    private final com.univocity.parsers.csv.CsvParser csvParser
    private final Function<String[], Ohlc> createOhlc = new Function<String[], Ohlc>() {
        @Override
        Ohlc apply(String[] strings) {
            return Ohlc.of(ZonedDateTime.parse(strings[0]),
                    BidAsk.of(strings[1].toDouble(), strings[2].toDouble()),
                    BidAsk.of(strings[3].toDouble(), strings[4].toDouble()),
                    BidAsk.of(strings[5].toDouble(), strings[6].toDouble()),
                    BidAsk.of(strings[7].toDouble(), strings[8].toDouble()),
                    strings[9].toDouble())
        }
    }

    CsvParserUnivocity() {
        this.csvParserSettings = new CsvParserSettings()
        this.csvParser = new com.univocity.parsers.csv.CsvParser(csvParserSettings)
    }

    @Override
    Stream<Ohlc> parse(Stream<String> inputRows) {
        throw new UnsupportedOperationException("not implemented")
    }

    @Override
    Stream<Ohlc> parse(InputStream inputStream) {
        return csvParser.parseAll(inputStream).stream()
                .map(createOhlc)
    }
}
