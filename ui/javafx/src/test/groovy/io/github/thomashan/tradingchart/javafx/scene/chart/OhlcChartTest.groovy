package io.github.thomashan.tradingchart.javafx.scene.chart

import io.github.thomashan.tradingchart.ui.candlestick.Candle
import io.github.thomashan.tradingchart.ui.data.Granularity
import io.github.thomashan.tradingchart.ui.data.MidOhlcData
import io.github.thomashan.tradingchart.ui.data.MutableInstantData
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Point2D
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.Window
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxToolkit
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.ApplicationTest
import org.testfx.framework.junit5.Start

@ExtendWith(ApplicationExtension.class)
class OhlcChartTest extends ApplicationTest {
    private OhlcChart<MidOhlcData> ohlcChart
    private Stage stage

    @Start
    @Override
    void start(Stage stage) throws Exception {
        this.ohlcChart = new OhlcChart<>(new MutableInstantAxis(), new MidOhlcAxis())
        this.stage = stage
        this.stage.setScene(new Scene(ohlcChart))
        this.stage.show()
    }

    @Test
    void testLayoutPlotChildren_NoData() {
        ohlcChart.layoutPlotChildren()
        assert !ohlcChart.getData()
    }

    @Test
    void testLayoutPlotChildren_Data() {
        FxToolkit.setupFixture {
            ObservableList<OhlcChart.Data<MidOhlcData>> data = createObservableList(createData(MutableInstantData.emptyFull(), 1, 2, 0, 1.5))
            setUpSingleSeriesOhlcChart(data, 100, 100)
        }
        ohlcChart.layoutPlotChildren()

        assert ohlcChart.data
        assert 1 == ohlcChart.data.size()
        assert Series == ohlcChart.data[0].class
    }

    @Disabled("find a way to verify the current mouse location's cursor style")
    @Test
    void testCursorIsCrosshairOnlyOnPlotBackground() {
        FxToolkit.setupFixture {
            ObservableList<OhlcChart.Data<MidOhlcData>> data = createObservableList(createData(MutableInstantData.emptyFull(), 1, 2, 0, 1.5))
            setUpSingleSeriesOhlcChart(data, 100, 100)
        }
        ohlcChart.layoutPlotChildren()

        Point2D point2D = robotContext().baseRobot.retrieveMouse()
        Cursor cursor = stage.scene.cursor
        List<Window> windows = listWindows()
        FxToolkit.setupFixture {
            ohlcChart.getXAxis().requestFocus()
            clickOn(ohlcChart.getXAxis())
            moveTo(ohlcChart)
        }

        // FIXME: is there a way of getting the current mouse's cursor style?
        assert false
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

    private static <E> ObservableList<E> createObservableList(E... items) {
        return FXCollections.observableArrayList(items)
    }

    private static OhlcChart.Data<MidOhlcData> createData(MutableInstantData instant, double open, double high, double low, double close) {
        OhlcChart.Data<MidOhlcData> data = new OhlcChart.Data<>(instant, MidOhlcData.of(open, high, low, close, 0, Granularity.D1))
        data.setNode(new Candle())
        return data
    }
}
