package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.BidAsk
import io.github.thomashan.tradingchart.price.Price

import java.time.ZonedDateTime
import java.util.function.Function
import java.util.stream.Stream

/**
 * Uniform API to integrate various CSV parser implementations
 */
interface CsvParser<P extends Price> {
    /**
     * Interim api to do performance testing
     * @return {@link List<Ohlc>} should be {@link Stream<Ohlc>}
     */
    Stream<Ohlc<P>> parse(InputStream inputStream)

    Stream<Ohlc<P>> parse(Stream<String> inputRows)

    public static final Function<String[], Ohlc<BidAsk>> createOhlc = new Function<String[], Ohlc<BidAsk>>() {
        @Override
        Ohlc apply(String[] strings) {
            return Ohlc.of(ZonedDateTime.parse(strings[0]),
                    BidAsk.of(strings[2].toDouble(), strings[1].toDouble()),
                    BidAsk.of(strings[4].toDouble(), strings[3].toDouble()),
                    BidAsk.of(strings[6].toDouble(), strings[5].toDouble()),
                    BidAsk.of(strings[8].toDouble(), strings[7].toDouble()),
                    strings[9].toDouble())
        }
    }
}
