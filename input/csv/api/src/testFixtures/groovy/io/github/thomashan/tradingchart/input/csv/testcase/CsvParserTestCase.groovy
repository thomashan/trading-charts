package io.github.thomashan.tradingchart.input.csv.testcase


import io.github.thomashan.tradingchart.domain.ohlc.Ohlc
import io.github.thomashan.tradingchart.input.csv.CsvParser
import org.junit.jupiter.api.BeforeEach

import java.nio.charset.StandardCharsets

trait CsvParserTestCase<O extends Ohlc<O, ?>, C extends CsvParser<O>> {
    C csvParser

    abstract C createCsvParser(boolean extractHeader)

    @BeforeEach
    void setUp() {
        this.csvParser = createCsvParser(true)
    }

    InputStream createInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8))
    }
}
