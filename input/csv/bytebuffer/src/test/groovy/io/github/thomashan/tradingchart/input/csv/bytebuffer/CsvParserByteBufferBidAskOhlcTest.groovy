package io.github.thomashan.tradingchart.input.csv.bytebuffer

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserBidAskOhlcTestCase

class CsvParserByteBufferBidAskOhlcTest implements CsvParserBidAskOhlcTestCase<CsvParserByteBuffer<BidAskOhlc>> {
    @Override
    CsvParserByteBuffer<BidAskOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserByteBuffer<>()
    }
}
