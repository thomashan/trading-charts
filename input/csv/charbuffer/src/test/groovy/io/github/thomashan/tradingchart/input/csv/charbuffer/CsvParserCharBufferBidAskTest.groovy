package io.github.thomashan.tradingchart.input.csv.charbuffer

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.testcase.CsvParserBidAskOhlcTestCase

class CsvParserCharBufferBidAskTest implements CsvParserBidAskOhlcTestCase<CsvParserCharBuffer<BidAskOhlc>>  {
    @Override
    CsvParserCharBuffer<BidAskOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserCharBuffer<>()
    }
}
