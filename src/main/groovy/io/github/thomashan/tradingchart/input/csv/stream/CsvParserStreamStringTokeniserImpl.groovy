package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc

class CsvParserStreamStringTokeniserImpl<O extends Ohlc> implements CsvParserStream<O> {
    @Override
    String[] split(String string) {
        StringTokenizer stringTokenizer = new StringTokenizer(string, ",")
        int size = stringTokenizer.countTokens()
        String[] result = new String[size]

        for (int i = 0; i < size; i++) {
            result[i] = stringTokenizer.nextToken()
        }

        return result
    }
}
