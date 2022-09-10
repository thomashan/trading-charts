package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;

public class CsvParserStreamIndexOfImplJmhTest extends CsvParserJmhTestCase<CsvParserStreamIndexOf<BidAskOhlc>> {
    @Override
    public CsvParserStreamIndexOf<BidAskOhlc> getCsvParser() {
        return new CsvParserStreamIndexOf<>();
    }
}
