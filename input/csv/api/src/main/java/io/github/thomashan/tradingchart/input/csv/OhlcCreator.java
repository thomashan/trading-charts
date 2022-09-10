package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;
import java.util.function.BiFunction;

import static java.lang.Double.parseDouble;

public class OhlcCreator {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .appendZoneOrOffsetId()
            .toFormatter();
    private static final ThreadLocal<BidAskOhlc> THREAD_LOCAL_BID_ASK_OHLC = ThreadLocal.withInitial(() -> BidAskOhlc.emptyFull());
    private static final ThreadLocal<MidOhlc> THREAD_LOCAL_MID_OHLC = ThreadLocal.withInitial(() -> MidOhlc.emptyFull());
    private static final Instant NOW = Instant.now();

    private OhlcCreator() {
        throw new AssertionError("not instantiable");
    }

    @SuppressWarnings("unchecked")
    public static <O extends Ohlc<O, ?>> BiFunction<String[], Map<String, Integer>, O> getCreator(String[] row) {
        switch (row.length) {
            case 10:
                return (BiFunction<String[], Map<String, Integer>, O>) CREATE_BID_ASK;
            case 6:
                return (BiFunction<String[], Map<String, Integer>, O>) CREATE_MID;
            default:
                throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid");
        }
    }

    public static final BiFunction<String[], Map<String, Integer>, BidAskOhlc> CREATE_BID_ASK = (String[] strings, Map<String, Integer> stringIntegerMap) -> {
        BidAskOhlc bidAskOhlc = THREAD_LOCAL_BID_ASK_OHLC.get();
        // FIXME: we should use MutableInstant instead of returning a new instance of Instant
        bidAskOhlc.dateTime = DATE_TIME_FORMATTER.parse(strings[stringIntegerMap.get("dateTime")], Instant::from);
        // FIXME: parseDouble should not produce any garbage but the Double.parseDouble does produce garbage
        bidAskOhlc.open.bid = parseDouble(strings[stringIntegerMap.get("openBid")]);
        bidAskOhlc.open.ask = parseDouble(strings[stringIntegerMap.get("openAsk")]);
        bidAskOhlc.high.bid = parseDouble(strings[stringIntegerMap.get("highBid")]);
        bidAskOhlc.high.ask = parseDouble(strings[stringIntegerMap.get("highAsk")]);
        bidAskOhlc.low.bid = parseDouble(strings[stringIntegerMap.get("lowBid")]);
        bidAskOhlc.low.ask = parseDouble(strings[stringIntegerMap.get("lowAsk")]);
        bidAskOhlc.close.bid = parseDouble(strings[stringIntegerMap.get("closeBid")]);
        bidAskOhlc.close.ask = parseDouble(strings[stringIntegerMap.get("closeAsk")]);
        bidAskOhlc.volume = parseDouble(strings[stringIntegerMap.get("volume")]);
        return bidAskOhlc;
    };


    public static final BiFunction<String[], Map<String, Integer>, Ohlc<MidOhlc, ?>> CREATE_MID = (String[] strings, Map<String, Integer> stringIntegerMap) -> {
        MidOhlc midOhlc = THREAD_LOCAL_MID_OHLC.get();
        // FIXME: we should use MutableInstant instead of returning a new instance of Instant
        midOhlc.dateTime = DATE_TIME_FORMATTER.parse(strings[stringIntegerMap.get("dateTime")], Instant::from);
        // FIXME: parseDouble should not produce any garbage but the Double.parseDouble does produce garbage
        midOhlc.open.value = parseDouble(strings[stringIntegerMap.get("open")]);
        midOhlc.high.value = parseDouble(strings[stringIntegerMap.get("high")]);
        midOhlc.low.value = parseDouble(strings[stringIntegerMap.get("low")]);
        midOhlc.close.value = parseDouble(strings[stringIntegerMap.get("close")]);
        midOhlc.volume = parseDouble(strings[stringIntegerMap.get("volume")]);
        return midOhlc;
    };
}
