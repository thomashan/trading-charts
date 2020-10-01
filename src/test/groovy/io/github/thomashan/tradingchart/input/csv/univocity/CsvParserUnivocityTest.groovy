package io.github.thomashan.tradingchart.input.csv.univocity

import io.github.thomashan.tradingchart.input.csv.CsvParserTestCase
import io.github.thomashan.tradingchart.price.BidAsk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled

class CsvParserUnivocityTest implements CsvParserTestCase<CsvParserUnivocity<BidAsk>> {
    @BeforeEach
    void setUp() {
        this.csvParser = new CsvParserUnivocity<>()
    }

    @Disabled
    @Override
    void testParse_Stream() {

    }
}
