package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserBidAskOhlcTestCase

class CsvParserStreamSplitBidAskOhlcTest implements CsvParserBidAskOhlcTestCase<CsvParserStreamSplit<BidAskOhlc>> {
    @Override
    CsvParserStreamSplit<BidAskOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserStreamSplit<>()
    }
}
