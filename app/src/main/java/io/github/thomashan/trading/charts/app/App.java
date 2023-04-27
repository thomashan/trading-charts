package io.github.thomashan.trading.charts.app;

import io.github.thomashan.tradingchart.javafx.component.chart.ChartSection;
import io.github.thomashan.tradingchart.javafx.component.menu.MenuSection;
import io.github.thomashan.tradingchart.javafx.scene.chart.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public App() {
        // leave blank for now
    }

    public static void main(String[] args) {
        App app = new App();
        app.start(args);
    }

    public void start(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Trading Charts by @thomashan");
        MainScene mainScene = new MainScene();
        mainScene.getMainContainer().getChildren().add(new MenuSection());
        mainScene.getMainContainer().getChildren().add(new ChartSection(TestData.getMidOhlcData()));
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
