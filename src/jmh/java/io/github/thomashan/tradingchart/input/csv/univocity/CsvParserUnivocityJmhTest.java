package io.github.thomashan.tradingchart.input.csv.univocity;

import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;
import io.github.thomashan.tradingchart.price.BidAsk;

public class CsvParserUnivocityJmhTest extends CsvParserJmhTestCase<CsvParserUnivocity<BidAsk>> {
    @Override
    public CsvParserUnivocity<BidAsk> getCsvParser() {
        return new CsvParserUnivocity<>();
    }
}
