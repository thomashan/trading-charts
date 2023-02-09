module trading.charts.ui {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive trading.charts.domain;

    requires trading.charts.util;

    exports io.github.thomashan.tradingchart.javafx.scene.chart;
    exports io.github.thomashan.tradingchart.ui.data;
}
