package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.testcase.CsvParserBwTestCase;
import io.github.thomashan.tradingchart.input.csv.OhlcCreatorNoGc;

public class CsvParserStreamSplitBwTest extends CsvParserBwTestCase<BidAskOhlc> {
    @Override
    protected CsvParser<BidAskOhlc> createCsvParser() {
        return new CsvParserStreamSplit<>(OhlcCreatorNoGc.CREATE_BID_ASK);
    }

    @Override
    protected long byteLimitPerIteration() {
        return 61027104;
    }
}
