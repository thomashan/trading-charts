module trading.charts.ui {
    requires javafx.controls;
    requires javafx.graphics;
    requires trading.charts.domain;
    requires trading.charts.util;

    exports io.github.thomashan.tradingchart.javafx.scene.chart;
    exports io.github.thomashan.tradingchart.ui.data;
}
