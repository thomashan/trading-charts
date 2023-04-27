package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.time.MutableInstant;

import java.util.function.BiFunction;

import static io.github.thomashan.tradingchart.lang.ObjectConstruction.NOT_INSTANTIABLE;

public class OhlcCreatorNoGc {
    private static final ThreadLocal<BidAskOhlc> THREAD_LOCAL_BID_ASK_OHLC = ThreadLocal.withInitial(() -> BidAskOhlc.emptyFull());
    private static final ThreadLocal<MidOhlc> THREAD_LOCAL_MID_OHLC = ThreadLocal.withInitial(() -> MidOhlc.emptyFull());
    private static final MutableInstant EPOCH = MutableInstant.EPOCH;

    private OhlcCreatorNoGc() {
        throw NOT_INSTANTIABLE;
    }

    @SuppressWarnings("unchecked")
    public static <O extends Ohlc<O, ?>> BiFunction<Integer, CharSequence, O> getCreator(int numberOfColumns, CsvHeader csvHeader) {
        return switch (numberOfColumns) {
            case 10 -> (BiFunction<Integer, CharSequence, O>) getBidAskOhlcCreator(csvHeader);
            case 6 -> (BiFunction<Integer, CharSequence, O>) getMidOhlcCreator(csvHeader);
            default -> throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid");
        };
    }

    @SuppressWarnings("unchecked")
    public static <O extends Ohlc<O, ?>> BiFunction<String[], CsvHeader, O> getCreator(String[] row) {
        return switch (row.length) {
            case 10 -> (BiFunction<String[], CsvHeader, O>) CREATE_BID_ASK;
            case 6 -> (BiFunction<String[], CsvHeader, O>) CREATE_MID;
            default -> throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid");
        };
    }

    public static BiFunction<Integer, CharSequence, BidAskOhlc> getBidAskOhlcCreator(CsvHeader csvHeader) {
        return (columnNumber, value) -> {
            BidAskOhlc bidAskOhlc = THREAD_LOCAL_BID_ASK_OHLC.get();
            String columnName = csvHeader.getIndexToColumnName().get(columnNumber);
            switch (columnName) {
                case "dateTime" -> bidAskOhlc.dateTime = EPOCH;
                case "openBid" -> bidAskOhlc.open.bid = 1;
                case "openAsk" -> bidAskOhlc.open.ask = 1;
                case "highBid" -> bidAskOhlc.high.bid = 1;
                case "highAsk" -> bidAskOhlc.high.ask = 1;
                case "lowBid" -> bidAskOhlc.low.bid = 1;
                case "lowAsk" -> bidAskOhlc.low.ask = 1;
                case "closeBid" -> bidAskOhlc.close.bid = 1;
                case "closeAsk" -> bidAskOhlc.close.ask = 1;
                case "volume" -> bidAskOhlc.volume = 1;
            }
            return bidAskOhlc;
        };
    }

    private static BiFunction<Integer, CharSequence, MidOhlc> getMidOhlcCreator(CsvHeader csvHeader) {
        return (columnNumber, value) -> {
            MidOhlc midOhlc = THREAD_LOCAL_MID_OHLC.get();
            String columnName = csvHeader.getIndexToColumnName().get(columnNumber);
            switch (columnName) {
                case "dateTime" -> midOhlc.dateTime = EPOCH;
                case "open" -> midOhlc.open.value = 1;
                case "high" -> midOhlc.high.value = 1;
                case "low" -> midOhlc.low.value = 1;
                case "close" -> midOhlc.close.value = 1;
                case "volume" -> midOhlc.volume = 1;
            }
            return midOhlc;
        };
    }

    public static final BiFunction<String[], CsvHeader, BidAskOhlc> CREATE_BID_ASK = (row, csvHeader) -> {
        BidAskOhlc bidAskOhlc = THREAD_LOCAL_BID_ASK_OHLC.get();
        bidAskOhlc.dateTime = EPOCH;
        bidAskOhlc.open.bid = 1;
        bidAskOhlc.open.ask = 1;
        bidAskOhlc.high.bid = 1;
        bidAskOhlc.high.ask = 1;
        bidAskOhlc.low.bid = 1;
        bidAskOhlc.low.ask = 1;
        bidAskOhlc.close.bid = 1;
        bidAskOhlc.close.ask = 1;
        bidAskOhlc.volume = 1;
        return bidAskOhlc;
    };


    public static final BiFunction<String[], CsvHeader, MidOhlc> CREATE_MID = (row, csvHeader) -> {
        MidOhlc midOhlc = THREAD_LOCAL_MID_OHLC.get();
        midOhlc.dateTime = EPOCH;
        midOhlc.open.value = 1;
        midOhlc.high.value = 1;
        midOhlc.low.value = 1;
        midOhlc.close.value = 1;
        midOhlc.volume = 1;
        return midOhlc;
    };
}
