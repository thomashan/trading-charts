package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.consumer.Consumers
import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc

class BidAskCsvParserBwTest extends NewCsvParserBwTestCase<BidAskOhlc> {
    @Override
    NewCsvParser<BidAskOhlc> createCsvParser() {
        return new TestCsvParser()
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
