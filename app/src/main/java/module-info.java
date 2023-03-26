module trading.charts.app {
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires trading.charts.domain;
    requires trading.charts.ui;
    requires trading.charts.input.csv.api;
    requires trading.charts.input.csv.charbuffer;
    requires trading.charts.persistence.api;
    requires trading.charts.persistence.chronicle;
    requires trading.charts.util;

    exports io.github.thomashan.trading.charts.app;
}
