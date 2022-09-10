package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvHeader;

import java.util.function.BiFunction;

public class CsvParserStreamSplit<O extends Ohlc<O, ?>> extends BaseCsvParserStream<O> {
    public CsvParserStreamSplit() {
        this(null);
    }

    CsvParserStreamSplit(BiFunction<String[], CsvHeader, O> createFunction) {
        super(createFunction);
    }

    @Override
    public String[] split(String string) {
        return string.split(",");
    }
}
