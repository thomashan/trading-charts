package io.github.thomashan.tradingchart.input.csv.charbuffer;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.testcase.CsvParserBwTestCase;
import io.github.thomashan.tradingchart.input.csv.DefaultCsvHeader;
import io.github.thomashan.tradingchart.input.csv.OhlcCreatorNoGc;

import static io.github.thomashan.tradingchart.input.csv.DefaultCsvHeader.Type.BID_ASK_OHLC;

public class CsvParserCharBufferBwTest extends CsvParserBwTestCase<BidAskOhlc> {
    @Override
    protected CsvParser<BidAskOhlc> createCsvParser() {
        return new CsvParserCharBuffer<>(OhlcCreatorNoGc.getBidAskOhlcCreator(DefaultCsvHeader.getDefaultHeaders(BID_ASK_OHLC)));
    }

    @Override
    protected long byteLimitPerIteration() {
        // FIXME: why are 40824 bytes used?
        return 40824;
    }
}
