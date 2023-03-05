package io.github.thomashan.tradingchart.ui.candlestick;

import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
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

    public void update(double openMinusHigh, double openMinusLow, double openMinusClose, double barWidth,
                       MutableInstantData mutableInstantData) {
        checkPreconditions(openMinusHigh, openMinusLow, openMinusClose, mutableInstantData);
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

    private void checkPreconditions(double openMinusHigh, double openMinusLow, double openMinusClose,
                                    MutableInstantData mutableInstantData) {
        checkHighNotLowerThanLow(openMinusHigh, openMinusLow, mutableInstantData);
        checkHighNotLowerThanClose(openMinusHigh, openMinusClose, mutableInstantData);
        checkLowNotHigherThanClose(openMinusLow, openMinusClose, mutableInstantData);
    }

    private void checkLowNotHigherThanClose(double openMinusLow, double openMinusClose, MutableInstantData mutableInstantData) {
        if (openMinusClose > openMinusLow) {
            throw new IllegalArgumentException("Low is higher than close @" + mutableInstantData.toString());
        }
    }

    private void checkHighNotLowerThanClose(double openMinusHigh, double openMinusClose, MutableInstantData mutableInstantData) {
        if (openMinusHigh > openMinusClose) {
            throw new IllegalArgumentException("High is lower than close @" + mutableInstantData.toString());
        }
    }

    private void checkHighNotLowerThanLow(double openMinusHigh, double openMinusLow, MutableInstantData mutableInstantData) {
        if (openMinusHigh > openMinusLow) {
            throw new IllegalArgumentException("High is lower than low @" + mutableInstantData.toString());
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
