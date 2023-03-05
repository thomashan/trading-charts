package io.github.thomashan.tradingchart.input.csv.univocit

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.testcase.CsvParserBidAskOhlcTestCase
import io.github.thomashan.tradingchart.input.csv.univocity.CsvParserUnivocity

class CsvParserUnivocityBidAskOhlcTest implements CsvParserBidAskOhlcTestCase<CsvParserUnivocity<BidAskOhlc>> {
    @Override
    CsvParserUnivocity<BidAskOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserUnivocity<>(extractHeader)
    }
}
