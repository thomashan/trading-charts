package io.github.thomashan.tradingchart.input.csv;

import java.util.Map;

public class DefaultHeader {
    private DefaultHeader() {
        throw new AssertionError("not instantiable");
    }

    public static final Map<String, Integer> VALUE_BID_ASK = Map.of("dateTime", 0,
            "openAsk", 1,
            "openBid", 2,
            "highAsk", 3,
            "highBid", 4,
            "lowAsk", 5,
            "lowBid", 6,
            "closeAsk", 7,
            "closeBid", 8,
            "volume", 9);

    public static final Map<String, Integer> VALUE_MID = Map.of("dateTime", 0,
            "open", 1,
            "high", 2,
            "low", 3,
            "close", 4,
            "volume", 5);

    public static Map<String, Integer> getDefaultHeaders(String[] row) {
        switch (row.length) {
            case 10:
                return VALUE_BID_ASK;
            case 6:
                return VALUE_MID;
            default:
                throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid");
        }
    }
}
