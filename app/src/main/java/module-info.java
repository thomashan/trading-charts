module trading.charts.app {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires trading.charts.domain;
    requires trading.charts.ui;

    exports io.github.thomashan.trading.charts.app;
}
