package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;
import io.github.thomashan.tradingchart.price.BidAsk;

public class CsvParserStreamIndexOfImplJmhTest extends CsvParserJmhTestCase<CsvParserStreamIndexOfImpl<BidAsk>> {
    @Override
    public CsvParserStreamIndexOfImpl<BidAsk> getCsvParser() {
        return new CsvParserStreamIndexOfImpl<>();
    }
}
