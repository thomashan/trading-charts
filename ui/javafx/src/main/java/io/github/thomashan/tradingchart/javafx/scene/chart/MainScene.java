package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.javafx.component.MainContainer;
import javafx.scene.Scene;

public class MainScene extends Scene {
    private final MainContainer mainContainer;
    public MainScene() {
        super(new MainContainer());
        this.mainContainer = (MainContainer) getRoot();
    }

    public MainContainer getMainContainer() {
        return mainContainer;
    }
}
