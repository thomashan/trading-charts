package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.consumer.Consumers
import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.price.BidAsk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.Instant
import java.util.stream.Collectors

class NewCsvParserTest {
    private NewCsvParser csvParser

    @BeforeEach
    void setUp() {
        this.csvParser = new TestCsvParser()
    }

    @Test
    void testParse_ReadsHeaders() {
        BidAskOhlc bidAskOhlc = csvParser.parse("dateTime,openAsk,openBid,highAsk,highBid,lowAsk,lowBid,closeAsk,closeBid,volume")

        assert null == bidAskOhlc
        assert csvParser.headerIndexes == [
                0: "dateTime",
                1: "openAsk",
                2: "openBid",
                3: "highAsk",
                4: "highBid",
                5: "lowAsk",
                6: "lowBid",
                7: "closeAsk",
                8: "closeBid",
                9: "volume"
        ]
    }

    @Test
    void testParse() {
        String csvContent = """dateTime,openAsk,openBid,highAsk,highBid,lowAsk,lowBid,closeAsk,closeBid,volume
2019-08-13T06:18:15Z,1.11904,1.11892,1.11907,1.11895,1.11904,1.11892,1.11907,1.11895,2
2019-08-13T06:18:20Z,1.12004,1.11992,1.12007,1.11995,1.12004,1.11992,1.12007,1.11995,10
"""
        List<String> lines = csvContent.lines().collect(Collectors.toList())

        csvParser.parse(lines[0])
        assert csvParser.headerIndexes == [
                0: "dateTime",
                1: "openAsk",
                2: "openBid",
                3: "highAsk",
                4: "highBid",
                5: "lowAsk",
                6: "lowBid",
                7: "closeAsk",
                8: "closeBid",
                9: "volume"
        ]

        csvParser.parse(lines[1])
        assert csvParser.ohlc == BidAskOhlc.of(Instant.parse("2019-08-13T06:18:15Z"),
                BidAsk.of(1.11892, 1.11904),
                BidAsk.of(1.11895, 1.11907),
                BidAsk.of(1.11892, 1.1904),
                BidAsk.of(1.11895, 1.19007),
                2
        )

        csvParser.parse(lines[2])
        assert csvParser.ohlc == BidAskOhlc.of(Instant.parse("2019-08-13T06:18:20Z"),
                BidAsk.of(1.11992, 1.12004),
                BidAsk.of(1.11995, 1.12007),
                BidAsk.of(1.11992, 1.12004),
                BidAsk.of(1.11995, 1.12007),
                10
        )
    }

    @Test
    void testParse_WrongNumberOfFields() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException, () ->
                csvParser.parse("dateTime,openAsk,openBid,highAsk,highBid,lowAsk,lowBid,closeAsk,closeBid,volume,extraField")
        )

        assert exception.getMessage() == "expected 10 fields but got 11"
    }

    @Test
    void testParse_WrongFields() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException, () ->
                csvParser.parse("dateTime,openAsk,openBid,highAsk,highBid,lowAsk,lowBid,closeAsk,closeBid,extraField")
        )

        assert exception.getMessage() == "unexpected columns, please name the columns " + csvParser.createSetter().keySet().join(",")
    }

    private static class TestCsvParser implements BidAskOhlcCsvParser {
        TestCsvParser() {
            this.io_github_thomashan_tradingchart_input_csv_NewCsvParser__consumer = Consumers.noOps()
        }

        @Override
        BidAskOhlc emptyOhlc() {
            return BidAskOhlc.emptyFull()
        }
    }
}
