package io.github.thomashan.tradingchart.javafx.scene.chart

import com.sun.javafx.collections.ObservableListWrapper
import io.github.thomashan.tradingchart.ui.data.MidOhlcData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest

class OhlcChartTest extends ApplicationTest {
    private OhlcChart<MidOhlcData> ohlcChart;

    @BeforeEach
    void setUp() {
        this.ohlcChart = new OhlcChart<>(new MutableInstantAxis(), new MidOhlcAxis())
    }

    @Test
    void testLayoutPlotChildren_NoData() {
        ohlcChart.layoutPlotChildren()
        assert !ohlcChart.getData()
    }

    @Test
    void testLayoutPlotChildren_Data() {
        Series<MidOhlcData> series = new Series<MidOhlcData>()
        ohlcChart.data = new ObservableListWrapper<>(new ArrayList<>())
        ohlcChart.getData().add(series)
        ohlcChart.layoutPlotChildren()
        assert ohlcChart.data
        assert 1 == ohlcChart.data.size()
        assert Series == ohlcChart.data[0].class
    }
}
