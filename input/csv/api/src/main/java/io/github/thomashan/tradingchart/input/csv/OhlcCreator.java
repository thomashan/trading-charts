package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.MidOhlc;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.lang.DoubleParser;
import io.github.thomashan.tradingchart.time.MutableInstant;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.function.BiFunction;

public class OhlcCreator {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral('T')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .appendZoneOrOffsetId()
            .toFormatter();
    private static final ThreadLocal<BidAskOhlc> THREAD_LOCAL_BID_ASK_OHLC = ThreadLocal.withInitial(() -> BidAskOhlc.emptyFull());
    private static final ThreadLocal<MidOhlc> THREAD_LOCAL_MID_OHLC = ThreadLocal.withInitial(() -> MidOhlc.emptyFull());

    private OhlcCreator() {
        throw new AssertionError("not instantiable");
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

    private static BiFunction<Integer, CharSequence, BidAskOhlc> getBidAskOhlcCreator(CsvHeader csvHeader) {
        return (columnNumber, value) -> {
            BidAskOhlc bidAskOhlc = THREAD_LOCAL_BID_ASK_OHLC.get();
            String columnName = csvHeader.getIndexToColumnName().get(columnNumber);
            switch (columnName) {
                case "dateTime" -> bidAskOhlc.dateTime = DATE_TIME_FORMATTER.parse(value, MutableInstant::from);
                case "openBid" -> bidAskOhlc.open.bid = DoubleParser.parseApprox(value);
                case "openAsk" -> bidAskOhlc.open.ask = DoubleParser.parseApprox(value);
                case "highBid" -> bidAskOhlc.high.bid = DoubleParser.parseApprox(value);
                case "highAsk" -> bidAskOhlc.high.ask = DoubleParser.parseApprox(value);
                case "lowBid" -> bidAskOhlc.low.bid = DoubleParser.parseApprox(value);
                case "lowAsk" -> bidAskOhlc.low.ask = DoubleParser.parseApprox(value);
                case "closeBid" -> bidAskOhlc.close.bid = DoubleParser.parseApprox(value);
                case "closeAsk" -> bidAskOhlc.close.ask = DoubleParser.parseApprox(value);
                case "volume" -> bidAskOhlc.volume = DoubleParser.parseApprox(value);
            }
            return bidAskOhlc;
        };
    }

    private static BiFunction<Integer, CharSequence, MidOhlc> getMidOhlcCreator(CsvHeader csvHeader) {
        return (columnNumber, value) -> {
            MidOhlc midOhlc = THREAD_LOCAL_MID_OHLC.get();
            String columnName = csvHeader.getIndexToColumnName().get(columnNumber);
            switch (columnName) {
                case "dateTime" -> midOhlc.dateTime = DATE_TIME_FORMATTER.parse(value, MutableInstant::from);
                case "open" -> midOhlc.open.value = DoubleParser.parseApprox(value);
                case "high" -> midOhlc.high.value = DoubleParser.parseApprox(value);
                case "low" -> midOhlc.low.value = DoubleParser.parseApprox(value);
                case "close" -> midOhlc.close.value = DoubleParser.parseApprox(value);
                case "volume" -> midOhlc.volume = DoubleParser.parseApprox(value);
            }
            return midOhlc;
        };
    }

    public static final BiFunction<String[], CsvHeader, BidAskOhlc> CREATE_BID_ASK = (row, csvHeader) -> {
        BidAskOhlc bidAskOhlc = THREAD_LOCAL_BID_ASK_OHLC.get();
        bidAskOhlc.dateTime = DATE_TIME_FORMATTER.parse(row[csvHeader.getColumnNameToIndex().get("dateTime")], MutableInstant::from);
        bidAskOhlc.open.bid = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("openBid")]);
        bidAskOhlc.open.ask = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("openAsk")]);
        bidAskOhlc.high.bid = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("highBid")]);
        bidAskOhlc.high.ask = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("highAsk")]);
        bidAskOhlc.low.bid = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("lowBid")]);
        bidAskOhlc.low.ask = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("lowAsk")]);
        bidAskOhlc.close.bid = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("closeBid")]);
        bidAskOhlc.close.ask = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("closeAsk")]);
        bidAskOhlc.volume = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("volume")]);
        return bidAskOhlc;
    };


    public static final BiFunction<String[], CsvHeader, MidOhlc> CREATE_MID = (row, csvHeader) -> {
        MidOhlc midOhlc = THREAD_LOCAL_MID_OHLC.get();
        midOhlc.dateTime = DATE_TIME_FORMATTER.parse(row[csvHeader.getColumnNameToIndex().get("dateTime")], MutableInstant::from);
        midOhlc.open.value = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("open")]);
        midOhlc.high.value = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("high")]);
        midOhlc.low.value = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("low")]);
        midOhlc.close.value = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("close")]);
        midOhlc.volume = DoubleParser.parseApprox(row[csvHeader.getColumnNameToIndex().get("volume")]);
        return midOhlc;
    };
}
