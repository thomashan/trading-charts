package io.github.thomashan.tradingchart.javafx.scene.chart

import io.github.thomashan.tradingchart.ui.data.Granularity
import org.junit.jupiter.api.Test

class GranularityToMutableInstantTest {
    @Test
    void testGetMutableInstant() {
        for (Granularity granularity : Granularity.values()) {
            assert GranularityToMutableInstant.getMutableInstant(granularity)
        }
    }
}
