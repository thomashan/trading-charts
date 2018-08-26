package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.domain.price.BidAsk;
import io.github.thomashan.tradingchart.domain.price.Mid;

import java.time.Instant;
import java.util.Map;
import java.util.function.BiFunction;

import static java.lang.Double.parseDouble;

public class OhlcCreator {
    private OhlcCreator() {
        throw new AssertionError("not instantiable");
    }

    @SuppressWarnings("unchecked")
    public static <O extends Ohlc<?>> BiFunction<String[], Map<String, Integer>, O> getCreator(String[] row) {
        switch (row.length) {
            case 10:
                return (BiFunction<String[], Map<String, Integer>, O>) CREATE_BID_ASK;
            case 6:
                return (BiFunction<String[], Map<String, Integer>, O>) CREATE_MID;
            default:
                throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid");
        }
    }

    public static final BiFunction<String[], Map<String, Integer>, BidAskOhlc> CREATE_BID_ASK = (String[] strings, Map<String, Integer> stringIntegerMap) ->
            // FIXME: parseDouble should not produce any garbage but the Double.parseDouble does produce garbage
            BidAskOhlc.of(Instant.parse(strings[stringIntegerMap.get("dateTime")]),
                    BidAsk.of(parseDouble(strings[stringIntegerMap.get("openBid")]), parseDouble(strings[stringIntegerMap.get("openAsk")])),
                    BidAsk.of(parseDouble(strings[stringIntegerMap.get("highBid")]), parseDouble(strings[stringIntegerMap.get("highAsk")])),
                    BidAsk.of(parseDouble(strings[stringIntegerMap.get("lowBid")]), parseDouble(strings[stringIntegerMap.get("lowAsk")])),
                    BidAsk.of(parseDouble(strings[stringIntegerMap.get("closeBid")]), parseDouble(strings[stringIntegerMap.get("closeAsk")])),
                    parseDouble(strings[stringIntegerMap.get("volume")]));

    public static final BiFunction<String[], Map<String, Integer>, Ohlc<?>> CREATE_MID = (String[] strings, Map<String, Integer> stringIntegerMap) ->
            // FIXME: parseDouble should not produce any garbage but the Double.parseDouble does produce garbage
            MidOhlc.of(Instant.parse(strings[stringIntegerMap.get("dateTime")]),
                    Mid.of(parseDouble(strings[stringIntegerMap.get("open")])),
                    Mid.of(parseDouble(strings[stringIntegerMap.get("high")])),
                    Mid.of(parseDouble(strings[stringIntegerMap.get("low")])),
                    Mid.of(parseDouble(strings[stringIntegerMap.get("close")])),
                    parseDouble(strings[stringIntegerMap.get("volume")]));
}
