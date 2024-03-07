package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.OhlcData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

/**
 * Render a crosshair like guide across the chart
 */
public class CursorCrosshair<O extends OhlcData<O, ?>> {
    private final Line verticalLine;
    private final Line horizontalLine;
    private final MutableInstantAxis xAxis;
    private final OhlcDataAxis<O> yAxis;
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final Region plotBackground;

    public CursorCrosshair(MutableInstantAxis xAxis, OhlcDataAxis<O> yAxis,
                           Region plotBackground) {
        this.verticalLine = new Line();
        this.horizontalLine = new Line();
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.plotBackground = plotBackground;
        styleCursorLines();
        backgroundMouseEvents();
        DoubleBinding xValueBinding = Bindings.createDoubleBinding(() -> plotBackground.getWidth() + yAxis.getWidth(), plotBackground.widthProperty(), xAxis.widthProperty());
        DoubleBinding yValueBinding = Bindings.createDoubleBinding(() -> plotBackground.getHeight() + xAxis.getHeight(), plotBackground.heightProperty(), xAxis.heightProperty());
        verticalLine.endYProperty().bind(yValueBinding);
        horizontalLine.endXProperty().bind(xValueBinding);
    }

    public Line getVerticalLine() {
        return verticalLine;
    }

    public Line getHorizontalLine() {
        return horizontalLine;
    }

    private void styleCursorLines() {
        verticalLine.getStrokeDashArray().addAll(10d, 10d);
        horizontalLine.getStrokeDashArray().addAll(10d, 10d);
        verticalLine.setStrokeType(StrokeType.CENTERED);
        horizontalLine.setStrokeType(StrokeType.CENTERED);
        verticalLine.setStrokeLineCap(StrokeLineCap.BUTT);
        horizontalLine.setStrokeLineCap(StrokeLineCap.BUTT);
        // FIXME: pull out crosshair stroke width into styleable property
        verticalLine.setStrokeWidth(0.25);
        horizontalLine.setStrokeWidth(0.25);
    }

    private void backgroundMouseEvents() {
        plotBackground.setOnMouseExited(mouseEvent -> {
            if (!plotBackground.getBoundsInLocal().contains(mouseEvent.getX(), mouseEvent.getY())) {
                hideCrosshairLines();
            }
        });
        plotBackground.setOnMouseEntered(mouseEvent -> showCrosshairLines());
        plotBackground.setOnMouseMoved(mouseEvent -> {
            double labelHeight = 10;
            verticalLine.setStartX(mouseEvent.getX() + yAxis.getWidth() + labelHeight); // FIXME: need to + label height
            verticalLine.setEndX(mouseEvent.getX() + yAxis.getWidth() + labelHeight);  // FIXME: need to + label height
            horizontalLine.setStartY(mouseEvent.getY() + labelHeight); // FIXME: need to + label height
            horizontalLine.setEndY(mouseEvent.getY() + labelHeight); // FIXME: need to + label height
            showCrosshairLines();
        });
    }

    private void showCrosshairLines() {
        verticalLine.visibleProperty().set(true);
        horizontalLine.visibleProperty().set(true);
    }

    private void hideCrosshairLines() {
        verticalLine.visibleProperty().set(false);
        horizontalLine.visibleProperty().set(false);
    }
}
