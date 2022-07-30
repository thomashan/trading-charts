package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.price.BidAsk
import io.github.thomashan.tradingchart.input.csv.CsvParserTestCase
import org.junit.jupiter.api.BeforeEach

class CsvParserStreamSplitImplTest implements CsvParserTestCase<CsvParserStreamSplitImpl<BidAsk>> {
    @BeforeEach
    void setUp() {
        this.csvParser = new CsvParserStreamSplitImpl<>()
    }
}
