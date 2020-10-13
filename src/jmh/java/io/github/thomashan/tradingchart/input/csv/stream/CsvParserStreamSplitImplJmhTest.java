package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;
import io.github.thomashan.tradingchart.price.BidAsk;

public class CsvParserStreamSplitImplJmhTest extends CsvParserJmhTestCase<CsvParserStreamSplitImpl<BidAsk>> {
    @Override
    public CsvParserStreamSplitImpl<BidAsk> getCsvParser() {
        return new CsvParserStreamSplitImpl<>();
    }
}
