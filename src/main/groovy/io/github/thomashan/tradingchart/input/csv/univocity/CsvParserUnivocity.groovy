package io.github.thomashan.tradingchart.input.csv.univocity

import com.univocity.parsers.csv.CsvParserSettings
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.BidAsk
import io.github.thomashan.tradingchart.price.Price

import java.time.ZonedDateTime
import java.util.function.Function
import java.util.stream.Stream

class CsvParserUnivocity<P extends Price> implements CsvParser<P> {
    private final CsvParserSettings csvParserSettings
    private final com.univocity.parsers.csv.CsvParser csvParser
    private final Function<String[], Ohlc<P>> createOhlc = new Function<String[], Ohlc<P>>() {
        @Override
        Ohlc apply(String[] strings) {
            return Ohlc.of(ZonedDateTime.parse(strings[0]),
                    BidAsk.of(strings[2].toDouble(), strings[1].toDouble()),
                    BidAsk.of(strings[4].toDouble(), strings[3].toDouble()),
                    BidAsk.of(strings[6].toDouble(), strings[5].toDouble()),
                    BidAsk.of(strings[8].toDouble(), strings[7].toDouble()),
                    strings[9].toDouble())
        }
    }

    CsvParserUnivocity() {
        this.csvParserSettings = new CsvParserSettings()
        csvParserSettings.headerExtractionEnabled = true
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
