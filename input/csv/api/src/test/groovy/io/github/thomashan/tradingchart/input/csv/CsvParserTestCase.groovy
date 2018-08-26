package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

import java.time.Instant
import java.util.stream.Collectors
import java.util.stream.Stream

trait CsvParserTestCase<C extends CsvParser> {
    C csvParser

    @BeforeAll
    static void setUpClass() {
//        File.createTempFile("EURUSD-s5.csv", ".gz").withOutputStream { outputStream ->
//            outputStream << new URL("https://drive.google.com/file/d/1AQcNzrVV4dy5RKzUWQ1cIeFUeN9h4b0n/view?usp=sharing").openStream()
//        }
    }

    @AfterAll
    static void tearDownClass() {

    }

    @Test
    void testParseBidAsk_Stream() {
        Stream<BidAskOhlc> ohlcStream = csvParser.parse(Stream.of("2019-08-13T06:18:15Z,1.11904,1.11892,1.11907,1.11895,1.11904,1.11892,1.11907,1.11895,2"))
        List<BidAskOhlc> ohlcs = ohlcStream.collect(Collectors.toList())

        assert 1 == ohlcs.size()
        assert Instant.parse("2019-08-13T06:18:15Z") == ohlcs[0].dateTime
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
    void testParseMid_Stream() {
        Stream<MidOhlc> ohlcStream = csvParser.parse(Stream.of("2019-08-13T06:18:15Z,1.11898,1.11901,1.11898,1.11901,2"))
        List<MidOhlc> ohlcs = ohlcStream.collect(Collectors.toList())

        assert 1 == ohlcs.size()
        assert Instant.parse("2019-08-13T06:18:15Z") == ohlcs[0].dateTime
        assert 1.11898 == ohlcs[0].open.value
        assert 1.11901 == ohlcs[0].high.value
        assert 1.11898 == ohlcs[0].low.value
        assert 1.11901 == ohlcs[0].close.value
        assert 2 == ohlcs[0].volume
    }

    @Test
    void testParseBidAsk_InputStream() {
        InputStream inputStream = this.class.getResourceAsStream("/EURUSD-S5-bid-ask.csv")
        Stream<BidAskOhlc> ohlcStream = csvParser.parse(inputStream)
        List<BidAskOhlc> ohlcs = ohlcStream.collect(Collectors.toList())

        assert 3 == ohlcs.size()
        assert Instant.parse("2019-08-13T06:18:15Z") == ohlcs[0].dateTime
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
    void testParseMid_InputStream() {
        InputStream inputStream = this.class.getResourceAsStream("/EURUSD-S5-mid.csv")
        Stream<MidOhlc> ohlcStream = csvParser.parse(inputStream)
        List<MidOhlc> ohlcs = ohlcStream.collect(Collectors.toList())

        assert 3 == ohlcs.size()
        assert Instant.parse("2019-08-13T06:18:15Z") == ohlcs[0].dateTime
        assert 1.11898 == ohlcs[0].open.value
        assert 1.11901 == ohlcs[0].high.value
        assert 1.11898 == ohlcs[0].low.value
        assert 1.11901 == ohlcs[0].close.value
        assert 2 == ohlcs[0].volume
    }
}
