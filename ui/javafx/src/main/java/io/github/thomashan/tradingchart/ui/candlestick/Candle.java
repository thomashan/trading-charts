package io.github.thomashan.tradingchart.ui.candlestick;

import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

public class Candle extends Group {
    public static final double SMALLEST_BAR_HEIGHT = 1;
    private static final String CANDLESTICK_STYLE = "candlestick-candle";
    private static final String CANDLESTICK_LINE = "candlestick-line";
    private static final String CANDLESTICK_BAR = "candlestick-bar";
    private static final String OPEN_ABOVE_CLOSE = "open-above-close";
    private static final String CLOSE_ABOVE_OPEN = "close-above-open";
    private Line highLowLine = new Line();
    private Region bar = new Region();
    private String seriesStyleClass;
    private String dataStyleClass;
    private boolean openAboveClose = true;

    public Candle() {
        this(null, null);
    }

    public Candle(String seriesStyleClass, String dataStyleClass) {
        setAutoSizeChildren(false);
        getChildren().addAll(highLowLine, bar);
        this.seriesStyleClass = seriesStyleClass;
        this.dataStyleClass = dataStyleClass;
        updateStyleClasses();
    }

    public void update(double highMinusOpen, double lowMinusOpen, double closeMinusOpen, double barWidth,
                       MutableInstantData mutableInstantData) {
        checkPreconditions(highMinusOpen, lowMinusOpen, closeMinusOpen, mutableInstantData);
        openAboveClose = closeMinusOpen < 0;
        highLowLine.strokeWidthProperty().set(20);
        highLowLine.setStartY(highMinusOpen);
        highLowLine.setEndY(lowMinusOpen);
        double barHeight = closeMinusOpen == 0 ? SMALLEST_BAR_HEIGHT : closeMinusOpen;
        double halfBarWidth = barWidth / 2;
        if (openAboveClose) {
            bar.resizeRelocate(-halfBarWidth, 0, barWidth, -barHeight);
        } else {
            bar.resizeRelocate(-halfBarWidth, -closeMinusOpen, barWidth, barHeight);
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
        final String aboveClose = openAboveClose ? OPEN_ABOVE_CLOSE : CLOSE_ABOVE_OPEN;
        getStyleClass().setAll(CANDLESTICK_STYLE, seriesStyleClass, dataStyleClass);
        highLowLine.getStyleClass().setAll(CANDLESTICK_LINE, seriesStyleClass, dataStyleClass, aboveClose);
        bar.getStyleClass().setAll(CANDLESTICK_BAR, seriesStyleClass, dataStyleClass, aboveClose);
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
