package io.github.thomashan.tradingchart.javafx.component.menu;

public class MenuBar extends javafx.scene.control.MenuBar {
    public MenuBar() {
        getMenus().add(new FileMenu());
    }
}
