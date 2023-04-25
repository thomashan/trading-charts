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
        int numberOfTicks = 1
        Object[] autoRangeObjects = createAutoRangeObjects(numberOfTicks)
        assertAutoRangeObjects(numberOfTicks, autoRangeObjects)
    }

    @Test
    void testAutoRange_TwoTicks() {
        int numberOfTicks = 2
        Object[] autoRangeObjects = createAutoRangeObjects(numberOfTicks)
        assertAutoRangeObjects(numberOfTicks, autoRangeObjects)
    }

    @Test
    void testAutoRange_MoreThanTwentyTicks() {
        int numberOfTicks = 21
        Object[] autoRangeObjects = createAutoRangeObjects(numberOfTicks)
        assertAutoRangeObjects(numberOfTicks, autoRangeObjects)
    }

    private Object[] createAutoRangeObjects(int numberOfTicks) {
        return mutableInstantAxis.autoRange(0, (numberOfTicks - 1) * D1_VALUE, 100, 10)
    }

    private void assertAutoRangeObjects(int numberOfTicks, Object[] autoRangeObjects) {
        assert -D1_VALUE / 2 == autoRangeObjects[0][0]
        assert (2 * numberOfTicks - 1) * D1_VALUE / 2 == autoRangeObjects[0][1]
        assert D1_VALUE == autoRangeObjects[0][2]
        assert 100 / (numberOfTicks * D1_VALUE) == autoRangeObjects[0][3]
        assert "yyyy-MM-dd" == autoRangeObjects[1]
    }
}
