package io.github.thomashan.tradingchart.input.csv

import org.junit.jupiter.api.Test

class CsvHeaderTest {
    @Test
    void testGetCsvHeader_ReturnsSameInstance() {
        CsvHeader csvHeader = CsvHeader.getCsvHeader(["header": 1])

        assert csvHeader === CsvHeader.getCsvHeader(["header": 1])
        assert csvHeader !== CsvHeader.getCsvHeader(["header": 2])
    }
}
