package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

public class CsvParserStreamSplit<O extends Ohlc<O, ?>> extends BaseCsvParserStream<O> {
    @Override
    public String[] split(String string) {
        return string.split(",");
    }
}