package io.github.thomashan.tradingchart.javafx.component.chart;

import io.github.thomashan.tradingchart.javafx.scene.chart.MidOhlcAxis;
import io.github.thomashan.tradingchart.javafx.scene.chart.MutableInstantAxis;
import io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChart;
import io.github.thomashan.tradingchart.javafx.scene.chart.OhlcDataAxis;
import io.github.thomashan.tradingchart.javafx.scene.chart.Series;
import io.github.thomashan.tradingchart.ui.data.MidOhlcData;
import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

import static javafx.scene.layout.Priority.ALWAYS;

public class ChartSection extends VBox {
    private final OhlcChart<MidOhlcData> ohlcChart;

    public ChartSection() {
        this(Map.of());
    }

    public ChartSection(Map<MutableInstantData, MidOhlcData> midOhlcData) {
        VBox.setVgrow(this, ALWAYS);
        this.ohlcChart = createOhlcChart(midOhlcData);
        VBox.setVgrow(ohlcChart, ALWAYS);
        getChildren().add(ohlcChart);
    }

    private OhlcChart<MidOhlcData> createOhlcChart(Map<MutableInstantData, MidOhlcData> midOhlcData) {
        MutableInstantAxis xAxis = new MutableInstantAxis();
        xAxis.setMinorTickCount(0);
        OhlcDataAxis<MidOhlcData> yAxis = new MidOhlcAxis();
        OhlcChart<MidOhlcData> ohlcChart = new OhlcChart<>(xAxis, yAxis);

        // setup chart
        xAxis.setLabel("Day");
        yAxis.setLabel("Price");
        // add starting data
        Series<MidOhlcData> series = createSeries(midOhlcData, "tradingChartSeries");
        setUpData(ohlcChart, series);
        return ohlcChart;
    }

    private Series<MidOhlcData> createSeries(Map<MutableInstantData, MidOhlcData> midOhlcData,
                                             String name) {
        Series<MidOhlcData> series = new Series<>();
        series.setName(name);
        for (Map.Entry<MutableInstantData, MidOhlcData> entry : midOhlcData.entrySet()) {
            MidOhlcData midOhlc = entry.getValue();
            series.getData().add(new OhlcChart.Data<>(entry.getKey(), midOhlc));
        }
        return series;
    }

    private void setUpData(OhlcChart<MidOhlcData> ohlcChart,
                           Series<MidOhlcData> series) {
        ObservableList<Series<MidOhlcData>> data = ohlcChart.getData();
        if (data == null) {
            ohlcChart.setData(FXCollections.observableList(List.of(series)));
            return;
        }
        ohlcChart.getData().add(series);
    }

    public OhlcChart<MidOhlcData> getOhlcChart() {
        return ohlcChart;
    }
}
