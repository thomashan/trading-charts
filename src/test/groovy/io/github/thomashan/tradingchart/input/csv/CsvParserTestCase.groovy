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
    void testParse_Stream() {
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

    @Test
    void testParse_InputStream() {

        InputStream inputStream = this.class.getResourceAsStream("/EURUSD-S5.csv")

//        InputStream inputStream = new URL("https://doc-0c-0g-docs.googleusercontent.com/docs/securesc/3rpa7l0iau6fa4kl1c8r8790kr9j0rg4/sab40qav39vqmvbvlkg1116apca1p5oq/1601459175000/08905669649840263172/08905669649840263172/1AQcNzrVV4dy5RKzUWQ1cIeFUeN9h4b0n?e=download&authuser=0&nonce=a3vtqh14klhek&user=08905669649840263172&hash=sfovqqsvf3ep6etpp06ecv988ej059gr").newInputStream()
//        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)
        Stream<Ohlc<BidAsk>> ohlcStream = csvParser.parse(inputStream)
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
