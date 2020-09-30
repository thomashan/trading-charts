package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.ohlc.Ohlc

import java.util.stream.Stream

/**
 * Uniform API to integrate various CSV parser implementations
 */
interface CsvParser {
    Stream<Ohlc> parse(InputStream inputStream)

    Stream<Ohlc> parse(Stream<String> inputRows)
}
