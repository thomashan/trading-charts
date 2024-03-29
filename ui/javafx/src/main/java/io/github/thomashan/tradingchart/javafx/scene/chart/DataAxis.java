package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.AxisData;
import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.css.converter.BooleanConverter;
import javafx.css.converter.SizeConverter;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.CURRENT_FORMATTER;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.CURRENT_LOWER_BOUND;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.MINOR_TICK_COUNT;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.MINOR_TICK_LENGTH;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.MINOR_TICK_VISIBLE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.SCALE;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.TICK_LABEL_FORMATTER;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.TICK_UNIT;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.UPPER_BOUND;

public abstract class DataAxis<D extends AxisData<D>> extends Axis<D> {
    private static final ZoneId UTC = ZoneId.of("UTC");
    private Object currentAnimationID;
    private final ChartLayoutAnimator animator = new ChartLayoutAnimator(this);
    private final StringProperty currentFormatterProperty = new SimpleStringProperty(this, CURRENT_FORMATTER, AxisConstants.EMPTY_STRING);
    private final StringConverter<D> defaultFormatter = createDefaultFormatter();
    private final D currentValue = createCurrentValue();
    private final D major = createMajor();

    protected D getMajor() {
        return major;
    }

    boolean measureInvalid = false;

    protected abstract D createMajor();

    protected abstract D createCurrentValue();

    protected abstract StringConverter<D> createDefaultFormatter();

    // FIXME: initialise all the tickValues so the tick values can be used later
    private D tickValue = createTickValue();

    protected D getTickValue() {
        return tickValue;
    }

    protected abstract D createTickValue();

    private final List<D> tickValues = new ArrayList<>();

    protected List<D> getTickValues() {
        return tickValues;
    }


    // -------------- PUBLIC PROPERTIES --------------------------------------------------------------------------------

    /**
     * When true zero is always included in the visible range. This only has effect if auto-ranging is on.
     */
    private BooleanProperty forceZeroInRange = new BooleanPropertyBase(false) {
        @Override
        protected void invalidated() {
            // This will affect layout if we are auto ranging
            if (isAutoRanging()) {
                requestAxisLayout();
                invalidateRange();
            }
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return AxisConstants.FORCE_ZERO_IN_RANGE;
        }
    };

    public boolean isForceZeroInRange() {
        return forceZeroInRange.getValue();
    }

    public void setForceZeroInRange(boolean value) {
        forceZeroInRange.setValue(value);
    }

    public BooleanProperty forceZeroInRangeProperty() {
        return forceZeroInRange;
    }

    /**
     * The value between each major tick mark in data units. This is automatically set if we are auto-ranging.
     */
    private DoubleProperty tickUnit = new StyleableDoubleProperty(5) {
        @Override
        protected void invalidated() {
            if (!isAutoRanging()) {
                invalidateRange();
                requestAxisLayout();
            }
        }

        @Override
        public CssMetaData<DataAxis<?>, Number> getCssMetaData() {
            return DataAxis.StyleableProperties.TICK_UNIT;
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return TICK_UNIT;
        }
    };

    public double getTickUnit() {
        return tickUnit.get();
    }

    public void setTickUnit(double value) {
        tickUnit.set(value);
    }

    public DoubleProperty tickUnitProperty() {
        return tickUnit;
    }

    /**
     * The value for the lower bound of this axis (minimum value). This is automatically set if auto ranging is on.
     */
    private DoubleProperty lowerBound = new DoublePropertyBase(0) {
        @Override
        protected void invalidated() {
            if (!isAutoRanging()) {
                invalidateRange();
                requestAxisLayout();
            }
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return AxisConstants.LOWER_BOUND;
        }
    };

    public double getLowerBound() {
        return lowerBound.get();
    }

    public void setLowerBound(double value) {
        lowerBound.set(value);
    }

    public DoubleProperty lowerBoundProperty() {
        return lowerBound;
    }

    // -------------- PRIVATE FIELDS -----------------------------------------------------------------------------------

    private final Path minorTickPath = new Path();

    private double offset;
    /**
     * This is the minimum current data value, and it is used while auto ranging.
     * Package private solely for test purposes
     */
    private double dataMinValue;
    /**
     * This is the maximum current data value, and it is used while auto ranging.
     * Package private solely for test purposes
     */
    private double dataMaxValue;
    /**
     * List of the values at which there are minor ticks
     */
    private List<Double> minorTickMarkValues = null;
    private boolean minorTickMarksDirty = true;
    // -------------- PRIVATE PROPERTIES -------------------------------------------------------------------------------

    /**
     * The current value for the lowerBound of this axis (minimum value).
     * This may be the same as lowerBound or different. It is used by ObjectAxis to animate the
     * lowerBound from the old value to the new value.
     */
    protected final DoubleProperty currentLowerBound = new SimpleDoubleProperty(this, CURRENT_LOWER_BOUND);

    // -------------- PUBLIC PROPERTIES --------------------------------------------------------------------------------

    /**
     * true if minor tick marks should be displayed
     */
    private BooleanProperty minorTickVisible = new StyleableBooleanProperty(true) {
        @Override
        protected void invalidated() {
            minorTickPath.setVisible(get());
            requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return MINOR_TICK_VISIBLE;
        }

        @Override
        public CssMetaData<DataAxis<?>, Boolean> getCssMetaData() {
            return StyleableProperties.MINOR_TICK_VISIBLE;
        }
    };

    public boolean isMinorTickVisible() {
        return minorTickVisible.get();
    }

    public void setMinorTickVisible(boolean value) {
        minorTickVisible.set(value);
    }

    public BooleanProperty minorTickVisibleProperty() {
        return minorTickVisible;
    }


    /**
     * The scale factor from data units to visual units
     */
    private ReadOnlyDoubleWrapper scale = new ReadOnlyDoubleWrapper(this, SCALE, 0) {
        @Override
        protected void invalidated() {
            requestAxisLayout();
            measureInvalid = true;
        }
    };

    public double getScale() {
        return scale.get();
    }

    protected void setScale(double scale) {
        this.scale.set(scale);
    }

    public ReadOnlyDoubleProperty scaleProperty() {
        return scale.getReadOnlyProperty();
    }

    ReadOnlyDoubleWrapper scalePropertyImpl() {
        return scale;
    }

    /**
     * The value for the upper bound of this axis (maximum value). This is automatically set if auto ranging is on.
     */
    private DoubleProperty upperBound = new DoublePropertyBase(100) {
        @Override
        protected void invalidated() {
            if (!isAutoRanging()) {
                invalidateRange();
                requestAxisLayout();
            }
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return UPPER_BOUND;
        }
    };

    public double getUpperBound() {
        return upperBound.get();
    }

    public void setUpperBound(double value) {
        upperBound.set(value);
    }

    public DoubleProperty upperBoundProperty() {
        return upperBound;
    }

    /**
     * StringConverter used to format tick mark labels. If null a default will be used
     */
    private final ObjectProperty<StringConverter<D>> tickLabelFormatter = new ObjectPropertyBase<>(null) {
        @Override
        protected void invalidated() {
            invalidateRange();
            requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return TICK_LABEL_FORMATTER;
        }
    };

    public StringConverter<D> getTickLabelFormatter() {
        StringConverter<D> stringConverter = tickLabelFormatter.getValue();
        return null == stringConverter ? defaultFormatter : stringConverter;
    }

    public void setTickLabelFormatter(StringConverter<D> value) {
        tickLabelFormatter.setValue(value);
    }

    public ObjectProperty<StringConverter<D>> tickLabelFormatterProperty() {
        return tickLabelFormatter;
    }

    /**
     * The length of minor tick mark lines. Set to 0 to not display minor tick marks.
     */
    private DoubleProperty minorTickLength = new StyleableDoubleProperty(5) {
        @Override
        protected void invalidated() {
            requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return MINOR_TICK_LENGTH;
        }

        @Override
        public CssMetaData<DataAxis<?>, Number> getCssMetaData() {
            return StyleableProperties.MINOR_TICK_LENGTH;
        }
    };

    public double getMinorTickLength() {
        return minorTickLength.get();
    }

    public void setMinorTickLength(double value) {
        minorTickLength.set(value);
    }

    public DoubleProperty minorTickLengthProperty() {
        return minorTickLength;
    }

    /**
     * The number of minor tick divisions to be displayed between each major tick mark.
     * The number of actual minor tick marks will be one less than this.
     */
    private IntegerProperty minorTickCount = new StyleableIntegerProperty(5) {
        @Override
        protected void invalidated() {
            invalidateRange();
            requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return DataAxis.this;
        }

        @Override
        public String getName() {
            return MINOR_TICK_COUNT;
        }

        @Override
        public CssMetaData<DataAxis<?>, Number> getCssMetaData() {
            return DataAxis.StyleableProperties.MINOR_TICK_COUNT;
        }
    };

    public int getMinorTickCount() {
        return minorTickCount.get();
    }

    public void setMinorTickCount(int value) {
        minorTickCount.set(value);
    }

    public IntegerProperty minorTickCountProperty() {
        return minorTickCount;
    }

    // -------------- CONSTRUCTORS -------------------------------------------------------------------------------------

    /**
     * Creates an auto-ranging ObjectAxis.
     */
    public DataAxis() {
        super();
    }

    /**
     * Creates a non-auto-ranging ValueAxis with the given lower and upper bound.
     *
     * @param lowerBound The lower bound for this axis, i.e. min plottable value
     * @param upperBound The upper bound for this axis, i.e. max plottable value
     */
    public DataAxis(double lowerBound, double upperBound) {
        this();
        setAutoRanging(false);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    /**
     * Creates a non-auto-ranging ObjectAxis with the given upper bound, lower bound and tick unit.
     *
     * @param lowerBound The lower bound for this axis, i.e. min plottable value
     * @param upperBound The upper bound for this axis, i.e. max plottable value
     * @param tickUnit   The tick unit, i.e. space between tickmarks
     */
    public DataAxis(double lowerBound, double upperBound, double tickUnit) {
        this(lowerBound, upperBound);
        setTickUnit(tickUnit);
    }

    /**
     * Creates a non-auto-ranging ObjectAxis with the given lower bound, upper bound and tick unit.
     *
     * @param axisLabel  The name to display for this axis
     * @param lowerBound The lower bound for this axis, i.e. min plottable value
     * @param upperBound The upper bound for this axis, i.e. max plottable value
     * @param tickUnit   The tick unit, i.e. space between tickmarks
     */
    public DataAxis(String axisLabel, double lowerBound, double upperBound, double tickUnit) {
        this(lowerBound, upperBound);
        setTickUnit(tickUnit);
        setLabel(axisLabel);
    }

    // -------------- PROTECTED METHODS --------------------------------------------------------------------------------

    // return getTickLabelFont().getSize() * 2;
    protected abstract double calculateLabelSize();


    @Override
    protected Object autoRange(double length) {
        // guess a sensible starting size for label size, that is approx 2 lines vertically or 2 charts horizontally
        if (isAutoRanging()) {
            // guess a sensible starting size for label size, that is approx 2 lines vertically or 2 charts horizontally
            double labelSize = calculateLabelSize();
            return autoRange(dataMinValue, dataMaxValue, length, labelSize);
        } else {
            return getRange();
        }
    }

    /**
     * Get the string label name for a tick mark with the given value.
     *
     * @param value The value to format into a tick label string
     * @return A formatted string for the given value
     */
    @Override
    protected String getTickMarkLabel(D value) {
        return getTickLabelFormatter().toString(value);
    }

    /**
     * Called to get the current axis range.
     *
     * @return A range object that can be passed to setRange() and calculateTickValues()
     */
    @Override
    protected Object getRange() {
        return new Object[]{getLowerBound(), getUpperBound(), getTickUnit(), getScale(), currentFormatterProperty.get()};
    }

    @Override
    public double getZeroPosition() {
        if (0 < getLowerBound() || 0 > getUpperBound()) {
            return Double.NaN;
        }
        return getDisplayPosition(getZeroPositionValue());
    }

    protected abstract D getZeroPositionValue();

    @Override
    public double getDisplayPosition(D value) {
        return offset + ((value.getValue() - currentLowerBound.get()) * getScale());
    }

    public double getDisplayPosition(double value) {
        return offset + ((value - currentLowerBound.get()) * getScale());
    }

    @Override
    public D getValueForDisplay(double displayPosition) {
        return toRealValue(((displayPosition - offset) / getScale()) + currentLowerBound.get());
    }

    @Override
    public boolean isValueOnAxis(D value) {
        double num = value.getValue();
        return num >= getLowerBound() && num <= getUpperBound();
    }

    @Override
    public double toNumericValue(D value) {
        return null == value ? Double.NaN : value.getValue();
    }

    @Override
    public D toRealValue(double value) {
        return currentValue.setValue(value);
    }

    /**
     * Called to set the current axis range to the given range. If isAnimating() is true then this method should
     * animate the range to the new range.
     *
     * @param range   A range object returned from autoRange()
     * @param animate If true animate the change in range
     */
    @Override
    protected void setRange(Object range, boolean animate) {
        if (range instanceof Object[] rangeProps) {
            double[] rangeObject = (double[]) rangeProps[0];
            double lowerBound = rangeObject[0];
            double upperBound = rangeObject[1];
            double tickUnit = rangeObject[2];
            double scale = rangeObject[3];
            String formatter = (String) rangeProps[1];
            currentFormatterProperty.set(formatter);
            double oldLowerBound = getLowerBound();
            setLowerBound(lowerBound);
            setUpperBound(upperBound);
            setTickUnit(tickUnit);
            if (animate) {
                animator.stop(currentAnimationID);
                currentAnimationID = animator.animate(new KeyFrame(Duration.ZERO, new KeyValue(currentLowerBound, oldLowerBound), new KeyValue(scalePropertyImpl(), getScale())), new KeyFrame(Duration.millis(700), new KeyValue(currentLowerBound, lowerBound), new KeyValue(scalePropertyImpl(), scale)));
            } else {
                currentLowerBound.set(lowerBound);
                setScale(scale);
            }
        }
    }

    /**
     * Calculates a list of the data values for every minor tick mark
     *
     * @return List of data values where to draw minor tick marks
     */
    protected List<Number> calculateMinorTickMarks() {
        List<Number> minorTickMarks = new ArrayList<>();
        double lowerBound = getLowerBound();
        double upperBound = getUpperBound();
        double tickUnit = getTickUnit();
        double minorUnit = tickUnit / Math.max(1, getMinorTickCount());
        if (tickUnit > 0) {
            if (((upperBound - lowerBound) / minorUnit) > 10000) {
                // This is a ridiculous amount of major tick marks, something has probably gone wrong
                System.err.println("Warning we tried to create more than 10000 minor tick marks on a ObjectAxis. " + "Lower Bound=" + getLowerBound() + ", Upper Bound=" + getUpperBound() + ", Tick Unit=" + tickUnit);
                return minorTickMarks;
            }
            boolean tickUnitIsInteger = Math.rint(tickUnit) == tickUnit;
            if (tickUnitIsInteger) {
                double minor = Math.floor(lowerBound) + minorUnit;
                int count = (int) Math.ceil((Math.ceil(lowerBound) - minor) / minorUnit);
                for (int i = 0; minor < Math.ceil(lowerBound) && i < count; minor += minorUnit, i++) {
                    if (minor > lowerBound) {
                        minorTickMarks.add(minor);
                    }
                }
            }
            double major = tickUnitIsInteger ? Math.ceil(lowerBound) : lowerBound;
            int count = (int) Math.ceil((upperBound - major) / tickUnit);
            for (int i = 0; major < upperBound && i < count; major += tickUnit, i++) {
                double next = Math.min(major + tickUnit, upperBound);
                double minor = major + minorUnit;
                int minorCount = (int) Math.ceil((next - minor) / minorUnit);
                for (int j = 0; minor < next && j < minorCount; minor += minorUnit, j++) {
                    minorTickMarks.add(minor);
                }
            }
        }
        return minorTickMarks;
    }

    /**
     * Measures the size of the label for a given tick mark value. This uses the font that is set for the tick marks.
     *
     * @param value tick mark value
     * @param range range to use during calculations
     * @return size of tick mark label for given value
     */
    @Override
    protected Dimension2D measureTickMarkSize(D value, Object range) {
        Object[] rangeProps = (Object[]) range;
        String formatter = (String) rangeProps[1];
        return measureTickMarkSize(value, getTickLabelRotation(), formatter);
    }

    /**
     * Measures the size of the label for a given tick mark value. This uses the font that is set for the tick marks.
     *
     * @param value        tick mark value
     * @param rotation     The text rotation
     * @param numFormatter The number formatter
     * @return size of tick mark label for given value
     */
    protected Dimension2D measureTickMarkSize(D value, double rotation, String numFormatter) {
        StringConverter<D> formatter = getTickLabelFormatter();
        String labelText = getLabelText(formatter, value, numFormatter);
        return measureTickMarkLabelSize(labelText, rotation);
    }

    private String getLabelText(StringConverter<D> formatter,
                                D value,
                                String numFormatter) {
        if (formatter instanceof DataAxis.DefaultFormatter<D> defaultFormatter) {
            return defaultFormatter.toString(value, numFormatter);
        } else if (formatter instanceof MutableInstantAxis.DefaultFormatter defaultFormatter &&
                value instanceof MutableInstantData mutableInstantData) {
            return defaultFormatter.toString(mutableInstantData, numFormatter, UTC);
        }
        return formatter.toString(value);
    }

    protected abstract String getFormatterString(double tickUnit, double ratio);

    /**
     * Called to set the upper and lower bound and anything else that needs to be auto-ranged.
     *
     * @param minValue  The min data value that needs to be plotted on this axis
     * @param maxValue  The max data value that needs to be plotted on this axis
     * @param length    The length of the axis in display coordinates
     * @param labelSize The approximate average size a label takes along the axis
     * @return The calculated range
     */
    protected abstract Object autoRange(double minValue, double maxValue, double length, double labelSize);

    /**
     * Calculates new scale for this axis. This should not affect any properties of this axis.
     *
     * @param length     The display length of the axis
     * @param lowerBound The lower bound value
     * @param upperBound The upper bound value
     * @return new scale to fit the range from lower bound to upper bound in the given display length
     */
    protected double calculateNewScale(double length, double lowerBound, double upperBound) {
        Side side = getSide();
        double scale = (upperBound - lowerBound) == 0 ? length : length / (upperBound - lowerBound);
        if (side.isVertical()) {
            offset = length;
            return -scale;
        } else { // HORIZONTAL
            offset = 0;
            return scale;
        }
    }

    @Override
    public void invalidateRange(List<D> data) {
        if (data.isEmpty()) {
            dataMaxValue = getUpperBound();
            dataMinValue = getLowerBound();
            return;
        }
        dataMinValue = Double.MAX_VALUE;
        // We need to init to the lowest negative double (which is NOT Double.MIN_VALUE)
        // in order to find the maximum (positive or negative)
        dataMaxValue = -Double.MAX_VALUE;
        for (int i = 0; i < data.size(); i++) {
            D dataValue = data.get(i);
            dataMinValue = Math.min(dataMinValue, dataValue.getLow());
            dataMaxValue = Math.max(dataMaxValue, dataValue.getHigh());
        }
        super.invalidateRange(data);
    }

    @Override
    public void setWidth(double value) {
        super.setWidth(value);
    }

    @Override
    public void setHeight(double value) {
        super.setHeight(value);
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();
    }

    // -------------- STYLESHEET HANDLING ------------------------------------------------------------------------------

    private static class StyleableProperties {
        private static final CssMetaData<DataAxis<?>, Number> TICK_UNIT = new CssMetaData<>(CssMetaDataConstants.TICK_UNIT, SizeConverter.getInstance(), 5.0) {
            @Override
            public boolean isSettable(DataAxis<?> n) {
                return n.tickUnit == null || !n.tickUnit.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(DataAxis<?> n) {
                return (StyleableProperty<Number>) n.tickUnitProperty();
            }
        };
        private static final CssMetaData<DataAxis<?>, Number> MINOR_TICK_LENGTH = new CssMetaData<>(CssMetaDataConstants.MINOR_TICK_LENGTH, SizeConverter.getInstance(), 5.0) {
            @Override
            public boolean isSettable(DataAxis<?> n) {
                return n.minorTickLength == null || !n.minorTickLength.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(DataAxis<?> n) {
                return (StyleableProperty<Number>) n.minorTickLengthProperty();
            }
        };

        private static final CssMetaData<DataAxis<?>, Number> MINOR_TICK_COUNT = new CssMetaData<>(CssMetaDataConstants.MINOR_TICK_COUNT, SizeConverter.getInstance(), 5) {
            @Override
            public boolean isSettable(DataAxis<?> n) {
                return n.minorTickCount == null || !n.minorTickCount.isBound();
            }

            @Override
            public StyleableProperty<Number> getStyleableProperty(DataAxis<?> n) {
                return (StyleableProperty<Number>) n.minorTickCountProperty();
            }
        };

        private static final CssMetaData<DataAxis<?>, Boolean> MINOR_TICK_VISIBLE = new CssMetaData<>(CssMetaDataConstants.MINOR_TICK_VISIBLE, BooleanConverter.getInstance(), Boolean.TRUE) {
            @Override
            public boolean isSettable(DataAxis<?> n) {
                return n.minorTickVisible == null || !n.minorTickVisible.isBound();
            }

            @Override
            public StyleableProperty<Boolean> getStyleableProperty(DataAxis<?> n) {
                return (StyleableProperty<Boolean>) n.minorTickVisibleProperty();
            }
        };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Axis.getClassCssMetaData());
            styleables.add(TICK_UNIT);
            styleables.add(MINOR_TICK_COUNT);
            styleables.add(MINOR_TICK_LENGTH);
            styleables.add(MINOR_TICK_COUNT);
            styleables.add(MINOR_TICK_VISIBLE);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * Gets the {@code CssMetaData} associated with this class, which may include the
     * {@code CssMetaData} of its superclasses.
     *
     * @return the {@code CssMetaData}
     * @since JavaFX 8.0
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return DataAxis.StyleableProperties.STYLEABLES;
    }

    /**
     * {@inheritDoc}
     *
     * @since JavaFX 8.0
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return getClassCssMetaData();
    }

    // -------------- INNER CLASSES ------------------------------------------------------------------------------------

    /**
     * Default number formatter for ObjectAxis, this stays in sync with auto-ranging and formats values appropriately.
     * You can wrap this formatter to add prefixes or suffixes;
     *
     * @since JavaFX 2.0
     */
    // FIXME: maybe we should get rid of DataAxis.DefaultFormatter and rely on the sub-classes default formatter e.g. MutableInstantAxis.DefaultFormatter
    public static class DefaultFormatter<D extends AxisData<D>> extends StringConverter<D> {
        private DecimalFormat formatter;
        private String prefix = null;
        private String suffix = null;

        /**
         * Construct a DefaultFormatter for the given ObjectAxis
         *
         * @param axis The axis to format tick marks for
         */
        public DefaultFormatter(DataAxis<D> axis) {
            formatter = axis.isAutoRanging() ? new DecimalFormat(axis.currentFormatterProperty.get()) : new DecimalFormat();
            ChangeListener<Object> axisListener = (observable, oldValue, newValue) -> {
                formatter = axis.isAutoRanging() ? new DecimalFormat(axis.currentFormatterProperty.get()) : new DecimalFormat();
            };
            axis.currentFormatterProperty.addListener(axisListener);
            axis.autoRangingProperty().addListener(axisListener);
        }

        /**
         * Construct a DefaultFormatter for the given ObjectAxis with a prefix and/or suffix.
         *
         * @param axis   The axis to format tick marks for
         * @param prefix The prefix to append to the start of formatted number, can be null if not needed
         * @param suffix The suffix to append to the end of formatted number, can be null if not needed
         */
        public DefaultFormatter(DataAxis<D> axis, String prefix, String suffix) {
            this(axis);
            this.prefix = prefix;
            this.suffix = suffix;
        }

        /**
         * Converts the object provided into its string form.
         * Format of the returned string is defined by this converter.
         *
         * @return a string representation of the object passed in.
         * @see StringConverter#toString
         */
        @Override
        public String toString(D object) {
            return toString(object, formatter);
        }

        private String toString(D object, String numFormatter) {
            if (numFormatter == null || numFormatter.isEmpty()) {
                return toString(object, formatter);
            } else {
                return toString(object, new DecimalFormat(numFormatter));
            }
        }

        private String toString(D object, DecimalFormat formatter) {
            if (prefix != null && suffix != null) {
                return prefix + formatter.format(object) + suffix;
            } else if (prefix != null) {
                return prefix + formatter.format(object);
            } else if (suffix != null) {
                return formatter.format(object) + suffix;
            } else {
                return formatter.format(object);
            }
        }

        /**
         * Converts the string provided into a Number defined by this converter.
         * Format of the string and type of the resulting object is defined by this converter.
         *
         * @return a Number representation of the string passed in.
         * @see StringConverter#toString
         */
        @Override
        public D fromString(String string) {
            int prefixLength = (prefix == null) ? 0 : prefix.length();
            int suffixLength = (suffix == null) ? 0 : suffix.length();
            // FIXME: why and when is fromString needed?
            return null;
        }
    }
}
