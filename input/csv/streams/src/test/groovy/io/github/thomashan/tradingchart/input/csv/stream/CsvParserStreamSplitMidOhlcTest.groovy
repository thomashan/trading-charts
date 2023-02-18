package io.github.thomashan.tradingchart.input.csv.stream


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.input.csv.testcase.CsvParserMidOhlcTestCase

class CsvParserStreamSplitMidOhlcTest implements CsvParserMidOhlcTestCase<CsvParserStreamSplit<MidOhlc>> {
    @Override
    CsvParserStreamSplit<MidOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserStreamSplit<>()
    }
}
