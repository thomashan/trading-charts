module trading.charts.persistence.chronicle {
    requires trading.charts.domain;
    requires trading.charts.persistence.api;
    requires chronicle.bytes;
    requires chronicle.queue;
    requires chronicle.wire;

    exports io.github.thomashan.tradingchart.persistence.chronicle;
}
