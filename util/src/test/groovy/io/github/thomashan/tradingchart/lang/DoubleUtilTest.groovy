package io.github.thomashan.tradingchart.lang

import org.junit.jupiter.api.Test

class DoubleUtilTest {
    @Test
    void testRound() {
        assert 1.594 == DoubleUtil.round(1.5939999999999999, 6)

        for (int i = 0; i < 16; i++) {
            assert Double.MAX_VALUE == DoubleUtil.round(Double.MAX_VALUE, i)
        }
    }
}
