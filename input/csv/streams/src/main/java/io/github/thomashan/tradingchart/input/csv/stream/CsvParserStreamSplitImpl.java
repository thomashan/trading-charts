package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

public class CsvParserStreamSplitImpl<O extends Ohlc<?>> extends BaseCsvParserStream<O> {
    @Override
    public String[] split(String string) {
        return string.split(",");
    }
}
