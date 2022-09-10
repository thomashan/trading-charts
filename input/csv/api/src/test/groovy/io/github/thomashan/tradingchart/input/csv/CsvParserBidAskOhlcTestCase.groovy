package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import org.junit.jupiter.api.Test

import java.time.Instant

trait CsvParserBidAskOhlcTestCase<C extends CsvParser<BidAskOhlc>> extends CsvParserTestCase<BidAskOhlc, C> {
    @Test
    void testParseBidAskWithHeader_InputStream() {
        String bidAskCsv = """dateTime,openAsk,openBid,highAsk,highBid,lowAsk,lowBid,closeAsk,closeBid,volume
2019-08-13T06:18:15Z,1.11904,1.11892,1.11907,1.11895,1.11904,1.11892,1.11907,1.11895,2
2019-08-13T06:18:30Z,1.11905,1.11892,1.11905,1.11892,1.11902,1.11889,1.11902,1.11889,2
2019-08-13T06:18:40Z,1.11904,1.11892,1.11904,1.11892,1.11904,1.11892,1.11904,1.11892,1
"""
        List<BidAskOhlc> ohlcs = parseBidAsk(bidAskCsv)
        verifyBidAskOhlc(ohlcs)
    }

    @Test
    void testParseBidAskWithoutHeader_InputStream() {
        this.csvParser = createCsvParser(false)
        String bidAskCsv = """2019-08-13T06:18:15Z,1.11904,1.11892,1.11907,1.11895,1.11904,1.11892,1.11907,1.11895,2
2019-08-13T06:18:30Z,1.11905,1.11892,1.11905,1.11892,1.11902,1.11889,1.11902,1.11889,2
2019-08-13T06:18:40Z,1.11904,1.11892,1.11904,1.11892,1.11904,1.11892,1.11904,1.11892,1
"""
        List<BidAskOhlc> ohlcs = parseBidAsk(bidAskCsv)
        verifyBidAskOhlc(ohlcs)
    }

    private List<BidAskOhlc> parseBidAsk(String input) {
        InputStream inputStream = createInputStream(input)
        List<BidAskOhlc> ohlcs = []
        csvParser.parse(inputStream, (BidAskOhlc ohlc) -> {
            ohlcs.add(ohlc.copy())
        })
        return ohlcs
    }

    private void verifyBidAskOhlc(List<BidAskOhlc> bidAskOhlcs) {
        assert 3 == bidAskOhlcs.size()
        assert Instant.parse("2019-08-13T06:18:15Z") == bidAskOhlcs[0].dateTime
        assert 1.11904 == bidAskOhlcs[0].open.ask
        assert 1.11892 == bidAskOhlcs[0].open.bid
        assert 1.11907 == bidAskOhlcs[0].high.ask
        assert 1.11895 == bidAskOhlcs[0].high.bid
        assert 1.11904 == bidAskOhlcs[0].low.ask
        assert 1.11892 == bidAskOhlcs[0].low.bid
        assert 1.11907 == bidAskOhlcs[0].close.ask
        assert 1.11895 == bidAskOhlcs[0].close.bid
        assert 2 == bidAskOhlcs[0].volume
    }
}
