package io.github.thomashan.tradingchart.javafx.scene.chart

import io.github.thomashan.tradingchart.ui.data.OhlcData
import io.github.thomashan.tradingchart.ui.data.PriceData
import javafx.geometry.Side
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest

abstract class OhlcDataAxisTestCase<O extends OhlcData<O, P>, P extends PriceData<P>> extends ApplicationTest {
    private OhlcDataAxis<O> ohlcDataAxis
    private O ohlcData

    @BeforeEach
    void setUp() {
        this.ohlcDataAxis = createOhlcDataAxis()
        ohlcDataAxis.setSide(Side.LEFT)
        this.ohlcData = createOhlcData(1, 2, 0, 1.5)
        ohlcDataAxis.invalidateRange(List.of(ohlcData))
        autoRange(100)
    }

    protected abstract OhlcDataAxis<O> createOhlcDataAxis()

    protected abstract O createOhlcData(double open, double high, double low, double close)

    @Test
    void testGetTickUnit() {
        assert 0.5 == ohlcDataAxis.getTickUnit()
    }

    @Test
    void testGetUpperBound() {
        // the auto range layout will add one more tick to the highest data
        assert 2 + ohlcDataAxis.getTickUnit() == ohlcDataAxis.getUpperBound()
    }

    @Test
    void testGetLowerBound() {
        assert 0 == ohlcDataAxis.getLowerBound()
    }

    @Test
    void testGetDisplayPositionObject_Open() {
        double displayPosition = ohlcDataAxis.getDisplayPosition(ohlcData)
        assert calculateFromTop(100, ohlcData.getValue()) == displayPosition
    }

    @Test
    void testGetDisplayPositionDouble_Open() {
        double displayPosition = ohlcDataAxis.getDisplayPosition(ohlcData.open)
        assert calculateFromTop(100, ohlcData.getValue()) == displayPosition
    }

    @Test
    void testGetDisplayPosition_High() {
        double displayPosition = ohlcDataAxis.getDisplayPosition(ohlcData.high)
        assert calculateFromTop(100, ohlcData.getHigh()) == displayPosition
    }

    @Test
    void testGetDisplayPosition_Low() {
        double displayPosition = ohlcDataAxis.getDisplayPosition(ohlcData.low)
        assert calculateFromTop(100, ohlcData.getLow()) == displayPosition
    }

    @Test
    void testGetDisplayPosition_Close() {
        double displayPosition = ohlcDataAxis.getDisplayPosition(ohlcData.close)
        assert calculateFromTop(100, ohlcData.close.getValue()) == displayPosition
    }

    private autoRange(double height) {
        ohlcDataAxis.setHeight(height)
        ohlcDataAxis.layoutChildren()
    }

    private double calculateFromTop(double length, double value) {
        double valuePerPixel = (ohlcDataAxis.getUpperBound() - ohlcDataAxis.getLowerBound()) / length
        return (ohlcDataAxis.getUpperBound() - value) / valuePerPixel
    }
}
