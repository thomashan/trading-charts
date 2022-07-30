package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;

public class CsvParserStreamStringTokeniserImplJmhTest extends CsvParserJmhTestCase<CsvParserStreamStringTokeniserImpl<BidAskOhlc>> {
    @Override
    public CsvParserStreamStringTokeniserImpl<BidAskOhlc> getCsvParser() {
        return new CsvParserStreamStringTokeniserImpl();
    }
}
