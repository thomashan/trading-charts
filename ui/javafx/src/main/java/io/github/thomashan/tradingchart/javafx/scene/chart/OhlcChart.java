package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.javafx.collections.NonIterableChange;
import io.github.thomashan.tradingchart.ui.candlestick.Candle;
import io.github.thomashan.tradingchart.ui.data.Granularity;
import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
import io.github.thomashan.tradingchart.ui.data.OhlcData;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.ALTERNATIVE_COLUMN_FILL_VISIBLE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.ALTERNATIVE_ROW_FILL_VISIBLE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.CHART;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.DATA;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.HORIZONTAL_GRID_LINES_VISIBLE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.HORIZONTAL_ZERO_LINE_VISIBLE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.NODE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.VERTICAL_GRID_LINES_VISIBLE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChartConstants.VERTICAL_ZERO_LINE_VISIBLE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.SeriesConstants.NAME;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.CANDLESTICK_CHART_CSS;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.CHART_ALTERNATIVE_COLUMN_FILL;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.CHART_ALTERNATIVE_ROW_FILL;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.CHART_HORIZONTAL_GRID_LINES;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.CHART_HORIZONTAL_ZERO_LINE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.CHART_VERTICAL_GRID_LINES;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.CHART_VERTICAL_ZERO_LINE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.StyleClassConstants.PLOT_CONTENT;

// FIXME: replace all List<Double> with DoubleList
public class OhlcChart<O extends OhlcData<O, ?>> extends Chart {
    private static final String CURRENT_X = "currentX";
    private static final String CURRENT_Y = "currentY";

    // -------------- PRIVATE FIELDS -----------------------------------------------------------------------------------

    // to indicate which colors are being used for the series
    private final BitSet colorBits = new BitSet(8);
    static String DEFAULT_COLOR = "default-color";
    final Map<Series<O>, Integer> seriesColorMap = new HashMap<>();
    private boolean rangeValid = false;
    private final Line verticalZeroLine = new Line();
    private final Line horizontalZeroLine = new Line();
    private final Path verticalGridLines = new Path();
    private final Path horizontalGridLines = new Path();
    private final Path horizontalRowFill = new Path();
    private final Path verticalRowFill = new Path();
    private final Region plotBackground = new Region();
    private final Group plotArea = new Group() {
        @Override
        public void requestLayout() {
        } // suppress layout requests
    };
    private final Group plotContent = new Group();
    private final Rectangle plotAreaClip = new Rectangle();

    private final List<Series<O>> displayedSeries = new ArrayList<>();
    private Legend legend = new Legend();

    private final MutableInstantAxis xAxis;

    private final OhlcDataAxis<O> yAxis;

    private BooleanProperty verticalZeroLineVisible = new StyleableBooleanProperty(true) {
        @Override
        protected void invalidated() {
            requestChartLayout();
        }

        @Override
        public Object getBean() {
            return OhlcChart.this;
        }

        @Override
        public String getName() {
            return VERTICAL_ZERO_LINE_VISIBLE;
        }

        @Override
        public CssMetaData<OhlcChart<?>, Boolean> getCssMetaData() {
            return StyleableProperties.VERTICAL_ZERO_LINE_VISIBLE;
        }
    };

    private BooleanProperty alternativeColumnFillVisible = new StyleableBooleanProperty(false) {
        @Override
        protected void invalidated() {
            requestChartLayout();
        }

        @Override
        public Object getBean() {
            return OhlcChart.this;
        }

        @Override
        public String getName() {
            return ALTERNATIVE_COLUMN_FILL_VISIBLE;
        }

        @Override
        public CssMetaData<OhlcChart<?>, Boolean> getCssMetaData() {
            return StyleableProperties.ALTERNATIVE_COLUMN_FILL_VISIBLE;
        }
    };


    /**
     * If this is true and the horizontal axis has both positive and negative values then a additional axis line
     * will be drawn at the zero point
     *
     * @defaultValue true
     */
    private BooleanProperty horizontalZeroLineVisible = new StyleableBooleanProperty(true) {
        @Override
        protected void invalidated() {
            requestChartLayout();
        }

        @Override
        public Object getBean() {
            return OhlcChart.this;
        }

        @Override
        public String getName() {
            return HORIZONTAL_ZERO_LINE_VISIBLE;
        }

        @Override
        public CssMetaData<OhlcChart<?>, Boolean> getCssMetaData() {
            return StyleableProperties.HORIZONTAL_ZERO_LINE_VISIBLE;
        }
    };


    /**
     * True if horizontal grid lines should be drawn
     */
    private BooleanProperty horizontalGridLinesVisible = new StyleableBooleanProperty(true) {
        @Override
        protected void invalidated() {
            requestChartLayout();
        }

        @Override
        public Object getBean() {
            return OhlcChart.this;
        }

        @Override
        public String getName() {
            return HORIZONTAL_GRID_LINES_VISIBLE;
        }

        @Override
        public CssMetaData<OhlcChart<?>, Boolean> getCssMetaData() {
            return StyleableProperties.HORIZONTAL_GRID_LINE_VISIBLE;
        }
    };

    private boolean useChartContentMirroring = true;

    private final Pane chartContent = new Pane() {
        @Override
        protected void layoutChildren() {
            final double top = snappedTopInset();
            final double left = snappedLeftInset();
            final double bottom = snappedBottomInset();
            final double right = snappedRightInset();
            final double width = getWidth();
            final double height = getHeight();
            final double contentWidth = snapSizeX(width - (left + right));
            final double contentHeight = snapSizeY(height - (top + bottom));
            layoutChartChildren(snapPositionY(top), snapPositionX(left), contentWidth, contentHeight);
        }

        @Override
        public boolean usesMirroring() {
            return useChartContentMirroring;
        }
    };

    private final ChartLayoutAnimator animator = new ChartLayoutAnimator(chartContent);

    /**
     * Get the Y axis, by default it is along the left of the plot
     *
     * @return the Y axis of this chart
     */
    public OhlcDataAxis<O> getYAxis() {
        return yAxis;
    }

    private final ListChangeListener<Series<O>> seriesChanged = changedSeries -> {
        ObservableList<? extends Series<O>> series = changedSeries.getList();
        while (changedSeries.next()) {
            // RT-12069, linked list pointers should update when list is permutated.
            if (changedSeries.wasPermutated()) {
                displayedSeries.sort((o1, o2) -> series.indexOf(o2) - series.indexOf(o1));
            }

            if (changedSeries.getRemoved().size() > 0) updateLegend();

            Set<Series<O>> dupCheck = new HashSet<>(displayedSeries);
            dupCheck.removeAll(changedSeries.getRemoved());
            for (Series<O> d : changedSeries.getAddedSubList()) {
                if (!dupCheck.add(d)) {
                    throw new IllegalArgumentException("Duplicate series added");
                }
            }

            for (Series<O> s : changedSeries.getRemoved()) {
                s.setToRemove = true;
                seriesRemoved(s);
            }

            for (int i = changedSeries.getFrom(); i < changedSeries.getTo() && !changedSeries.wasPermutated(); i++) {
                final Series<O> s = changedSeries.getList().get(i);
                // add new listener to data
                s.setChart(OhlcChart.this);
                if (s.setToRemove) {
                    s.setToRemove = false;
                    s.getChart().seriesBeingRemovedIsAdded(s);
                }
                // update linkedList Pointers for series
                displayedSeries.add(s);
                // update default color style class
                int nextClearBit = colorBits.nextClearBit(0);
                colorBits.set(nextClearBit, true);
                s.defaultColorStyleClass = DEFAULT_COLOR + (nextClearBit % 8);
                seriesColorMap.put(s, nextClearBit % 8);
                // inform sub-classes of series added
                seriesAdded(s, i);
            }
            if (changedSeries.getFrom() < changedSeries.getTo()) updateLegend();
            seriesChanged(changedSeries);

        }
        // update axis ranges
        invalidateRange();
        // lay everything out
        requestChartLayout();
    };

    public final BooleanProperty horizontalGridLinesVisibleProperty() {
        return horizontalGridLinesVisible;
    }

    private ObjectProperty<ObservableList<Series<O>>> data = new ObjectPropertyBase<>() {
        private ObservableList<Series<O>> old;

        @Override
        protected void invalidated() {
            final ObservableList<Series<O>> current = getValue();
            if (current == old) return;
            int saveAnimationState = -1;
            // add remove listeners
            if (old != null) {
                old.removeListener(seriesChanged);
                // Set animated to false so we don't animate both remove and add
                // at the same time. RT-14163
                // RT-21295 - disable animated only when current is also not null.
                if (current != null && old.size() > 0) {
                    saveAnimationState = (old.get(0).getChart().getAnimated()) ? 1 : 2;
                    old.get(0).getChart().setAnimated(false);
                }
            }
            if (current != null) current.addListener(seriesChanged);
            // fire series change event if series are added or removed
            if (old != null || current != null) {
                final List<Series<O>> removed = (old != null) ? old : Collections.emptyList();
                final int toIndex = (current != null) ? current.size() : 0;
                // let series listener know all old series have been removed and new that have been added
                if (toIndex > 0 || !removed.isEmpty()) {
                    seriesChanged.onChanged(new NonIterableChange<>(0, toIndex, current) {
                        @Override
                        public List<Series<O>> getRemoved() {
                            return removed;
                        }

                        @Override
                        protected int[] getPermutation() {
                            return new int[0];
                        }
                    });
                }
            } else if (old != null && old.size() > 0) {
                // let series listener know all old series have been removed
                seriesChanged.onChanged(new NonIterableChange<>(0, 0, current) {
                    @Override
                    public List<Series<O>> getRemoved() {
                        return old;
                    }

                    @Override
                    protected int[] getPermutation() {
                        return new int[0];
                    }
                });
            }
            // restore animated on chart.
            if (current != null && current.size() > 0 && saveAnimationState != -1) {
                current.get(0).getChart().setAnimated((saveAnimationState == 1) ? true : false);
            }
            old = current;
        }

        public Object getBean() {
            return OhlcChart.this;
        }

        public String getName() {
            return DATA;
        }
    };


    /**
     * True if vertical grid lines should be drawn
     */
    private BooleanProperty verticalGridLinesVisible = new StyleableBooleanProperty(true) {
        @Override
        protected void invalidated() {
            requestChartLayout();
        }

        @Override
        public Object getBean() {
            return OhlcChart.this;
        }

        @Override
        public String getName() {
            return VERTICAL_GRID_LINES_VISIBLE;
        }

        @Override
        public CssMetaData<OhlcChart<?>, Boolean> getCssMetaData() {
            return StyleableProperties.VERTICAL_GRID_LINE_VISIBLE;
        }
    };

    private BooleanProperty alternativeRowFillVisible = new StyleableBooleanProperty(true) {
        @Override
        protected void invalidated() {
            requestChartLayout();
        }

        @Override
        public Object getBean() {
            return OhlcChart.this;
        }

        @Override
        public String getName() {
            return ALTERNATIVE_ROW_FILL_VISIBLE;
        }

        @Override
        public CssMetaData<OhlcChart<?>, Boolean> getCssMetaData() {
            return StyleableProperties.ALTERNATIVE_ROW_FILL_VISIBLE;
        }
    };

    /**
     * Constructs a XYChart given the two axes. The initial content for the chart
     * plot background and plot area that includes vertical and horizontal grid
     * lines and fills, are added.
     *
     * @param xAxis X Axis for this XY chart
     * @param yAxis Y Axis for this XY chart
     */
    public OhlcChart(MutableInstantAxis xAxis, OhlcDataAxis<O> yAxis) {
        this.xAxis = xAxis;
        if (xAxis.getSide() == null) {
            xAxis.setSide(Side.BOTTOM);
        }
        xAxis.setEffectiveOrientation(Orientation.HORIZONTAL);
        this.yAxis = yAxis;
        if (yAxis.getSide() == null) {
            yAxis.setSide(Side.LEFT);
        }
        yAxis.setEffectiveOrientation(Orientation.VERTICAL);
        // RT-23123 autoranging leads to charts incorrect appearance.
        xAxis.autoRangingProperty().addListener((ov, t, t1) -> updateAxisRange());
        yAxis.autoRangingProperty().addListener((ov, t, t1) -> updateAxisRange());
        // add initial content to chart content
        getChartChildren().addAll(plotBackground, plotArea, xAxis, yAxis);
        // We don't want plotArea or plotContent to autoSize or do layout
        plotArea.setAutoSizeChildren(false);
        plotContent.setAutoSizeChildren(false);
        // setup clipping on plot area
        plotAreaClip.setSmooth(false);
        plotArea.setClip(plotAreaClip);
        // add children to plot area
        plotArea.getChildren().addAll(verticalRowFill, horizontalRowFill, verticalGridLines, horizontalGridLines, verticalZeroLine, horizontalZeroLine, plotContent);
        // setup css style classes
        plotContent.getStyleClass().setAll(PLOT_CONTENT);
        plotBackground.getStyleClass().setAll(StyleClassConstants.CHART_PLOT_BACKGROUND);
        verticalRowFill.getStyleClass().setAll(CHART_ALTERNATIVE_COLUMN_FILL);
        horizontalRowFill.getStyleClass().setAll(CHART_ALTERNATIVE_ROW_FILL);
        verticalGridLines.getStyleClass().setAll(CHART_VERTICAL_GRID_LINES);
        horizontalGridLines.getStyleClass().setAll(CHART_HORIZONTAL_GRID_LINES);
        verticalZeroLine.getStyleClass().setAll(CHART_VERTICAL_ZERO_LINE);
        horizontalZeroLine.getStyleClass().setAll(CHART_HORIZONTAL_ZERO_LINE);
        // mark plotContent as unmanaged as its preferred size changes do not effect our layout
        plotContent.setManaged(false);
        plotArea.setManaged(false);
        // listen to animation on/off and sync to axis
        animatedProperty().addListener((valueModel, oldValue, newValue) -> {
            if (getXAxis() != null) {
                getXAxis().setAnimated(newValue);
            }
            if (getYAxis() != null) {
                getYAxis().setAnimated(newValue);
            }
        });
        setLegend(legend);
        final String candleStickChartCss = getClass().getClassLoader().getResource(CANDLESTICK_CHART_CSS).toExternalForm();
        getStylesheets().add(candleStickChartCss);
        setAnimated(false);
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);
    }

    @Override
    protected void layoutChartChildren(double top, double left, double width, double height) {
        if (getData() == null) {
            return;
        }
        if (!rangeValid) {
            rangeValid = true;
            if (getData() != null) {
                updateAxisRange();
            }
        }
        // snap top and left to pixels
        top = snapPositionY(top);
        left = snapPositionX(left);
        // get starting stuff
        final MutableInstantAxis xAxis = getXAxis();
        final ObservableList<Axis.TickMark<MutableInstantData>> xaTickMarks = xAxis.getTickMarks();
        final OhlcDataAxis<O> yAxis = getYAxis();
        final ObservableList<Axis.TickMark<O>> yaTickMarks = yAxis.getTickMarks();
        // check we have 2 axises and know their sides
        if (xAxis == null || yAxis == null) {
            return;
        }
        // try and work out width and height of axises
        double xAxisWidth = 0;
        double xAxisHeight = 30; // guess x axis height to start with
        double yAxisWidth = 0;
        double yAxisHeight = 0;
        // FIXME: why do we do this 5 times in a loop?
        for (int count = 0; count < 5; count++) {
            yAxisHeight = snapSizeY(height - xAxisHeight);
            if (yAxisHeight < 0) {
                yAxisHeight = 0;
            }
            yAxisWidth = yAxis.prefWidth(yAxisHeight);
            xAxisWidth = snapSizeX(width - yAxisWidth);
            if (xAxisWidth < 0) {
                xAxisWidth = 0;
            }
            double newXAxisHeight = xAxis.prefHeight(xAxisWidth);
            if (newXAxisHeight == xAxisHeight) {
                break;
            }
            xAxisHeight = newXAxisHeight;
        }
        // round axis sizes up to whole integers to snap to pixel
        xAxisWidth = Math.ceil(xAxisWidth);
        xAxisHeight = Math.ceil(xAxisHeight);
        yAxisWidth = Math.ceil(yAxisWidth);
        yAxisHeight = Math.ceil(yAxisHeight);
        // calc xAxis height
        double xAxisY = 0;
        switch (xAxis.getEffectiveSide()) {
            case TOP:
                xAxis.setVisible(true);
                xAxisY = top + 1;
                top += xAxisHeight;
                break;
            case BOTTOM:
                xAxis.setVisible(true);
                xAxisY = top + yAxisHeight;
        }

        // calc yAxis width
        double yAxisX = 0;
        switch (yAxis.getEffectiveSide()) {
            case LEFT:
                yAxis.setVisible(true);
                yAxisX = left + 1;
                left += yAxisWidth;
                break;
            case RIGHT:
                yAxis.setVisible(true);
                yAxisX = left + xAxisWidth;
        }
        // resize axises
        xAxis.resizeRelocate(left, xAxisY, xAxisWidth, xAxisHeight);
        yAxis.resizeRelocate(yAxisX, top, yAxisWidth, yAxisHeight);
        // When the chart is resized, need to specifically call out the axises
        // to lay out as they are unmanaged.
        xAxis.requestAxisLayout();
        xAxis.layout();
        yAxis.requestAxisLayout();
        yAxis.layout();
        // layout plot content
        layoutPlotChildren();
        // get axis zero points
        final double xAxisZero = xAxis.getZeroPosition();
        final double yAxisZero = yAxis.getZeroPosition();
        // position vertical and horizontal zero lines
        if (Double.isNaN(xAxisZero) || !isVerticalZeroLineVisible()) {
            verticalZeroLine.setVisible(false);
        } else {
            verticalZeroLine.setStartX(left + xAxisZero + 0.5);
            verticalZeroLine.setStartY(top);
            verticalZeroLine.setEndX(left + xAxisZero + 0.5);
            verticalZeroLine.setEndY(top + yAxisHeight);
            verticalZeroLine.setVisible(true);
        }
        if (Double.isNaN(yAxisZero) || !isHorizontalZeroLineVisible()) {
            horizontalZeroLine.setVisible(false);
        } else {
            horizontalZeroLine.setStartX(left);
            horizontalZeroLine.setStartY(top + yAxisZero + 0.5);
            horizontalZeroLine.setEndX(left + xAxisWidth);
            horizontalZeroLine.setEndY(top + yAxisZero + 0.5);
            horizontalZeroLine.setVisible(true);
        }
        // layout plot background
        plotBackground.resizeRelocate(left, top, xAxisWidth, yAxisHeight);
        // update clip
        plotAreaClip.setX(left);
        plotAreaClip.setY(top);
        plotAreaClip.setWidth(xAxisWidth + 1);
        plotAreaClip.setHeight(yAxisHeight + 1);
//        plotArea.setClip(new Rectangle(left, top, xAxisWidth, yAxisHeight));
        // position plot group, its origin is the bottom left corner of the plot area
        plotContent.setLayoutX(left);
        plotContent.setLayoutY(top);
        plotContent.requestLayout(); // Note: not sure this is right, maybe plotContent should be resizeable
        // update vertical grid lines
        verticalGridLines.getElements().clear();
        if (getVerticalGridLinesVisible()) {
            for (int i = 0; i < xaTickMarks.size(); i++) {
                final double x = xAxis.getDisplayPosition(xaTickMarks.get(i).getValue());
                if ((x != xAxisZero || !isVerticalZeroLineVisible()) && x > 0 && x <= xAxisWidth) {
                    verticalGridLines.getElements().add(new MoveTo(left + x + 0.5, top));
                    verticalGridLines.getElements().add(new LineTo(left + x + 0.5, top + yAxisHeight));
                }
            }
        }
        // update horizontal grid lines
        horizontalGridLines.getElements().clear();
        if (isHorizontalGridLinesVisible()) {
            for (int i = 0; i < yaTickMarks.size(); i++) {
                Axis.TickMark<O> tick = yaTickMarks.get(i);
                final double y = yAxis.getDisplayPosition(tick.getValue());
                if ((y != yAxisZero || !isHorizontalZeroLineVisible()) && y >= 0 && y < yAxisHeight) {
                    horizontalGridLines.getElements().add(new MoveTo(left, top + y + 0.5));
                    horizontalGridLines.getElements().add(new LineTo(left + xAxisWidth, top + y + 0.5));
                }
            }
        }
        // Note: is there a more efficient way to calculate horizontal and vertical row fills?
        // update vertical row fill
        verticalRowFill.getElements().clear();
        if (isAlternativeColumnFillVisible()) {
            // tick marks are not sorted so get all the positions and sort them
            final List<Double> tickPositionsPositive = new ArrayList<>();
            final List<Double> tickPositionsNegative = new ArrayList<>();
            for (int i = 0; i < xaTickMarks.size(); i++) {
                double pos = xAxis.getDisplayPosition(xaTickMarks.get(i).getValue());
                if (pos == xAxisZero) {
                    tickPositionsPositive.add(pos);
                    tickPositionsNegative.add(pos);
                } else if (pos < xAxisZero) {
                    tickPositionsPositive.add(pos);
                } else {
                    tickPositionsNegative.add(pos);
                }
            }
            Collections.sort(tickPositionsPositive);
            Collections.sort(tickPositionsNegative);
            // iterate over every pair of positive tick marks and create fill
            for (int i = 1; i < tickPositionsPositive.size(); i += 2) {
                if ((i + 1) < tickPositionsPositive.size()) {
                    final double x1 = tickPositionsPositive.get(i);
                    final double x2 = tickPositionsPositive.get(i + 1);
                    verticalRowFill.getElements().addAll(new MoveTo(left + x1, top), new LineTo(left + x1, top + yAxisHeight), new LineTo(left + x2, top + yAxisHeight), new LineTo(left + x2, top), new ClosePath());
                }
            }
            // iterate over every pair of positive tick marks and create fill
            for (int i = 0; i < tickPositionsNegative.size(); i += 2) {
                if ((i + 1) < tickPositionsNegative.size()) {
                    final double x1 = tickPositionsNegative.get(i);
                    final double x2 = tickPositionsNegative.get(i + 1);
                    verticalRowFill.getElements().addAll(new MoveTo(left + x1, top), new LineTo(left + x1, top + yAxisHeight), new LineTo(left + x2, top + yAxisHeight), new LineTo(left + x2, top), new ClosePath());
                }
            }
        }
        // update horizontal row fill
        horizontalRowFill.getElements().clear();
        if (isAlternativeRowFillVisible()) {
            // tick marks are not sorted so get all the positions and sort them
            final List<Double> tickPositionsPositive = new ArrayList<>();
            final List<Double> tickPositionsNegative = new ArrayList<>();
            for (int i = 0; i < yaTickMarks.size(); i++) {
                double pos = yAxis.getDisplayPosition(yaTickMarks.get(i).getValue());
                if (pos == yAxisZero) {
                    tickPositionsPositive.add(pos);
                    tickPositionsNegative.add(pos);
                } else if (pos < yAxisZero) {
                    tickPositionsPositive.add(pos);
                } else {
                    tickPositionsNegative.add(pos);
                }
            }
            Collections.sort(tickPositionsPositive);
            Collections.sort(tickPositionsNegative);
            // iterate over every pair of positive tick marks and create fill
            for (int i = 1; i < tickPositionsPositive.size(); i += 2) {
                if ((i + 1) < tickPositionsPositive.size()) {
                    final double y1 = tickPositionsPositive.get(i);
                    final double y2 = tickPositionsPositive.get(i + 1);
                    horizontalRowFill.getElements().addAll(new MoveTo(left, top + y1), new LineTo(left + xAxisWidth, top + y1), new LineTo(left + xAxisWidth, top + y2), new LineTo(left, top + y2), new ClosePath());
                }
            }
            // iterate over every pair of positive tick marks and create fill
            for (int i = 0; i < tickPositionsNegative.size(); i += 2) {
                if ((i + 1) < tickPositionsNegative.size()) {
                    final double y1 = tickPositionsNegative.get(i);
                    final double y2 = tickPositionsNegative.get(i + 1);
                    horizontalRowFill.getElements().addAll(new MoveTo(left, top + y1), new LineTo(left + xAxisWidth, top + y1), new LineTo(left + xAxisWidth, top + y2), new LineTo(left, top + y2), new ClosePath());
                }
            }
        }
    }

    public final boolean isAlternativeRowFillVisible() {
        return alternativeRowFillVisible.getValue();
    }

    public final BooleanProperty horizontalZeroLineVisibleProperty() {
        return horizontalZeroLineVisible;
    }

    public final boolean isAlternativeColumnFillVisible() {
        return alternativeColumnFillVisible.getValue();
    }

    public final boolean isHorizontalZeroLineVisible() {
        return horizontalZeroLineVisible.get();
    }

    public final boolean isHorizontalGridLinesVisible() {
        return horizontalGridLinesVisible.get();
    }

    protected void layoutPlotChildren() {
        // we have nothing to layout if no data is present
        if (getData() == null) {
            return;
        }
        // update candle positions
        for (int index = 0; index < getData().size(); index++) {
            Series<O> series = getData().get(index);
            Iterator<Data<O>> iter = getDisplayedDataIterator(series);
            Path seriesPath = null;
            if (series.getNode() instanceof Path pathNode) {
                seriesPath = pathNode;
                seriesPath.getElements().clear();
            }
            while (iter.hasNext()) {
                MutableInstantAxis xAxis = getXAxis();
                xAxis.setGranularityWidth();
                OhlcDataAxis<O> yAxis = getYAxis();
                Data<O> item = iter.next();
                MutableInstantData mutableInstantData = getCurrentDisplayedXValue(item);
                O ohlcData = getCurrentDisplayedYValue(item);
                double x = xAxis.getDisplayPosition(mutableInstantData);
                // the display position starts from the top left, and positive value go down
                // (i.e. the highest element will have x value of zero and lowest position x value of xMax)
                double open = yAxis.getDisplayPosition(ohlcData);
                Node itemNode = item.getNode();
                double candleWidth = 0.9 * xAxis.getGranularityWidth();
                double barWidth = -1;
                double halfBarWidth = -1;
                if (itemNode instanceof Candle candle && ohlcData != null) {
                    if (-1 == barWidth) {
                        barWidth = candleWidth == -1 ? candle.getBar().prefWidth(-1) : candleWidth;
                        halfBarWidth = barWidth / 2;
                    }
                    double high = yAxis.getDisplayPosition(ohlcData.high.getValue());
                    double low = yAxis.getDisplayPosition(ohlcData.low.getValue());
                    double close = yAxis.getDisplayPosition(ohlcData.close.getValue());
                    candle.update(open - high, open - low, open - close, barWidth, halfBarWidth);
                    // position the candle
                    candle.setLayoutX(x);
                    candle.setLayoutY(open);
                }
            }
        }
    }

    private double calculateGranularityWidth(Granularity granularity) {
        return 0;
    }

    protected void seriesRemoved(Series<O> series) {
        // remove all candle nodes
        for (Data<O> d : series.getData()) {
            final Node candle = d.getNode();
            if (shouldAnimate()) {
                // fade out old candle
                final FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(0);
                ft.setOnFinished((ActionEvent actionEvent) -> getPlotChildren().remove(candle));
                ft.play();
            } else {
                getPlotChildren().remove(candle);
            }
        }
    }

    /**
     * Indicates whether vertical grid lines are visible or not.
     *
     * @return true if verticalGridLines are visible else false.
     * @see #verticalGridLinesVisibleProperty()
     */
    public final boolean getVerticalGridLinesVisible() {
        return verticalGridLinesVisible.get();
    }

    private void mutableInstantValueChanged(Data<O> item, MutableInstantData newValue, ObjectProperty<MutableInstantData> currentValueProperty) {
        if (!Objects.equals(currentValueProperty.get(), newValue)) {
            invalidateRange();
            dataItemChanged(item);
        }
        if (shouldAnimate()) {
            animate(new KeyFrame(Duration.ZERO, new KeyValue(currentValueProperty, currentValueProperty.get())), new KeyFrame(Duration.millis(700), new KeyValue(currentValueProperty, newValue, Interpolator.EASE_BOTH)));
        } else {
            currentValueProperty.set(newValue);
            requestChartLayout();
        }
    }

    /**
     * Play a animation involving the given keyframes. On every frame of the animation the chart will be relayed out
     *
     * @param keyFrames Array of KeyFrames to play
     */
    void animate(KeyFrame... keyFrames) {
        animator.animate(keyFrames);
    }


    private void doubleValueChanged(Data<O> item, double newValue, DoubleProperty currentValueProperty) {
        if (currentValueProperty.get() != newValue) {
            invalidateRange();
            dataItemChanged(item);
        }
        if (shouldAnimate()) {
            animate(new KeyFrame(Duration.ZERO, new KeyValue(currentValueProperty, currentValueProperty.get())), new KeyFrame(Duration.millis(700), new KeyValue(currentValueProperty, newValue, Interpolator.EASE_BOTH)));
        } else {
            currentValueProperty.set(newValue);
            requestChartLayout();
        }
    }

    private void ohlcValueChanged(Data<O> item, O newValue, ObjectProperty<O> currentValueProperty) {
        if (currentValueProperty.get() != newValue) {
            invalidateRange();
            dataItemChanged(item);
        }
        if (shouldAnimate()) {
            animate(new KeyFrame(Duration.ZERO, new KeyValue(currentValueProperty, currentValueProperty.get())), new KeyFrame(Duration.millis(700), new KeyValue(currentValueProperty, newValue, Interpolator.EASE_BOTH)));
        } else {
            currentValueProperty.set(newValue);
            requestChartLayout();
        }
    }

    private void seriesNameChanged() {
        updateLegend();
        requestChartLayout();
    }

    protected void updateLegend() {
        List<Legend.LegendItem> legendList = new ArrayList<>();
        if (getData() != null) {
            for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
                Series<O> series = getData().get(seriesIndex);
                legendList.add(createLegendItemForSeries(series, seriesIndex));
            }
        }
        legend.getItems().setAll(legendList);
        if (legendList.size() > 0) {
            if (getLegend() == null) {
                setLegend(legend);
            }
        } else {
            setLegend(null);
        }
    }

    /**
     * Get the X axis, by default it is along the bottom of the plot
     *
     * @return the X axis of the chart
     */
    public MutableInstantAxis getXAxis() {
        return xAxis;
    }

    /**
     * This is called when the range has been invalidated and we need to update it. If the axis are auto
     * ranging then we compile a list of all data that the given axis has to plot and call invalidateRange() on the
     * axis passing it that data.
     */
    protected void updateAxisRange() {
        final MutableInstantAxis xa = getXAxis();
        final OhlcDataAxis<O> ya = getYAxis();
        List<MutableInstantData> xData = null;
        List<O> yData = null;
        if (xa.isAutoRanging()) {
            xData = new ArrayList<>();
        }
        if (ya.isAutoRanging()) {
            yData = new ArrayList<>();
        }
        if (xData != null || yData != null) {
            for (Series<O> series : getData()) {
                for (Data<O> data : series.getData()) {
                    if (xData != null) {
                        xData.add(data.getXValue());
                    }
                    if (yData != null) {
                        yData.add(data.getYValue());
                    }
                }
            }
            if (xData != null) {
                xa.invalidateRange(xData);
            }
            if (yData != null) {
                ya.invalidateRange(yData);
            }
        }
    }

    /**
     * Called when a data item has been added to a series. This is where implementations of XYChart can create/add new
     * nodes to getPlotChildren to represent this data item. They also may animate that data add with a fade in or
     * similar if animated = true.
     *
     * @param series    The series the data item was added to
     * @param itemIndex The index of the new item within the series
     * @param item      The new data item that was added
     */
    protected void dataItemAdded(Series<O> series, int itemIndex, Data<O> item) {
        Node candle = createCandle(getData().indexOf(series), item, itemIndex);
        if (shouldAnimate()) {
            candle.setOpacity(0);
            getPlotChildren().add(candle);
            // fade in new candle
            final FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
            ft.setToValue(1);
            ft.play();
        } else {
            getPlotChildren().add(candle);
        }
        // always draw average line on top
        if (series.getNode() != null) {
            series.getNode().toFront();
        }
    }

    /**
     * Called when a data item has been removed from data model but it is still visible on the chart. Its still visible
     * so that you can handle animation for removing it in this method. After you are done animating the data item you
     * must call removeDataItemFromDisplay() to remove the items node from being displayed on the chart.
     *
     * @param item   The item that has been removed from the series
     * @param series The series the item was removed from
     */
    protected void dataItemRemoved(Data<O> item, Series<O> series) {
        final Node candle = item.getNode();
        if (shouldAnimate()) {
            // fade out old candle
            final FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
            ft.setToValue(0);
            ft.setOnFinished((ActionEvent actionEvent) -> {
                getPlotChildren().remove(candle);
            });
            ft.play();
        } else {
            getPlotChildren().remove(candle);
        }
    }


    private void dataItemsChanged(Series<O> series, List<? extends Data<O>> removed, int addedFrom, int addedTo, boolean permutation) {
        for (Data<O> item : removed) {
            dataItemRemoved(item, series);
        }
        for (int i = addedFrom; i < addedTo; i++) {
            Data<O> item = series.getData().get(i);
            dataItemAdded(series, i, item);
        }
        invalidateRange();
        requestChartLayout();
    }

    /**
     * This method is called when there is an attempt to add a Data item that was
     * set to be removed, and the removal might not have completed.
     *
     * @param item
     * @param series
     */
    void dataBeingRemovedIsAdded(Data<O> item, Series<O> series) {
    }

    /**
     * Called when each atomic change is made to the list of series for this chart
     *
     * @param c a Change instance representing the changes to the series
     */
    protected void seriesChanged(ListChangeListener.Change<? extends Series<O>> c) {
    }

    /**
     * A series has been added to the charts data model. This is where implementations of XYChart can create/add new
     * nodes to getPlotChildren to represent this series. Also you have to handle adding any data items that are
     * already in the series. You may simply call dataItemAdded() for each one or provide some different animation for
     * a whole series being added.
     *
     * @param series      The series that has been added
     * @param seriesIndex The index of the new series
     */
    protected void seriesAdded(Series<O> series, int seriesIndex) {
        // handle any data already in series
        for (int j = 0; j < series.getData().size(); j++) {
            Data<O> item = series.getData().get(j);
            Node candle = createCandle(seriesIndex, item, j);
            if (shouldAnimate()) {
                candle.setOpacity(0);
                getPlotChildren().add(candle);
                // fade in new candle
                final FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(1);
                ft.play();
            } else {
                getPlotChildren().add(candle);
            }
        }
        // create series path
        Path seriesPath = new Path();
        series.setNode(seriesPath);
        getPlotChildren().add(seriesPath);
    }

    /**
     * Modifiable and observable list of all content in the plot. This is where implementations of XYChart should add
     * any nodes they use to draw their plot.
     *
     * @return Observable list of plot children
     */
    protected ObservableList<Node> getPlotChildren() {
        return plotContent.getChildren();
    }

    /**
     * Create a new Candle node to represent a single data item
     */
    private Node createCandle(int seriesIndex, final Data<O> item, int itemIndex) {
        Node node = item.getNode();
        // check if candle has already been created
        if (node instanceof Candle candle) {
            // FIXME: get rid of new instance of String
            candle.setSeriesAndDataStyleClasses("series" + seriesIndex, "data" + itemIndex);
        } else {
            node = new Candle("series" + seriesIndex, "data" + itemIndex);
            item.setNode(node);
        }
        return node;
    }

    /**
     * This method is called when there is an attempt to add series that was
     * set to be removed, and the removal might not have completed.
     *
     * @param series
     */
    void seriesBeingRemovedIsAdded(Series<O> series) {
    }

    Legend.LegendItem createLegendItemForSeries(Series<O> series, int seriesIndex) {
        return new Legend.LegendItem(series.getName());
    }

    public final ObservableList<Series<O>> getData() {
        return data.getValue();
    }

    public final void setData(ObservableList<Series<O>> value) {
        data.setValue(value);
    }

    protected void dataItemChanged(Data<O> item) {

    }

    private void invalidateRange() {
        rangeValid = false;
    }

    public final boolean isVerticalZeroLineVisible() {
        return verticalZeroLineVisible.get();
    }

    public final BooleanProperty alternativeColumnFillVisibleProperty() {
        return alternativeColumnFillVisible;
    }

    public final BooleanProperty verticalZeroLineVisibleProperty() {
        return verticalZeroLineVisible;
    }

    public final BooleanProperty alternativeRowFillVisibleProperty() {
        return alternativeRowFillVisible;
    }

    public final BooleanProperty verticalGridLinesVisibleProperty() {
        return verticalGridLinesVisible;
    }

    protected final Iterator<Data<O>> getDisplayedDataIterator(final Series<O> series) {
        return Collections.unmodifiableList(series.displayedData).iterator();
    }

    protected final MutableInstantData getCurrentDisplayedXValue(Data<O> item) {
        return item.getCurrentX();
    }

    protected final O getCurrentDisplayedYValue(Data<O> item) {
        return item.getCurrentY();
    }

    /**
     * A single data item with data for 2 axis charts
     *
     * @since JavaFX 2.0
     */
    public final static class Data<O extends OhlcData<O, ?>> {
        private static final String X_VALUE = "dateTime";
        private static final String Y_VALUE = "ohlc";
        // -------------- PUBLIC PROPERTIES ----------------------------------------

        private boolean setToRemove = false;
        /**
         * The series this data belongs to
         */
        private Series<O> series;
        private O ohlcData;

        /**
         * The generic data value to be plotted on the X axis
         */
        private ObjectProperty<MutableInstantData> xValue = new SimpleObjectProperty<>(Data.this, X_VALUE) {
            @Override
            protected void invalidated() {
                if (series != null) {
                    OhlcChart<O> chart = series.getChart();
                    if (chart != null) {
                        chart.mutableInstantValueChanged(Data.this, get(), currentXProperty());
                    }
                } else {
                    // data has not been added to series yet :
                    // so currentX and X should be the same
                    setCurrentX(get());
                }
            }
        };

        /**
         * The generic data value to be plotted on the Y axis
         */
        private ObjectProperty<O> yValue = new SimpleObjectProperty<>(Data.this, Y_VALUE) {
            @Override
            protected void invalidated() {
                if (series != null) {
                    OhlcChart<O> chart = series.getChart();
                    if (chart != null) {
                        chart.ohlcValueChanged(Data.this, get(), currentYProperty());
                    }
                } else {
                    // data has not been added to series yet :
                    // so currentY and Y should be the same
                    setCurrentY(get());
                }
            }
        };


        /**
         * The node to display for this data item. You can either create your own node and set it on the data item
         * before you add the item to the chart. Otherwise the chart will create a node for you that has the default
         * representation for the chart type. This node will be set as soon as the data is added to the chart. You can
         * then get it to add mouse listeners etc. Charts will do their best to position and size the node
         * appropriately, for example on a Line or Scatter chart this node will be positioned centered on the data
         * values position. For a bar chart this is positioned and resized as the bar for this data item.
         */
        private ObjectProperty<Node> node = new SimpleObjectProperty<>(this, NODE) {
            protected void invalidated() {
                Node node = get();
                if (node != null) {
                    node.accessibleTextProperty().unbind();
                    node.accessibleTextProperty().bind(new StringBinding() {
                        {
                            bind(currentXProperty(), currentYProperty());
                        }

                        @Override
                        protected String computeValue() {
                            String seriesName = series != null ? series.getName() : "";
                            return seriesName + " X Axis is " + getCurrentX() + " Y Axis is " + getCurrentY();
                        }
                    });
                }
            }
        };


        /**
         * The current displayed data value plotted on the X axis. This may be the same as xValue or different. It is
         * used by XYChart to animate the xValue from the old value to the new value. This is what you should plot
         * in any custom XYChart implementations. Some XYChart chart implementations such as LineChart also use this
         * to animate when data is added or removed.
         */
        private ObjectProperty<MutableInstantData> currentX = new SimpleObjectProperty<>(this, CURRENT_X);


        /**
         * The current displayed data value plotted on the Y axis. This may be the same as yValue or different. It is
         * used by XYChart to animate the yValue from the old value to the new value. This is what you should plot
         * in any custom XYChart implementations. Some XYChart chart implementations such as LineChart also use this
         * to animate when data is added or removed.
         */
        private ObjectProperty<O> currentY = new SimpleObjectProperty<>(this, CURRENT_Y);

        // -------------- CONSTRUCTOR -------------------------------------------------

        /**
         * Creates an empty Data object.
         */
        public Data() {
        }

        /**
         * Creates an instance of Data object and initializes the X,Y
         * data values.
         *
         * @param xValue The X axis data value
         * @param yValue The Y axis data value
         */
        public Data(MutableInstantData xValue, O yValue) {
            setXValue(xValue);
            setYValue(yValue);
            setCurrentX(xValue);
            setCurrentY(yValue);
        }

        void setSeries(Series<O> series) {
            this.series = series;
        }

        /**
         * Gets the generic data value to be plotted on the X axis.
         *
         * @return the generic data value to be plotted on the X axis.
         */
        public MutableInstantData getXValue() {
            return xValue.get();
        }

        /**
         * Sets the generic data value to be plotted on the X axis.
         *
         * @param value the generic data value to be plotted on the X axis.
         */
        public void setXValue(MutableInstantData value) {
            xValue.set(value);
            // handle the case where this is a init because the default constructor was used
            // and the case when series is not associated to a chart due to a remove series
            if (currentX.get() == null || (series != null && series.getChart() == null)) {
                currentX.setValue(value);
            }
        }

        /**
         * The generic data value to be plotted on the X axis.
         *
         * @return The XValue property
         */
        public ObjectProperty<MutableInstantData> XValueProperty() {
            return xValue;
        }

        /**
         * Gets the generic data value to be plotted on the Y axis.
         *
         * @return the generic data value to be plotted on the Y axis.
         */
        public O getYValue() {
            return yValue.get();
        }

        /**
         * Sets the generic data value to be plotted on the Y axis.
         *
         * @param value the generic data value to be plotted on the Y axis.
         */
        public void setYValue(O value) {
            yValue.set(value);
            // handle the case where this is a init because the default constructor was used
            // and the case when series is not associated to a chart due to a remove series
            if (series != null && series.getChart() == null) {
                currentY.setValue(value);
            }
        }

        public O getOhlcData() {
            return ohlcData;
        }

        /**
         * The generic data value to be plotted on the Y axis.
         *
         * @return the YValue property
         */
        public ObjectProperty<O> YValueProperty() {
            return yValue;
        }

        public Node getNode() {
            return node.get();
        }

        public void setNode(Node value) {
            node.set(value);
        }

        public ObjectProperty<Node> nodeProperty() {
            return node;
        }

        MutableInstantData getCurrentX() {
            return currentX.get();
        }

        void setCurrentX(MutableInstantData value) {
            currentX.set(value);
        }

        ObjectProperty<MutableInstantData> currentXProperty() {
            return currentX;
        }

        O getCurrentY() {
            return currentY.get();
        }

        void setCurrentY(O value) {
            currentY.set(value);
        }

        ObjectProperty<O> currentYProperty() {
            return currentY;
        }

        // -------------- PUBLIC METHODS ----------------------------------------------

        /**
         * Returns a string representation of this {@code Data} object.
         *
         * @return a string representation of this {@code Data} object.
         */
        @Override
        public String toString() {
            return "Data[" + getXValue() + "," + getYValue() + "]";
        }

    }

    public static final class Series<O extends OhlcData<O, ?>> {

        // -------------- PRIVATE PROPERTIES ----------------------------------------

        /**
         * the style class for default color for this series
         */
        String defaultColorStyleClass;
        boolean setToRemove = false;

        private List<Data<O>> displayedData = new ArrayList<>();

        private final ListChangeListener<Data<O>> dataChangeListener = changedData -> {
            ObservableList<? extends Data<O>> data = changedData.getList();
            final OhlcChart<O> chart = getChart();
            while (changedData.next()) {
                if (chart != null) {
                    // RT-25187 Probably a sort happened, just reorder the pointers and return.
                    if (changedData.wasPermutated()) {
                        displayedData.sort((o1, o2) -> data.indexOf(o2) - data.indexOf(o1));
                        return;
                    }

                    Set<Data<O>> dupCheck = new HashSet<>(displayedData);
                    dupCheck.removeAll(changedData.getRemoved());
                    for (Data<O> d : changedData.getAddedSubList()) {
                        if (!dupCheck.add(d)) {
                            throw new IllegalArgumentException("Duplicate data added");
                        }
                    }

                    // update data items reference to series
                    for (Data<O> item : changedData.getRemoved()) {
                        item.setToRemove = true;
                    }

                    if (changedData.getAddedSize() > 0) {
                        for (Data<O> itemPtr : changedData.getAddedSubList()) {
                            if (itemPtr.setToRemove) {
                                if (chart != null) chart.dataBeingRemovedIsAdded(itemPtr, Series.this);
                                itemPtr.setToRemove = false;
                            }
                        }

                        for (Data<O> d : changedData.getAddedSubList()) {
                            d.setSeries(Series.this);
                        }
                        if (changedData.getFrom() == 0) {
                            displayedData.addAll(0, changedData.getAddedSubList());
                        } else {
                            displayedData.addAll(displayedData.indexOf(data.get(changedData.getFrom() - 1)) + 1, changedData.getAddedSubList());
                        }
                    }
                    // inform chart
                    chart.dataItemsChanged(Series.this, changedData.getRemoved(), changedData.getFrom(), changedData.getTo(), changedData.wasPermutated());
                } else {
                    Set<Data<O>> dupCheck = new HashSet<>();
                    for (Data<O> d : data) {
                        if (!dupCheck.add(d)) {
                            throw new IllegalArgumentException("Duplicate data added");
                        }
                    }

                    for (Data<O> d : changedData.getAddedSubList()) {
                        d.setSeries(Series.this);
                    }

                }
            }
        };

        // -------------- PUBLIC PROPERTIES ----------------------------------------

        /**
         * Reference to the chart this series belongs to
         */
        private final ReadOnlyObjectWrapper<OhlcChart<O>> chart = new ReadOnlyObjectWrapper<>(this, CHART) {
            @Override
            protected void invalidated() {
                if (get() == null) {
                    displayedData.clear();
                } else {
                    displayedData.addAll(getData());
                }
            }
        };

        public OhlcChart<O> getChart() {
            return chart.get();
        }

        private void setChart(OhlcChart<O> value) {
            chart.set(value);
        }

        public ReadOnlyObjectProperty<OhlcChart<O>> chartProperty() {
            return chart.getReadOnlyProperty();
        }

        /**
         * The user displayable name for this series
         */
        private final StringProperty name = new StringPropertyBase() {
            @Override
            protected void invalidated() {
                get(); // make non-lazy
                if (getChart() != null) getChart().seriesNameChanged();
            }

            @Override
            public Object getBean() {
                return Series.this;
            }

            @Override
            public String getName() {
                return NAME;
            }
        };

        public String getName() {
            return name.get();
        }

        public void setName(String value) {
            name.set(value);
        }

        public StringProperty nameProperty() {
            return name;
        }

        /**
         * The node to display for this series. This is created by the chart if it uses nodes to represent the whole
         * series. For example line chart uses this for the line but scatter chart does not use it. This node will be
         * set as soon as the series is added to the chart. You can then get it to add mouse listeners etc.
         */
        private ObjectProperty<Node> node = new SimpleObjectProperty<>(this, NODE);

        public final Node getNode() {
            return node.get();
        }

        public final void setNode(Node value) {
            node.set(value);
        }

        public final ObjectProperty<Node> nodeProperty() {
            return node;
        }

        /**
         * ObservableList of data items that make up this series
         */
        private final ObjectProperty<ObservableList<Data<O>>> data = new ObjectPropertyBase<>() {
            private ObservableList<Data<O>> old;

            @Override
            protected void invalidated() {
                final ObservableList<Data<O>> current = getValue();
                // add remove listeners
                if (old != null) old.removeListener(dataChangeListener);
                if (current != null) current.addListener(dataChangeListener);
                // fire data change event if series are added or removed
                if (old != null || current != null) {
                    final List<Data<O>> removed = (old != null) ? old : Collections.emptyList();
                    final int toIndex = (current != null) ? current.size() : 0;
                    // let data listener know all old data have been removed and new data that has been added
                    if (toIndex > 0 || !removed.isEmpty()) {
                        dataChangeListener.onChanged(new NonIterableChange<>(0, toIndex, current) {
                            @Override
                            public List<Data<O>> getRemoved() {
                                return removed;
                            }

                            @Override
                            protected int[] getPermutation() {
                                return new int[0];
                            }
                        });
                    }
                } else if (old != null && old.size() > 0) {
                    // let series listener know all old series have been removed
                    dataChangeListener.onChanged(new NonIterableChange<>(0, 0, current) {
                        @Override
                        public List<Data<O>> getRemoved() {
                            return old;
                        }

                        @Override
                        protected int[] getPermutation() {
                            return new int[0];
                        }
                    });
                }
                old = current;
            }

            @Override
            public Object getBean() {
                return Series.this;
            }

            @Override
            public String getName() {
                return DATA;
            }
        };

        public ObservableList<Data<O>> getData() {
            return data.getValue();
        }

        public void setData(ObservableList<Data<O>> value) {
            data.setValue(value);
        }

        public ObjectProperty<ObservableList<Data<O>>> dataProperty() {
            return data;
        }

        // -------------- CONSTRUCTORS ----------------------------------------------

        /**
         * Construct a empty series
         */
        public Series() {
            this(FXCollections.observableArrayList());
        }

        /**
         * Constructs a Series and populates it with the given {@link ObservableList} data.
         *
         * @param data ObservableList of Data
         */
        public Series(ObservableList<Data<O>> data) {
            setData(data);
            for (Data<O> item : data) item.setSeries(this);
        }

        /**
         * Constructs a named Series and populates it with the given {@link ObservableList} data.
         *
         * @param name a name for the series
         * @param data ObservableList of Data
         */
        public Series(String name, ObservableList<Data<O>> data) {
            this(data);
            setName(name);
        }

        // -------------- PUBLIC METHODS ----------------------------------------------

        /**
         * Returns a string representation of this {@code Series} object.
         *
         * @return a string representation of this {@code Series} object.
         */
        @Override
        public String toString() {
            return "Series[" + getName() + "]";
        }

        // -------------- PRIVATE/PROTECTED METHODS -----------------------------------

        /*
         * The following methods are for manipulating the pointers in the linked list
         * when data is deleted.
         */
        private void removeDataItemRef(Data<O> item) {
            if (item != null) item.setToRemove = false;
            displayedData.remove(item);
        }

        int getItemIndex(Data<O> item) {
            return displayedData.indexOf(item);
        }

        Data<O> getItem(int i) {
            return displayedData.get(i);
        }

        int getDataSize() {
            return displayedData.size();
        }
    }

    private static class StyleableProperties {
        private static final CssMetaData<OhlcChart<?>, Boolean> HORIZONTAL_GRID_LINE_VISIBLE = new CssMetaData<>(CssMetaDataConstants.HORIZONTAL_GRID_LINES_VISIBLE, BooleanConverter.getInstance(), Boolean.TRUE) {
            @Override
            public boolean isSettable(OhlcChart<?> node) {
                return node.horizontalGridLinesVisible == null || !node.horizontalGridLinesVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(OhlcChart<?> node) {
                return (StyleableProperty<Boolean>) node.horizontalGridLinesVisibleProperty();
            }
        };

        private static final CssMetaData<OhlcChart<?>, Boolean> HORIZONTAL_ZERO_LINE_VISIBLE = new CssMetaData<>(CssMetaDataConstants.HORIZONTAL_ZERO_LINE_VISIBLE, BooleanConverter.getInstance(), Boolean.TRUE) {

            @Override
            public boolean isSettable(OhlcChart<?> node) {
                return node.horizontalZeroLineVisible == null || !node.horizontalZeroLineVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(OhlcChart<?> node) {
                return (StyleableProperty<Boolean>) node.horizontalZeroLineVisibleProperty();
            }
        };

        private static final CssMetaData<OhlcChart<?>, Boolean> ALTERNATIVE_ROW_FILL_VISIBLE = new CssMetaData<>(CssMetaDataConstants.ALTERNATIVE_ROW_FILL_VISIBLE, BooleanConverter.getInstance(), Boolean.TRUE) {
            @Override
            public boolean isSettable(OhlcChart<?> node) {
                return node.alternativeRowFillVisible == null || !node.alternativeRowFillVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(OhlcChart<?> node) {
                return (StyleableProperty<Boolean>) node.alternativeRowFillVisibleProperty();
            }
        };

        private static final CssMetaData<OhlcChart<?>, Boolean> VERTICAL_GRID_LINE_VISIBLE = new CssMetaData<>(CssMetaDataConstants.VERTICAL_GRID_LINE_VISIBLE, BooleanConverter.getInstance(), Boolean.TRUE) {

            @Override
            public boolean isSettable(OhlcChart<?> node) {
                return node.verticalGridLinesVisible == null || !node.verticalGridLinesVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(OhlcChart<?> node) {
                return (StyleableProperty<Boolean>) node.verticalGridLinesVisibleProperty();
            }
        };

        private static final CssMetaData<OhlcChart<?>, Boolean> VERTICAL_ZERO_LINE_VISIBLE = new CssMetaData<>(CssMetaDataConstants.VERTICAL_ZERO_LINE_VISIBLE, BooleanConverter.getInstance(), Boolean.TRUE) {
            @Override
            public boolean isSettable(OhlcChart<?> node) {
                return node.verticalZeroLineVisible == null || !node.verticalZeroLineVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(OhlcChart<?> node) {
                return (StyleableProperty<Boolean>) node.verticalZeroLineVisibleProperty();
            }
        };

        private static final CssMetaData<OhlcChart<?>, Boolean> ALTERNATIVE_COLUMN_FILL_VISIBLE = new CssMetaData<>(CssMetaDataConstants.ALTERNATIVE_COLUMN_FILL_VISIBLE, BooleanConverter.getInstance(), Boolean.TRUE) {
            @Override
            public boolean isSettable(OhlcChart<?> node) {
                return node.alternativeColumnFillVisible == null || !node.alternativeColumnFillVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(OhlcChart<?> node) {
                return (StyleableProperty<Boolean>) node.alternativeColumnFillVisibleProperty();
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Chart.getClassCssMetaData());
            styleables.add(HORIZONTAL_GRID_LINE_VISIBLE);
            styleables.add(HORIZONTAL_ZERO_LINE_VISIBLE);
            styleables.add(ALTERNATIVE_ROW_FILL_VISIBLE);
            styleables.add(VERTICAL_GRID_LINE_VISIBLE);
            styleables.add(VERTICAL_ZERO_LINE_VISIBLE);
            styleables.add(ALTERNATIVE_COLUMN_FILL_VISIBLE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }
}
