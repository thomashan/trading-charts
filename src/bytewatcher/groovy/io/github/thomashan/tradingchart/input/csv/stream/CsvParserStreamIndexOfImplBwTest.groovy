package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.input.csv.CsvParser
import io.github.thomashan.tradingchart.input.csv.CsvParserBwTestCase

class CsvParserStreamIndexOfImplBwTest extends CsvParserBwTestCase<BidAskOhlc> {
    @Override
    CsvParser<BidAskOhlc> createCsvParser() {
        return new CsvParserStreamIndexOfImpl<BidAskOhlc>()
    }
}
