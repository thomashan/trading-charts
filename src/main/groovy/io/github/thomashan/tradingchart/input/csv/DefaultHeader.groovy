package io.github.thomashan.tradingchart.input.csv

class DefaultHeader {
    private DefaultHeader() {
        throw new AssertionError("not instantiable")
    }

    static final Map<String, Integer> VALUE_BID_ASK = ["dateTime": 0,
                                                       "openAsk" : 1,
                                                       "openBid" : 2,
                                                       "highAsk" : 3,
                                                       "highBid" : 4,
                                                       "lowAsk"  : 5,
                                                       "lowBid"  : 6,
                                                       "closeAsk": 7,
                                                       "closeBid": 8,
                                                       "volume"  : 9]

    static final Map<String, Integer> VALUE_MID = ["dateTime": 0,
                                                   "open"    : 1,
                                                   "high"    : 2,
                                                   "low"     : 3,
                                                   "close"   : 4,
                                                   "volume"  : 5]

    static Map<String, Integer> getDefaultHeaders(String[] row) {
        switch (row.length) {
            case 10:
                return VALUE_BID_ASK
            case 6:
                return VALUE_MID
            default:
                throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid")
        }
    }
}
