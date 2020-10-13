package io.github.thomashan.tradingchart.input.csv.stream

import io.github.thomashan.tradingchart.price.Price

class CsvParserStreamSplitImpl<P extends Price> implements CsvParserStream<P> {
    @Override
    String[] split(String string) {
        return string.split(",")
    }
}
