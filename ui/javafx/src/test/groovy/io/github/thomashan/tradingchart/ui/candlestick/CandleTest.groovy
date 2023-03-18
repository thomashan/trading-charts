package io.github.thomashan.tradingchart.ui.candlestick

import io.github.thomashan.tradingchart.ui.data.MutableInstantData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CandleTest {
    private Candle candle
    private MutableInstantData mutableInstantData

    @BeforeEach
    void setUp() {
        this.candle = new Candle()
        this.mutableInstantData = MutableInstantData.emptyFull()
    }

    @Test
    void testUpdate_OpenAboveClose() {
        candle.update(-1, 1, -1, 0, mutableInstantData)
        assert candle.openAboveClose
    }

    @Test
    void testUpdate_CloseAboveOpen() {
        candle.update(-1, 1, 1, 0, mutableInstantData)
        assert !candle.openAboveClose
    }

    @Test
    void testUpdate_OpenAndCloseEqual() {
        candle.update(0, 0, 0, 0, mutableInstantData)
        assert !candle.openAboveClose
    }

    @Test
    void testUpdate_LowAboveHigh() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException, () -> {
            candle.update(1, -1, 0, 0, mutableInstantData)
        })
        assert "High is lower than low @1970-01-01T00:00:00Z" == exception.message
    }

    @Test
    void testUpdate_CloseAboveHigh() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException, () -> {
            candle.update(-1, 1, -2, 0, mutableInstantData)
        })
        assert "High is lower than close @1970-01-01T00:00:00Z" == exception.message
    }

    @Test
    void testUpdate_LowAboveClose() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException, () -> {
            candle.update(-1, 1, 2, 0, mutableInstantData)
        })
        assert "Low is higher than close @1970-01-01T00:00:00Z" == exception.message
    }

    @Test
    void testUpdate_HighLowLineHighAboveLow() {
        candle.update(-1, 1, 0, 0, mutableInstantData)
        assert -1 == candle.getHighLowLine().startY
        assert 1 == candle.getHighLowLine().endY
    }

    @Test
    void testUpdate_HighLowLineHighEqualLow() {
        candle.update(0, 0, 0, 0, mutableInstantData)
        assert 0 == candle.getHighLowLine().startY
        assert 0 == candle.getHighLowLine().endY
    }

    @Test
    void testUpdate_BarOpenAboveClose() {
        candle.update(-1, 1, 1, 0, mutableInstantData)
        assert 1 == candle.bar.height
        assert 0 == candle.bar.layoutX
        assert -1 == candle.bar.layoutY
    }

    @Test
    void testUpdate_BarOpenEqualsClose() {
        candle.update(0, 0, 0, 0, mutableInstantData)
        assert Candle.SMALLEST_BAR_HEIGHT == candle.bar.height
    }

    @Test
    void testUpdate_BarOpenLowerThanClose() {
        candle.update(-1, 1, -1, 0, mutableInstantData)
        assert 1 == candle.bar.height
        assert 0 == candle.bar.layoutX
        assert 0 == candle.bar.layoutY
    }
}
