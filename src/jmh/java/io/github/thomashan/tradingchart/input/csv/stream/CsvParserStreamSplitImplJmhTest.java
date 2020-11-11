package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;

public class CsvParserStreamSplitImplJmhTest extends CsvParserJmhTestCase<CsvParserStreamSplitImpl<BidAskOhlc>> {
    @Override
    public CsvParserStreamSplitImpl<BidAskOhlc> getCsvParser() {
        return new CsvParserStreamSplitImpl<>();
    }
}
