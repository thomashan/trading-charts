package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;

public class CsvParserStreamIndexOfImplJmhTest extends CsvParserJmhTestCase<CsvParserStreamIndexOfImpl<BidAskOhlc>> {
    @Override
    public CsvParserStreamIndexOfImpl<BidAskOhlc> getCsvParser() {
        return new CsvParserStreamIndexOfImpl<>();
    }
}
