package io.github.thomashan.tradingchart.input.csv;

import java.util.Map;

public class DefaultCsvHeader {
    private DefaultCsvHeader() {
        throw new AssertionError("not instantiable");
    }

    private static final CsvHeader VALUE_BID_ASK = CsvHeader.getCsvHeader(Map.of("dateTime", 0,
            "openAsk", 1,
            "openBid", 2,
            "highAsk", 3,
            "highBid", 4,
            "lowAsk", 5,
            "lowBid", 6,
            "closeAsk", 7,
            "closeBid", 8,
            "volume", 9));

    private static final CsvHeader VALUE_MID = CsvHeader.getCsvHeader(Map.of("dateTime", 0,
            "open", 1,
            "high", 2,
            "low", 3,
            "close", 4,
            "volume", 5));

    public static CsvHeader getDefaultHeaders(String[] row) {
        return getDefaultHeaders(row.length);
    }

    public static CsvHeader getDefaultHeaders(int numberOfColumns) {
        return switch (numberOfColumns) {
            case 10 -> VALUE_BID_ASK;
            case 6 -> VALUE_MID;
            default -> throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid");
        };
    }

    public static CsvHeader getDefaultHeaders(Type type) {
        return switch (type) {
            case BID_ASK_OHLC -> VALUE_BID_ASK;
            case MID_OHLC -> VALUE_MID;
        };
    }

    public enum Type {
        BID_ASK_OHLC, MID_OHLC
    }
}
