package io.github.thomashan.tradingchart.input.csv.stream


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserMidOhlcTestCase

class CsvParserStreamStringTokeniserMidOhlcTest implements CsvParserMidOhlcTestCase<CsvParserStreamStringTokeniser<MidOhlc>> {
    @Override
    CsvParserStreamStringTokeniser<MidOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserStreamStringTokeniser<>()
    }
}
