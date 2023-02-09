package io.github.thomashan.tradingchart.javafx.scene.chart

import io.github.thomashan.tradingchart.ui.data.Granularity
import org.junit.jupiter.api.Test

import static io.github.thomashan.tradingchart.javafx.scene.chart.GranularityToMutableInstant.getMutableInstant

class GranularityToMutableInstantTest {
    @Test
    void testGetMutableInstant() {
        for (Granularity granularity : Granularity.values()) {
            assert getMutableInstant(granularity)
        }
    }
}
