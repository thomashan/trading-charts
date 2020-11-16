package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc

import java.util.stream.Stream

/**
 * Uniform API to integrate various CSV parser implementations
 */
interface CsvParser<O extends Ohlc> {
    /**
     * Interim api to do performance testing
     * @return {@link List<Ohlc>} should be {@link Stream<Ohlc>}
     */
    abstract Stream<O> parse(InputStream inputStream)

    abstract Stream<O> parse(Stream<String> inputRows)
}
