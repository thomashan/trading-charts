package io.github.thomashan.tradingchart.input.csv.univocity

import io.github.thomashan.tradingchart.input.csv.CsvParserTestCase
import org.junit.jupiter.api.BeforeEach

class CsvParserUnivocityTest implements CsvParserTestCase<CsvParserUnivocity> {
    @BeforeEach
    void setUp() {
        this.csvParser = new CsvParserUnivocity()
    }
}
