package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvHeader;

import java.util.StringTokenizer;
import java.util.function.BiFunction;

public class CsvParserStreamStringTokeniser<O extends Ohlc<O, ?>> extends BaseCsvParserStream<O> {
    public CsvParserStreamStringTokeniser() {
        this(null);
    }

    CsvParserStreamStringTokeniser(BiFunction<String[], CsvHeader, O> createFunction) {
        super(createFunction);
    }

    @Override
    public String[] split(String string) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, ",");
        int size = stringTokenizer.countTokens();
        String[] result = new String[size];

        for (int i = 0; i < size; i++) {
            result[i] = stringTokenizer.nextToken();
        }

        return result;
    }
}
