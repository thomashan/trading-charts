package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

import java.util.ArrayList;
import java.util.List;

public class CsvParserStreamIndexOfImpl<O extends Ohlc<O, ?>> extends BaseCsvParserStream<O> {
    private static final String comma = ",";
    private static final List<String> cache = new ArrayList<>();

    @Override
    public String[] split(String string) {
        cache.clear();
        int size = string.length();
        int start = 0;

        for (int indexOf = string.indexOf(comma); indexOf != -1; indexOf = string.indexOf(comma, start)) {
            cache.add(string.substring(start, indexOf));
            start = indexOf + 1;
        }
        if (start != size) {
            cache.add(string.substring(start, size));
        }
        return cache.toArray(new String[cache.size()]);
    }
}
