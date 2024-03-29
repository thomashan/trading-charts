package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.time.MutableInstant
import org.junit.jupiter.api.Test

class OhlcCreatorTest {
    @Test
    void testBidAsk() {
        List<List<String>> inputs = [
            ["2019-08-13T06:20Z", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00Z", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00.000Z", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00.000000Z", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00.000000000Z", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
        ]

        for (List<String> row : inputs) {
            BidAskOhlc bidAskOhlc = createBidAskOhlc(row)
            assert MutableInstant.parse("2019-08-13T06:20:00.00Z") == bidAskOhlc.dateTime
            assert 1 == bidAskOhlc.open.bid
            assert 1 == bidAskOhlc.open.ask
            assert 1 == bidAskOhlc.high.bid
            assert 1 == bidAskOhlc.high.ask
            assert 1 == bidAskOhlc.low.bid
            assert 1 == bidAskOhlc.low.ask
            assert 1 == bidAskOhlc.close.bid
            assert 1 == bidAskOhlc.close.ask
            assert 1 == bidAskOhlc.volume
        }
    }

    @Test
    void testMid() {
        List<List<String>> inputs = [
            ["2019-08-13T06:20Z", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00Z", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00.000Z", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00.000000Z", "1", "1", "1", "1", "1"],
            ["2019-08-13T06:20:00.000000000Z", "1", "1", "1", "1", "1"],
        ]

        for (List<String> row : inputs) {
            MidOhlc midOhlc = createMidOhlc(row)
            assert MutableInstant.parse("2019-08-13T06:20:00.00Z") == midOhlc.dateTime
            assert 1 == midOhlc.open.value
            assert 1 == midOhlc.high.value
            assert 1 == midOhlc.low.value
            assert 1 == midOhlc.close.value
            assert 1 == midOhlc.volume
        }
    }

    private BidAskOhlc createBidAskOhlc(List<String> row) {
        return OhlcCreator.CREATE_BID_ASK.apply(row as String[], DefaultCsvHeader.VALUE_BID_ASK)
    }

    private MidOhlc createMidOhlc(List<String> row) {
        return OhlcCreator.CREATE_MID.apply(row as String[], DefaultCsvHeader.VALUE_MID)
    }
}
