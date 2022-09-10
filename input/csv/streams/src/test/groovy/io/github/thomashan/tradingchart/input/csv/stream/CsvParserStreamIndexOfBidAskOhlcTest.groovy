package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserBidAskOhlcTestCase

class CsvParserStreamIndexOfBidAskOhlcTest implements CsvParserBidAskOhlcTestCase<CsvParserStreamIndexOf<BidAskOhlc>> {
    @Override
    CsvParserStreamIndexOf<BidAskOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserStreamIndexOf<>()
    }
}
