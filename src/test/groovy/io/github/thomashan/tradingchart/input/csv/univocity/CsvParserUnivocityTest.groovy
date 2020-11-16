package io.github.thomashan.tradingchart.input.csv.univocity

import io.github.thomashan.tradingchart.domain.price.BidAsk
import io.github.thomashan.tradingchart.input.csv.CsvParserTestCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled

class CsvParserUnivocityTest implements CsvParserTestCase<CsvParserUnivocity<BidAsk>> {
    @BeforeEach
    void setUp() {
        this.csvParser = new CsvParserUnivocity<>()
    }

    @Disabled
    @Override
    void testParseBidAsk_Stream() {

    }

    @Disabled
    @Override
    void testParseMid_Stream() {

    }
}
