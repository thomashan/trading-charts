package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.OhlcData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.StyleableBooleanProperty;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

/**
 * Render a crosshair like guide across the chart
 */
public class CursorCrosshair<O extends OhlcData<O, ?>> {
    private final StyleableBooleanProperty domainCrosshairVisible;
    private final StyleableBooleanProperty rangeCrosshairVisible;
    private final Line verticalLine;
    private final Line horizontalLine;
    private final MutableInstantAxis xAxis;
    private final OhlcDataAxis<O> yAxis;
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final Region plotBackground;

    // FIXME: there is a crosshair flickering issue, which I think is due to re-render on mouse over the lines
    public CursorCrosshair(Line verticalLine, Line horizontalLine,
                           MutableInstantAxis xAxis, OhlcDataAxis<O> yAxis,
                           StyleableBooleanProperty domainCrosshairVisible, StyleableBooleanProperty rangeCrosshairVisible,
                           Region plotBackground) {
        verticalLine.getStrokeDashArray().addAll(10d, 10d);
        horizontalLine.getStrokeDashArray().addAll(10d, 10d);
        verticalLine.setStrokeWidth(1);
        horizontalLine.setStrokeWidth(1);
        this.verticalLine = verticalLine;
        this.horizontalLine = horizontalLine;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.domainCrosshairVisible = domainCrosshairVisible;
        this.rangeCrosshairVisible = rangeCrosshairVisible;
        this.plotBackground = plotBackground;
        plotBackground.setOnMouseExited(mouseEvent -> {
            verticalLine.visibleProperty().set(false);
            horizontalLine.visibleProperty().set(false);
        });
        plotBackground.setOnMouseEntered(mouseEvent -> {
            verticalLine.visibleProperty().set(rangeCrosshairVisible.get());
            horizontalLine.visibleProperty().set(domainCrosshairVisible.get());
        });

        plotBackground.setOnMouseMoved(mouseEvent -> {
            double labelHeight = 10;
            verticalLine.setStartX(mouseEvent.getX() + yAxis.getWidth() + labelHeight); // FIXME: need to + label height
            verticalLine.setEndX(mouseEvent.getX() + yAxis.getWidth() + labelHeight);  // FIXME: need to + label height
            horizontalLine.setStartY(mouseEvent.getY() + labelHeight); // FIXME: need to + label height
            horizontalLine.setEndY(mouseEvent.getY() + labelHeight); // FIXME: need to + label height
        });
        DoubleBinding xValueBinding = Bindings.createDoubleBinding(() -> plotBackground.getWidth() + yAxis.getWidth(), plotBackground.widthProperty(), xAxis.widthProperty());
        DoubleBinding yValueBinding = Bindings.createDoubleBinding(() -> plotBackground.getHeight() + xAxis.getHeight(), plotBackground.heightProperty(), xAxis.heightProperty());
        verticalLine.endYProperty().bind(yValueBinding);
        horizontalLine.endXProperty().bind(xValueBinding);
    }
}
