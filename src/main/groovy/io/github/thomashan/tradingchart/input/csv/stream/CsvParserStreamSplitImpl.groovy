package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc

class CsvParserStreamSplitImpl<O extends Ohlc> implements CsvParserStream<O> {
    @Override
    String[] split(String string) {
        return string.split(",")
    }
}
