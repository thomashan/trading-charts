package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class BaseCsvParserStream<O extends Ohlc<?>> implements CsvParserStream<O> {
    private Map<String, Integer> headerIndexes;
    private BiFunction<String[], Map<String, Integer>, O> createFunction;

    @Override
    public Map<String, Integer> getHeaderIndexes() {
        return headerIndexes;
    }

    @Override
    public void setHeaderIndexes(Map<String, Integer> headerIndexes) {
        this.headerIndexes = headerIndexes;
    }

    @Override
    public BiFunction<String[], Map<String, Integer>, O> getCreateFunction() {
        return createFunction;
    }

    @Override
    public void setCreateFunction(BiFunction<String[], Map<String, Integer>, O> createFunction) {
        this.createFunction = createFunction;
    }
}
