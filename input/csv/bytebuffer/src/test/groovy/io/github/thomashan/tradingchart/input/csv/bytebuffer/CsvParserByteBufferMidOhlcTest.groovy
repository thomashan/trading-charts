package io.github.thomashan.tradingchart.input.csv.bytebuffer


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserMidOhlcTestCase

class CsvParserByteBufferMidOhlcTest implements CsvParserMidOhlcTestCase<CsvParserByteBuffer<MidOhlc>> {
    @Override
    CsvParserByteBuffer<MidOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserByteBuffer<>()
    }
}
