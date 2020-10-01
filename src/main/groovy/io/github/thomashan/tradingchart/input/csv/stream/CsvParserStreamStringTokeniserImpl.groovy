package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.price.Price

class CsvParserStreamStringTokeniserImpl<P extends Price> implements CsvParserStream<P> {
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
