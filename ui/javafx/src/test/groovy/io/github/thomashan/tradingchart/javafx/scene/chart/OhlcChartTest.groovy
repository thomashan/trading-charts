package io.github.thomashan.tradingchart.javafx.scene.chart


import io.github.thomashan.tradingchart.ui.candlestick.Candle
import io.github.thomashan.tradingchart.ui.data.Granularity
import io.github.thomashan.tradingchart.ui.data.MidOhlcData
import io.github.thomashan.tradingchart.ui.data.MutableInstantData
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest

class OhlcChartTest extends ApplicationTest {
    private OhlcChart<MidOhlcData> ohlcChart

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
        ObservableList<OhlcChart.Data<MidOhlcData>> data = createObservableList(
            createData(MutableInstantData.emptyFull(), 1, 2, 0, 1.5)
        )
        setUpSingleSeriesOhlcChart(data, 100, 100)
        ohlcChart.layoutPlotChildren()

        assert ohlcChart.data
        assert 1 == ohlcChart.data.size()
        assert Series == ohlcChart.data[0].class
    }

    private void setUpSingleSeriesOhlcChart(ObservableList<OhlcChart.Data<MidOhlcData>> data,
                                            double width,
                                            double height) {
        Series<MidOhlcData> series = new Series<>(data)
        ohlcChart.setData(createObservableList(series))
        ohlcChart.getXAxis().setWidth(width)
        ohlcChart.getYAxis().setHeight(height)
        ohlcChart.updateAxisRange()
        ohlcChart.getXAxis().layoutChildren()
        ohlcChart.getYAxis().layoutChildren()
    }

    private <E> ObservableList<E> createObservableList(E... items) {
        return FXCollections.observableArrayList(items)
    }

    private OhlcChart.Data<MidOhlcData> createData(MutableInstantData instant, double open, double high, double low, double close) {
        OhlcChart.Data<MidOhlcData> data = new OhlcChart.Data<>(instant, MidOhlcData.of(open, high, low, close, 0, Granularity.D1))
        data.setNode(new Candle())
        return data
    }
}
