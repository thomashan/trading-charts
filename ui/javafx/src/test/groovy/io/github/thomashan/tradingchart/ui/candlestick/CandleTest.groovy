package io.github.thomashan.tradingchart.ui.candlestick

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CandleTest {
    private Candle candle

    @BeforeEach
    void setUp() {
        this.candle = new Candle("", "")
    }

    @Test
    void testUpdate_OpenAboveClose() {
        candle.update(-1, 1, -1, 0)
        assert candle.openAboveClose
    }

    @Test
    void testUpdate_CloseAboveOpen() {
        candle.update(-1, 1, 1, 0)
        assert !candle.openAboveClose
    }

    @Test
    void testUpdate_OpenAndCloseEqual() {
        candle.update(0, 0, 0, 0)
        assert !candle.openAboveClose
    }

    @Test
    void testUpdate_LowAboveHigh() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException, () -> {
            candle.update(1, -1, 0, 0)
        })
        assert "High is lower than low" == exception.message
    }

    @Test
    void testUpdate_CloseAboveHigh() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException, () -> {
            candle.update(-1, 1, -2, 0)
        })
        assert "High is lower than close" == exception.message
    }

    @Test
    void testUpdate_LowAboveClose() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException, () -> {
            candle.update(-1, 1, 2, 0)
        })
        assert "Low is higher than close" == exception.message
    }

    @Test
    void testUpdate_HighLowLineHighAboveLow() {
        candle.update(-1, 1, 0, 0)
        assert -1 == candle.getHighLowLine().startY
        assert 1 == candle.getHighLowLine().endY
    }

    @Test
    void testUpdate_HighLowLineHighEqualLow() {
        candle.update(0, 0, 0, 0)
        assert 0 == candle.getHighLowLine().startY
        assert 0 == candle.getHighLowLine().endY
    }

    @Test
    void testUpdate_BarOpenAboveClose() {
        candle.update(-1, 1, 1, 0)
        assert 1 == candle.bar.height
        assert 0 == candle.bar.layoutX
        assert -1 == candle.bar.layoutY
    }

    @Test
    void testUpdate_BarOpenEqualsClose() {
        candle.update(0, 0, 0, 0)
        assert Candle.SMALLEST_BAR_HEIGHT == candle.bar.height
    }

    @Test
    void testUpdate_BarOpenLowerThanClose() {
        candle.update(-1, 1, -1, 0)
        assert 1 == candle.bar.height
        assert 0 == candle.bar.layoutX
        assert 0 == candle.bar.layoutY
    }
}
