package io.github.thomashan.tradingchart.input.csv.univocity;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;

public class CsvParserUnivocityJmhTest extends CsvParserJmhTestCase<CsvParserUnivocity<BidAskOhlc>> {
    @Override
    public CsvParserUnivocity<BidAskOhlc> getCsvParser() {
        return new CsvParserUnivocity<>();
    }
}
