package io.github.thomashan.tradingchart.ui.candlestick;

import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

public class Candle extends Group {
    public static final double SMALLEST_BAR_HEIGHT = 1;
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

    public void update(double openMinusHigh, double openMinusLow, double openMinusClose, double barWidth) {
        checkPreconditions(openMinusHigh, openMinusLow, openMinusClose);
        this.openAboveClose = openMinusClose < 0;
        highLowLine.setStartY(openMinusHigh);
        highLowLine.setEndY(openMinusLow);
        double barHeight = openMinusClose == 0 ? SMALLEST_BAR_HEIGHT : openMinusClose;
        double halfBarWidth = barWidth / 2;
        if (openAboveClose) {
            bar.resizeRelocate(-halfBarWidth, 0, barWidth, -barHeight);
        } else {
            bar.resizeRelocate(-halfBarWidth, -openMinusClose, barWidth, barHeight);
        }
        updateStyleClasses();
    }

    private void checkPreconditions(double openMinusHigh, double openMinusLow, double openMinusClose) {
        checkHighNotLowerThanLow(openMinusHigh, openMinusLow);
        checkHighNotLowerThanClose(openMinusHigh, openMinusClose);
        checkLowNotHigherThanClose(openMinusLow, openMinusClose);
        // FIXME: should we check for the barWidth?
    }

    private void checkLowNotHigherThanClose(double openMinusLow, double openMinusClose) {
        if (openMinusClose > openMinusLow) {
            throw new IllegalArgumentException("Low is higher than close");
        }
    }

    private void checkHighNotLowerThanClose(double openMinusHigh, double openMinusClose) {
        if (openMinusHigh > openMinusClose) {
            throw new IllegalArgumentException("High is lower than close");
        }
    }

    private void checkHighNotLowerThanLow(double openMinusHigh, double openMinusLow) {
        if (openMinusHigh > openMinusLow) {
            throw new IllegalArgumentException("High is lower than low");
        }
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

    public Line getHighLowLine() {
        return highLowLine;
    }

    public boolean isOpenAboveClose() {
        return openAboveClose;
    }
}
