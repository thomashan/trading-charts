package io.github.thomashan.trading.charts.app;

import io.github.thomashan.tradingchart.javafx.scene.chart.MidOhlcAxis;
import io.github.thomashan.tradingchart.javafx.scene.chart.MutableInstantAxis;
import io.github.thomashan.tradingchart.javafx.scene.chart.OhlcChart;
import io.github.thomashan.tradingchart.javafx.scene.chart.OhlcDataAxis;
import io.github.thomashan.tradingchart.javafx.scene.chart.Series;
import io.github.thomashan.tradingchart.ui.data.MidData;
import io.github.thomashan.tradingchart.ui.data.MidOhlcData;
import io.github.thomashan.tradingchart.ui.data.MutableInstantData;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.github.thomashan.tradingchart.time.MutableInstant.EPOCH;
import static io.github.thomashan.tradingchart.ui.data.Granularity.D1;

public class App extends Application {
    private static Map<MutableInstantData, MidOhlcData> midOhlcData = new LinkedHashMap<>();

    private OhlcChart<MidOhlcData> ohlcChart;
    private MutableInstantAxis xAxis;
    private OhlcDataAxis<MidOhlcData> yAxis;

    public App() {
        // leave blank for now
    }

    public static void main(String[] args) {
        // FIXME: there is an error calculating the bar width if only one data point is available
        midOhlcData.put(MutableInstantData.of(EPOCH), MidOhlcData.of(MidData.of(1), MidData.of(2), MidData.of(0), MidData.of(1.5), 0, D1));
        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(1))), MidOhlcData.of(MidData.of(1), MidData.of(2), MidData.of(0), MidData.of(1.5), 0, D1));


//        midOhlcData.put(MutableInstantData.of(EPOCH), MidOhlcData.of(MidData.of(1.58885), MidData.of(1.58971), MidData.of(1.58749), MidData.of(1.58971), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(1))), MidOhlcData.of(MidData.of(1.58940), MidData.of(1.58940), MidData.of(1.58729), MidData.of(1.58940), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(2))), MidOhlcData.of(MidData.of(1.58917), MidData.of(1.58928), MidData.of(1.58803), MidData.of(1.58928), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(3))), MidOhlcData.of(MidData.of(1.58783), MidData.of(1.58783), MidData.of(1.58506), MidData.of(1.58783), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(4))), MidOhlcData.of(MidData.of(1.58634), MidData.of(1.58724), MidData.of(1.58485), MidData.of(1.58724), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(5))), MidOhlcData.of(MidData.of(1.58650), MidData.of(1.58664), MidData.of(1.58479), MidData.of(1.58664), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(6))), MidOhlcData.of(MidData.of(1.58817), MidData.of(1.59024), MidData.of(1.58469), MidData.of(1.59024), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(7))), MidOhlcData.of(MidData.of(1.58985), MidData.of(1.59097), MidData.of(1.58826), MidData.of(1.59097), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(8))), MidOhlcData.of(MidData.of(1.59036), MidData.of(1.59119), MidData.of(1.58992), MidData.of(1.59119), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(9))), MidOhlcData.of(MidData.of(1.59204), MidData.of(1.59204), MidData.of(1.59029), MidData.of(1.59204), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(10))), MidOhlcData.of(MidData.of(1.59194), MidData.of(1.59252), MidData.of(1.59082), MidData.of(1.59252), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(11))), MidOhlcData.of(MidData.of(1.59304), MidData.of(1.59422), MidData.of(1.59094), MidData.of(1.59422), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(12))), MidOhlcData.of(MidData.of(1.59312), MidData.of(1.59369), MidData.of(1.59311), MidData.of(1.59369), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(13))), MidOhlcData.of(MidData.of(1.59320), MidData.of(1.59390), MidData.of(1.59289), MidData.of(1.59390), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(14))), MidOhlcData.of(MidData.of(1.59294), MidData.of(1.59386), MidData.of(1.59255), MidData.of(1.59386), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(15))), MidOhlcData.of(MidData.of(1.59262), MidData.of(1.59310), MidData.of(1.59207), MidData.of(1.59310), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(16))), MidOhlcData.of(MidData.of(1.59325), MidData.of(1.59341), MidData.of(1.59214), MidData.of(1.59341), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(17))), MidOhlcData.of(MidData.of(1.59332), MidData.of(1.59407), MidData.of(1.59258), MidData.of(1.59407), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(18))), MidOhlcData.of(MidData.of(1.59264), MidData.of(1.59312), MidData.of(1.59263), MidData.of(1.59312), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(19))), MidOhlcData.of(MidData.of(1.59313), MidData.of(1.59362), MidData.of(1.59257), MidData.of(1.59362), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(20))), MidOhlcData.of(MidData.of(1.59318), MidData.of(1.59345), MidData.of(1.59191), MidData.of(1.59345), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(21))), MidOhlcData.of(MidData.of(1.59184), MidData.of(1.59184), MidData.of(1.58889), MidData.of(1.59184), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(22))), MidOhlcData.of(MidData.of(1.59100), MidData.of(1.59117), MidData.of(1.58878), MidData.of(1.59117), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(23))), MidOhlcData.of(MidData.of(1.59105), MidData.of(1.59206), MidData.of(1.59065), MidData.of(1.59206), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(24))), MidOhlcData.of(MidData.of(1.59104), MidData.of(1.59270), MidData.of(1.59070), MidData.of(1.59270), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(25))), MidOhlcData.of(MidData.of(1.59098), MidData.of(1.59119), MidData.of(1.58958), MidData.of(1.59119), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(26))), MidOhlcData.of(MidData.of(1.58965), MidData.of(1.58987), MidData.of(1.58934), MidData.of(1.58987), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(27))), MidOhlcData.of(MidData.of(1.58927), MidData.of(1.58982), MidData.of(1.58851), MidData.of(1.58982), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(28))), MidOhlcData.of(MidData.of(1.58846), MidData.of(1.58946), MidData.of(1.58821), MidData.of(1.58946), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(29))), MidOhlcData.of(MidData.of(1.58828), MidData.of(1.58883), MidData.of(1.58776), MidData.of(1.58883), 0, D1));
//        midOhlcData.put(MutableInstantData.of(EPOCH.newInstance().plus(Duration.ofDays(30))), MidOhlcData.of(MidData.of(1.58769), MidData.of(1.58800), MidData.of(1.58654), MidData.of(1.58800), 0, D1));

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private Parent createContent() {
        xAxis = new MutableInstantAxis();
        xAxis.setMinorTickCount(0);
        yAxis = new MidOhlcAxis();
        ohlcChart = new OhlcChart<>(xAxis, yAxis);

        // setup chart
        xAxis.setLabel("Day");
        yAxis.setLabel("Price");
        // add starting data
        Series<MidOhlcData> series = new Series<>();
        series.setName("tradingChartSeries");
        for (Map.Entry<MutableInstantData, MidOhlcData> entry : midOhlcData.entrySet()) {
            final MidOhlcData midOhlc = entry.getValue();
            series.getData().add(new OhlcChart.Data<>(entry.getKey(), midOhlc));
        }
        ObservableList<Series<MidOhlcData>> data = ohlcChart.getData();
        if (data == null) {
            data = FXCollections.observableList(List.of(series));
            ohlcChart.setData(data);
        } else {
            ohlcChart.getData().add(series);
        }
        return ohlcChart;
    }
}
