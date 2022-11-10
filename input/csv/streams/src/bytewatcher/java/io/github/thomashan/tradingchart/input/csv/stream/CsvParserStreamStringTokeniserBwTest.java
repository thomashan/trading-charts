package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.CsvParserBwTestCase;
import io.github.thomashan.tradingchart.input.csv.OhlcCreatorNoGc;

public class CsvParserStreamStringTokeniserBwTest extends CsvParserBwTestCase<BidAskOhlc> {
    @Override
    protected CsvParser<BidAskOhlc> createCsvParser() {
        return new CsvParserStreamStringTokeniser<>(OhlcCreatorNoGc.CREATE_BID_ASK);
    }
}
