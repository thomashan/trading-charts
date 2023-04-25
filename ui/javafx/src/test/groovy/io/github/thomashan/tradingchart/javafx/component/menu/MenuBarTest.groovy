package io.github.thomashan.tradingchart.javafx.component.menu


import javafx.scene.control.Menu
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest

class MenuBarTest extends ApplicationTest {
    private MenuBar menuBar

    @BeforeEach
    void setUp() {
        this.menuBar = new MenuBar()
    }

    @Test
    void testMenu() {
        assert 1 == menuBar.getMenus().size()
        assertMenus(List.of("File"), menuBar)
        assertMenuItems(List.of("Open..."), menuBar.getMenus().get(0))
    }

    private static void assertMenus(List<String> menus, javafx.scene.control.MenuBar menuBar) {
        assert menus.size() == menuBar.getMenus().size()
        for (int i = 0; i < menus.size(); i++) {
            assert menus[i] == menuBar.getMenus().get(i).getText()
        }
    }

    private static void assertMenuItems(List<String> menuItems, Menu menu) {
        assert menuItems.size() == menu.getItems().size()
        for (int i = 0; i < menuItems.size(); i++) {
            assert menuItems[i] == menu.getItems().get(i).getText()
        }
    }
}
