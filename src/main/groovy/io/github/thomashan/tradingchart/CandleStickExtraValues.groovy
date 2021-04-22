package io.github.thomashan.tradingchart

class CandleStickExtraValues {
    private static final String FORMAT = "CandleStickExtraValues{open=%f, high=%f, low=%f, close=%f, average=%f, volume=%f}"
    private double open
    private double high
    private double low
    private double close
    private double average

    CandleStickExtraValues(double close, double high,
                           double low, double average) {
        this.close = close
        this.high = high
        this.low = low
        this.average = average
    }

    double getClose() {
        return close
    }

    double getHigh() {
        return high
    }

    double getLow() {
        return low
    }

    double getAverage() {
        return average
    }

    @Override
    String toString() {
        return String.format(FORMAT, open, close, high, low, average)
    }
}
