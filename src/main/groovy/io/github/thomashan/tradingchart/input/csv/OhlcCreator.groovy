package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.ohlc.MidOhlc
import io.github.thomashan.tradingchart.ohlc.Ohlc
import io.github.thomashan.tradingchart.price.BidAsk
import io.github.thomashan.tradingchart.price.Mid
import io.github.thomashan.tradingchart.price.Price

import java.time.ZonedDateTime
import java.util.function.BiFunction

class OhlcCreator {
    private OhlcCreator() {
        throw new AssertionError("not instantiable")
    }

    static <P extends Price> BiFunction<String[], Map<String, Integer>, Ohlc<P>> getCreator(String[] row) {
        switch (row.length) {
            case 10:
                return CREATE_BID_ASK
            case 6:
                return CREATE_MID
            default:
                throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid")
        }
    }

    static final BiFunction<String[], Map<String, Integer>, Ohlc<BidAsk>> CREATE_BID_ASK = (String[] strings, Map<String, Integer> stringIntegerMap) ->
            BidAskOhlc.of(ZonedDateTime.parse(strings[stringIntegerMap["dateTime"]]),
                    BidAsk.of(strings[stringIntegerMap["openBid"]].toDouble(), strings[stringIntegerMap["openAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["highBid"]].toDouble(), strings[stringIntegerMap["highAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["lowBid"]].toDouble(), strings[stringIntegerMap["lowAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["closeBid"]].toDouble(), strings[stringIntegerMap["closeAsk"]].toDouble()),
                    strings[stringIntegerMap["volume"]].toDouble())


    static final BiFunction<String[], Map<String, Integer>, Ohlc<BidAsk>> CREATE_MID = (String[] strings, Map<String, Integer> stringIntegerMap) ->
            MidOhlc.of(ZonedDateTime.parse(strings[stringIntegerMap["dateTime"]]),
                    Mid.of(strings[stringIntegerMap["open"]].toDouble()),
                    Mid.of(strings[stringIntegerMap["high"]].toDouble()),
                    Mid.of(strings[stringIntegerMap["low"]].toDouble()),
                    Mid.of(strings[stringIntegerMap["close"]].toDouble()),
                    strings[stringIntegerMap["volume"]].toDouble())
}
