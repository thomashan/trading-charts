package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.price.Price

class CsvParserStreamIndexOfImpl<P extends Price> implements CsvParserStream<P> {
    private static final String comma = ","
    private static final List<String> cache = []

    @Override
    String[] split(String string) {
        cache.clear()
        int size = string.size()
        int start = 0

        for (int indexOf = string.indexOf(comma); indexOf != -1; indexOf = string.indexOf(comma, start)) {
            cache << string.substring(start, indexOf)
            start = indexOf + 1
        }

        if (start != size) {
            cache << string.substring(start, size)
        }

        return cache.toArray(new String[cache.size()])
    }
}
