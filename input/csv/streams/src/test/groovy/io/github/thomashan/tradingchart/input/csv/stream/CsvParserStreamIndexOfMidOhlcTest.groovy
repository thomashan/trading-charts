package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserMidOhlcTestCase

class CsvParserStreamIndexOfMidOhlcTest implements CsvParserMidOhlcTestCase<CsvParserStreamIndexOf<MidOhlc>> {
    @Override
    CsvParserStreamIndexOf<MidOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserStreamIndexOf<>()
    }
}
