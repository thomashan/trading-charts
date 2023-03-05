package io.github.thomashan.tradingchart.input.csv.univocity;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.testcase.CsvParserBwTestCase;
import io.github.thomashan.tradingchart.input.csv.OhlcCreatorNoGc;

public class CsvParserUnivocityBwTest extends CsvParserBwTestCase<BidAskOhlc> {
    @Override
    protected CsvParser<BidAskOhlc> createCsvParser() {
        return new CsvParserUnivocity<>(true, OhlcCreatorNoGc.CREATE_BID_ASK);
    }
}
