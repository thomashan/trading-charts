package io.github.thomashan.tradingchart.input.csv.charbuffer

import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserMidOhlcTestCase

class CsvParserCharBufferMidTest implements CsvParserMidOhlcTestCase<CsvParserCharBuffer<MidOhlc>> {
    @Override
    CsvParserCharBuffer<MidOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserCharBuffer<>()
    }
}
