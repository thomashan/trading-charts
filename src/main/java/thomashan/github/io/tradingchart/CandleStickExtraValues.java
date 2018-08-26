package thomashan.github.io.tradingchart;

/**
 * Data extra values for storing close, high and low.
 */
public class CandleStickExtraValues {
    private double open;
    private double high;
    private double low;
    private double close;
    private double average;

    public CandleStickExtraValues(double close, double high,
                                  double low, double average) {
        this.close = close;
        this.high = high;
        this.low = low;
        this.average = average;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getAverage() {
        return average;
    }

    private static final String FORMAT =
            "CandleStickExtraValues{close=%f, high=%f, low=%f, average=%f}";

    @Override
    public String toString() {
        return String.format(FORMAT, close, high, low, average);
    }
}
