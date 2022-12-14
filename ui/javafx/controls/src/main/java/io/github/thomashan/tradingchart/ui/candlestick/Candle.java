package io.github.thomashan.tradingchart.ui.candlestick;

import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

public class Candle extends Group {
    private static final double SMALLEST_HEIGHT = 1;
    private Line highLowLine = new Line();
    private Region bar = new Region();
    private String seriesStyleClass;
    private String dataStyleClass;
    private boolean openAboveClose = true;

    public Candle(String seriesStyleClass, String dataStyleClass) {
        setAutoSizeChildren(false);
        getChildren().addAll(highLowLine, bar);
        this.seriesStyleClass = seriesStyleClass;
        this.dataStyleClass = dataStyleClass;
        updateStyleClasses();
    }

    public void update(double openMinusHigh, double openMinusLow, double openMinusClose, double barWidth, double halfBarWidth) {
        this.openAboveClose = openMinusClose < 0;
        highLowLine.setStartY(openMinusHigh);
        highLowLine.setEndY(openMinusLow);
        double barHeight = openMinusClose == 0 ? SMALLEST_HEIGHT : openMinusClose;
        if (openAboveClose) {
            bar.resizeRelocate(-halfBarWidth, 0, barWidth, -barHeight);
        } else {
            bar.resizeRelocate(-halfBarWidth, -openMinusClose, barWidth, barHeight);
        }

        updateStyleClasses();
    }

    public void setSeriesAndDataStyleClasses(String seriesStyleClass, String dataStyleClass) {
        this.seriesStyleClass = seriesStyleClass;
        this.dataStyleClass = dataStyleClass;
        updateStyleClasses();
    }

    private void updateStyleClasses() {
        final String aboveClose = openAboveClose ? "open-above-close" : "close-above-open";
        getStyleClass().setAll("candlestick-candle", seriesStyleClass, dataStyleClass);
        highLowLine.getStyleClass().setAll("candlestick-line", seriesStyleClass, dataStyleClass, aboveClose);
        bar.getStyleClass().setAll("candlestick-bar", seriesStyleClass, dataStyleClass, aboveClose);
    }

    public Region getBar() {
        return bar;
    }
}
