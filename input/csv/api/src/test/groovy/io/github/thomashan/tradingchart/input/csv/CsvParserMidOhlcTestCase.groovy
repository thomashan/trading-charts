package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.time.MutableInstant
import org.junit.jupiter.api.Test

import java.time.Instant

trait CsvParserMidOhlcTestCase<C extends CsvParser<MidOhlc>> extends CsvParserTestCase<MidOhlc, C> {
    @Test
    void testParseMidWithHeader_InputStream() {
        String midOhlcCsv = """dateTime,open,high,low,close,volume
2019-08-13T06:18:15Z,1.11898,1.11901,1.11898,1.11901,2
2019-08-13T06:18:30Z,1.118985,1.118985,1.118955,1.118955,2
2019-08-13T06:18:40Z,1.11898,1.11898,1.11898,1.11898,1
"""
        List<MidOhlc> ohlcs = parseMid(midOhlcCsv)
        verifyMidOhlc(ohlcs)
    }

    @Test
    void testParseMidWithoutHeader_InputStream() {
        this.csvParser = createCsvParser(false)
        String midOhlcCsv = """2019-08-13T06:18:15Z,1.11898,1.11901,1.11898,1.11901,2
2019-08-13T06:18:30Z,1.118985,1.118985,1.118955,1.118955,2
2019-08-13T06:18:40Z,1.11898,1.11898,1.11898,1.11898,1
"""
        List<MidOhlc> ohlcs = parseMid(midOhlcCsv)
        verifyMidOhlc(ohlcs)
    }

    private List<BidAskOhlc> parseMid(String input) {
        InputStream inputStream = createInputStream(input)
        List<MidOhlc> ohlcs = []
        csvParser.parse(inputStream, (MidOhlc ohlc) -> {
            ohlcs.add(ohlc.copy())
        })
        return ohlcs
    }

    private void verifyMidOhlc(List<MidOhlc> midOhlcs) {
        assert 3 == midOhlcs.size()
        assert MutableInstant.parse("2019-08-13T06:18:15Z") == midOhlcs[0].dateTime
        assert 1.11898 == midOhlcs[0].open.value
        assert 1.11901 == midOhlcs[0].high.value
        assert 1.11898 == midOhlcs[0].low.value
        assert 1.11901 == midOhlcs[0].close.value
        assert 2 == midOhlcs[0].volume
    }
}
