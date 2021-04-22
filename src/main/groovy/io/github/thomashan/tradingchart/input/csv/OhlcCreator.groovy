package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc
import io.github.thomashan.tradingchart.domain.price.BidAsk
import io.github.thomashan.tradingchart.domain.price.Mid

import java.time.Instant
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.ResolverStyle
import java.util.function.BiFunction

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

class OhlcCreator {
    public static DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(ISO_LOCAL_DATE_TIME)
            .parseLenient()
            .appendOffsetId()
            .parseStrict()
            .toFormatter(ResolverStyle.STRICT, IsoChronology.INSTANCE)

    private OhlcCreator() {
        throw new AssertionError(this.class.simpleName + " can't be instantiated")
    }

    static <O extends Ohlc> BiFunction<String[], Map<String, Integer>, O> getCreator(String[] row) {
        switch (row.length) {
            case 10:
                return CREATE_BID_ASK
            case 6:
                return CREATE_MID
            default:
                throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid")
        }
    }

    static final BiFunction<String[], Map<String, Integer>, BidAskOhlc> CREATE_BID_ASK = (String[] strings, Map<String, Integer> stringIntegerMap) ->
            BidAskOhlc.of(DATE_TIME_FORMATTER.parse(strings[stringIntegerMap["dateTime"]], Instant::from),
                    BidAsk.of(strings[stringIntegerMap["openBid"]].toDouble(), strings[stringIntegerMap["openAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["highBid"]].toDouble(), strings[stringIntegerMap["highAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["lowBid"]].toDouble(), strings[stringIntegerMap["lowAsk"]].toDouble()),
                    BidAsk.of(strings[stringIntegerMap["closeBid"]].toDouble(), strings[stringIntegerMap["closeAsk"]].toDouble()),
                    strings[stringIntegerMap["volume"]].toDouble())


    static final BiFunction<String[], Map<String, Integer>, MidOhlc> CREATE_MID = (String[] strings, Map<String, Integer> stringIntegerMap) ->
            MidOhlc.of(DATE_TIME_FORMATTER.parse(strings[stringIntegerMap["dateTime"]], Instant::from),
                    Mid.of(strings[stringIntegerMap["open"]].toDouble()),
                    Mid.of(strings[stringIntegerMap["high"]].toDouble()),
                    Mid.of(strings[stringIntegerMap["low"]].toDouble()),
                    Mid.of(strings[stringIntegerMap["close"]].toDouble()),
                    strings[stringIntegerMap["volume"]].toDouble())
}
