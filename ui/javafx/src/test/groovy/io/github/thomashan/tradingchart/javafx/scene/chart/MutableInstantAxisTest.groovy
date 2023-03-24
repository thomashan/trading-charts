package io.github.thomashan.tradingchart.javafx.scene.chart

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest

import static io.github.thomashan.tradingchart.ui.data.Granularity.D1
import static javafx.geometry.Side.BOTTOM

class MutableInstantAxisTest extends ApplicationTest {
    private static final D1_VALUE = GranularityToMutableInstant.getMutableInstant(D1).getValue()
    private MutableInstantAxis mutableInstantAxis

    @BeforeEach
    void setUp() {
        this.mutableInstantAxis = new MutableInstantAxis()
        mutableInstantAxis.setSide(BOTTOM)
    }

    @Test
    void testAutoRange_OneTick() {
        Object[] autoRangeObjects = mutableInstantAxis.autoRange(0, 0, 100, 10)

        assert -D1_VALUE / 2 == autoRangeObjects[0]
        assert D1_VALUE / 2 == autoRangeObjects[1]
        assert D1_VALUE == autoRangeObjects[2]
        assert 100 / D1_VALUE == autoRangeObjects[3]
        assert "yyyy-MM-dd" == autoRangeObjects[4]
    }

    @Test
    void testAutoRange_TwoTicks() {
        Object[] autoRangeObjects = mutableInstantAxis.autoRange(0, D1_VALUE, 100, 10)

        assert -D1_VALUE / 2 == autoRangeObjects[0]
        assert 3 * D1_VALUE / 2 == autoRangeObjects[1]
        assert D1_VALUE == autoRangeObjects[2]
        assert 100 / (2 * D1_VALUE) == autoRangeObjects[3]
        assert "yyyy-MM-dd" == autoRangeObjects[4]
    }

    @Test
    void testAutoRange_MoreThanTwentyTicks() {

    }
}
