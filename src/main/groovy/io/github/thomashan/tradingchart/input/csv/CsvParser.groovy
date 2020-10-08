package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.BidAsk
import io.github.thomashan.tradingchart.price.Price

import java.time.ZonedDateTime
import java.util.function.BiFunction
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

    default Map<String, Integer> getDefaultHeaders() {
        ["dateTime": 0,
         "openAsk" : 1,
         "openBid" : 2,
         "highAsk" : 3,
         "highBid" : 4,
         "lowAsk"  : 5,
         "lowBid"  : 6,
         "closeAsk": 7,
         "closeBid": 8,
         "volume"  : 9,
        ]
    }

    default BiFunction<String[], Map<String, Integer>, Ohlc<BidAsk>> getCreateOhlcFunction() {
        return { String[] strings, Map<String, Integer> stringIntegerMap ->
            return Ohlc.of(ZonedDateTime.parse(strings[stringIntegerMap["dateTime"]]),
                    BidAsk.of(strings[stringIntegerMap["openBid"]].toDouble(), strings[stringIntegerMap["openAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["highBid"]].toDouble(), strings[stringIntegerMap["highAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["lowBid"]].toDouble(), strings[stringIntegerMap["lowAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["closeBid"]].toDouble(), strings[stringIntegerMap["closeAsk"]].toDouble()),
                    strings[stringIntegerMap["volume"]].toDouble())
        }
    }
}
