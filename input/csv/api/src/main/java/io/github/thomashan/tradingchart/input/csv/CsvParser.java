package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Uniform API to integrate various CSV parser implementations
 */
public interface CsvParser<O extends Ohlc<O, ?>> {
    void parse(InputStream inputStream, Consumer<O> consumer);
}
