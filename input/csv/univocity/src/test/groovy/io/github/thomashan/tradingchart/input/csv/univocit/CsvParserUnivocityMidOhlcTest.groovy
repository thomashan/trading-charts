package io.github.thomashan.tradingchart.input.csv.univocit


import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParserMidOhlcTestCase
import io.github.thomashan.tradingchart.input.csv.univocity.CsvParserUnivocity

class CsvParserUnivocityMidOhlcTest implements CsvParserMidOhlcTestCase<CsvParserUnivocity<MidOhlc>> {
    @Override
    CsvParserUnivocity<MidOhlc> createCsvParser(boolean extractHeader) {
        return new CsvParserUnivocity<>(extractHeader)
    }
}
