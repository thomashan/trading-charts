package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.testcase.CsvParserBwTestCase;
import io.github.thomashan.tradingchart.input.csv.OhlcCreatorNoGc;

public class CsvParserStreamIndexBwTest extends CsvParserBwTestCase<BidAskOhlc> {
    @Override
    protected CsvParser<BidAskOhlc> createCsvParser() {
        return new CsvParserStreamIndexOf<>(OhlcCreatorNoGc.CREATE_BID_ASK);
    }

    @Override
    protected long byteLimitPerIteration() {
        return 55349504;
    }
}
