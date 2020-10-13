package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.Price

import java.util.stream.Stream

/**
 * Uniform API to integrate various CSV parser implementations
 */
interface CsvParser<P extends Price> {
    /**
     * Interim api to do performance testing
     * @return {@link List<Ohlc>} should be {@link Stream<Ohlc>}
     */
    abstract Stream<Ohlc<P>> parse(InputStream inputStream)

    abstract Stream<Ohlc<P>> parse(Stream<String> inputRows)
}
