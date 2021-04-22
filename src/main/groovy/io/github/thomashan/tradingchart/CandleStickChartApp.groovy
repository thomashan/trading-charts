package io.github.thomashan.tradingchart

import groovy.transform.CompileStatic
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage

@CompileStatic
class CandleStickChartApp extends Application {
    // DAY, OPEN, CLOSE, HIGH, LOW, AVERAGE
    private static double[][] data = new double[][]{
            [1, 25, 20, 32, 16, 20] as double[],
            [2, 26, 30, 33, 22, 25] as double[],
            [3, 30, 38, 40, 20, 32] as double[],
            [4, 24, 30, 34, 22, 30] as double[],
            [5, 26, 36, 40, 24, 32] as double[],
            [6, 28, 38, 45, 25, 34] as double[],
            [7, 36, 30, 44, 28, 39] as double[],
            [8, 30, 18, 36, 16, 31] as double[],
            [9, 40, 50, 52, 36, 41] as double[],
            [10, 30, 34, 38, 28, 36] as double[],
            [11, 24, 12, 30, 8, 32.4] as double[],
            [12, 28, 40, 46, 25, 31.6] as double[],
            [13, 28, 18, 36, 14, 32.6] as double[],
            [14, 38, 30, 40, 26, 30.6] as double[],
            [15, 28, 33, 40, 28, 30.6] as double[],
            [16, 25, 10, 32, 6, 30.1] as double[],
            [17, 26, 30, 42, 18, 27.3] as double[],
            [18, 20, 18, 30, 10, 21.9] as double[],
            [19, 20, 10, 30, 5, 21.9] as double[],
            [20, 26, 16, 32, 10, 17.9] as double[],
            [21, 38, 40, 44, 32, 18.9] as double[],
            [22, 26, 40, 41, 12, 18.9] as double[],
            [23, 30, 18, 34, 10, 18.9] as double[],
            [24, 12, 23, 26, 12, 18.2] as double[],
            [25, 30, 40, 45, 16, 18.9] as double[],
            [26, 25, 35, 38, 20, 21.4] as double[],
            [27, 24, 12, 30, 8, 19.6] as double[],
            [28, 23, 44, 46, 15, 22.2] as double[],
            [29, 30, 18, 38, 14, 23] as double[],
            [30, 28, 16, 36, 12, 21.2] as double[],
            [31, 26, 14, 30, 10, 20] as double[]
    }

    private CandleStickChart chart
    private NumberAxis xAxis
    private NumberAxis yAxis

    Parent createContent() {
        xAxis = new NumberAxis(0, 32, 1)
        xAxis.setMinorTickCount(0)
        yAxis = new NumberAxis()
        chart = new CandleStickChart(xAxis, yAxis)
        // setup chart
        xAxis.setLabel("Day")
        yAxis.setLabel("Price")
        // add starting data
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>()
        for (int i = 0; i < data.length; i++) {
            double[] day = data[i]
            final CandleStickExtraValues extras =
                    new CandleStickExtraValues(day[2], day[3], day[4], day[5])
            series.getData().add(new XYChart.Data<Number, Number>(day[0], day[1],
                    extras))
        }
        ObservableList<XYChart.Series<Number, Number>> data = chart.getData()
        if (data == null) {
            data = FXCollections.observableArrayList(series)
            chart.setData(data)
        } else {
            chart.getData().add(series)
        }
        return chart
    }

    @Override
    void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()))
        primaryStage.show()
    }

    /**
     * Java main for when running without JavaFX launcher
     */
    static void main(String[] args) { launch(args) }
}
