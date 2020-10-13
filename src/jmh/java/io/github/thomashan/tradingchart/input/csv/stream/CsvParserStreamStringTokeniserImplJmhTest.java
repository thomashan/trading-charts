package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;
import io.github.thomashan.tradingchart.price.BidAsk;

public class CsvParserStreamStringTokeniserImplJmhTest extends CsvParserJmhTestCase<CsvParserStreamStringTokeniserImpl<BidAsk>> {
    @Override
    public CsvParserStreamStringTokeniserImpl<BidAsk> getCsvParser() {
        return new CsvParserStreamStringTokeniserImpl();
    }
}
