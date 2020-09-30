package io.github.thomashan.tradingchart.input.csv


import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.BidAsk
import org.junit.jupiter.api.Test

import java.time.ZonedDateTime
import java.util.stream.Collectors
import java.util.stream.Stream

trait CsvParserTestCase<C extends CsvParser> {
    C csvParser

    @Test
    void testParse_StreamInputRows() {
        Stream<Ohlc<BidAsk>> ohlcStream = csvParser.parse(Stream.of("2019-08-13T06:18:15Z,1.11904,1.11892,1.11907,1.11895,1.11904,1.11892,1.11907,1.11895,2"))
        List<Ohlc<BidAsk>> ohlcs = ohlcStream.collect(Collectors.toList())

        assert 1 == ohlcs.size()
        assert ZonedDateTime.parse("2019-08-13T06:18:15Z") == ohlcs[0].dateTime
        assert 1.11904 == ohlcs[0].open.ask
        assert 1.11892 == ohlcs[0].open.bid
        assert 1.11907 == ohlcs[0].high.ask
        assert 1.11895 == ohlcs[0].high.bid
        assert 1.11904 == ohlcs[0].low.ask
        assert 1.11892 == ohlcs[0].low.bid
        assert 1.11907 == ohlcs[0].close.ask
        assert 1.11895 == ohlcs[0].close.bid
        assert 2 == ohlcs[0].volume
    }
}
