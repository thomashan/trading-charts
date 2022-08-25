package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

import java.util.StringTokenizer;

public class CsvParserStreamStringTokeniserImpl<O extends Ohlc<O, ?>> extends BaseCsvParserStream<O> {
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
