module trading.charts.persistence.api {
    requires transitive trading.charts.domain;

    exports io.github.thomashan.tradingchart.persistence;
    exports io.github.thomashan.tradingchart.persistence.domain.ohlc;
}
