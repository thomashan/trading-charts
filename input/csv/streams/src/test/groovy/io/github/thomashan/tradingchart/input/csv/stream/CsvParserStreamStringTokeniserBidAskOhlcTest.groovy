package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserBidAskOhlcTestCase

class CsvParserStreamStringTokeniserBidAskOhlcTest implements CsvParserBidAskOhlcTestCase<CsvParserStreamStringTokeniser<BidAskOhlc>> {
    @Override
    CsvParserStreamStringTokeniser<BidAskOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserStreamStringTokeniser<>()
    }
}
