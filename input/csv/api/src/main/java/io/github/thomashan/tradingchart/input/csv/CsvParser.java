package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * Uniform API to integrate various CSV parser implementations
 */
public interface CsvParser<O extends Ohlc<O, ?>> {
    /**
     * Interim api to do performance testing
     *
     * @return {@link List <Ohlc>} should be {@link Stream <Ohlc>}
     */
    Stream<O> parse(InputStream inputStream);

    Stream<O> parse(Stream<String> inputRows);
}
