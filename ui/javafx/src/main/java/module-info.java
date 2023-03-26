module trading.charts.ui {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires transitive trading.charts.domain;

    requires trading.charts.util;
    requires trading.charts.input.csv.api;
    requires trading.charts.input.csv.charbuffer;
    requires trading.charts.persistence.api;
    requires trading.charts.persistence.chronicle;

    exports io.github.thomashan.tradingchart.javafx.component.chart;
    exports io.github.thomashan.tradingchart.javafx.component.menu;
    exports io.github.thomashan.tradingchart.javafx.scene.chart;
    exports io.github.thomashan.tradingchart.ui.data;
    exports io.github.thomashan.tradingchart.javafx.component;
}
