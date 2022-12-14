package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.time.MutableInstant;
import io.github.thomashan.tradingchart.ui.data.Granularity;
import io.github.thomashan.tradingchart.ui.data.MutableInstantData;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.github.thomashan.tradingchart.ui.data.Granularity.D1;
import static io.github.thomashan.tradingchart.ui.data.Granularity.H1;
import static io.github.thomashan.tradingchart.ui.data.Granularity.H12;
import static io.github.thomashan.tradingchart.ui.data.Granularity.H2;
import static io.github.thomashan.tradingchart.ui.data.Granularity.H3;
import static io.github.thomashan.tradingchart.ui.data.Granularity.H4;
import static io.github.thomashan.tradingchart.ui.data.Granularity.H6;
import static io.github.thomashan.tradingchart.ui.data.Granularity.M1;
import static io.github.thomashan.tradingchart.ui.data.Granularity.M12;
import static io.github.thomashan.tradingchart.ui.data.Granularity.M3;
import static io.github.thomashan.tradingchart.ui.data.Granularity.M6;
import static io.github.thomashan.tradingchart.ui.data.Granularity.S1;
import static io.github.thomashan.tradingchart.ui.data.Granularity.S10;
import static io.github.thomashan.tradingchart.ui.data.Granularity.S15;
import static io.github.thomashan.tradingchart.ui.data.Granularity.S30;
import static io.github.thomashan.tradingchart.ui.data.Granularity.S5;
import static io.github.thomashan.tradingchart.ui.data.Granularity.W1;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m1;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m10;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m15;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m2;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m3;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m30;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m45;
import static io.github.thomashan.tradingchart.ui.data.Granularity.m5;

public class GranularityToMutableInstant {
    private static final Map<Granularity, MutableInstantData> MUTABLE_INSTANT = new HashMap<>();

    static {
        MUTABLE_INSTANT.put(M12, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofDays(365)).toImmutable()));
        MUTABLE_INSTANT.put(M6, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofDays(182)).toImmutable()));
        MUTABLE_INSTANT.put(M3, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofDays(91)).toImmutable()));
        MUTABLE_INSTANT.put(M1, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofDays(30)).toImmutable()));
        MUTABLE_INSTANT.put(W1, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofDays(7)).toImmutable()));
        MUTABLE_INSTANT.put(D1, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofDays(1)).toImmutable()));
        MUTABLE_INSTANT.put(H12, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofHours(12)).toImmutable()));
        MUTABLE_INSTANT.put(H6, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofHours(6)).toImmutable()));
        MUTABLE_INSTANT.put(H4, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofHours(4)).toImmutable()));
        MUTABLE_INSTANT.put(H3, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofHours(3)).toImmutable()));
        MUTABLE_INSTANT.put(H2, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofHours(2)).toImmutable()));
        MUTABLE_INSTANT.put(H1, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofHours(1)).toImmutable()));
        MUTABLE_INSTANT.put(m45, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(45)).toImmutable()));
        MUTABLE_INSTANT.put(m30, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(30)).toImmutable()));
        MUTABLE_INSTANT.put(m15, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(15)).toImmutable()));
        MUTABLE_INSTANT.put(m10, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(10)).toImmutable()));
        MUTABLE_INSTANT.put(m5, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(5)).toImmutable()));
        MUTABLE_INSTANT.put(m3, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(3)).toImmutable()));
        MUTABLE_INSTANT.put(m2, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(2)).toImmutable()));
        MUTABLE_INSTANT.put(m1, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofMinutes(1)).toImmutable()));
        MUTABLE_INSTANT.put(S30, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofSeconds(30)).toImmutable()));
        MUTABLE_INSTANT.put(S15, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofSeconds(15)).toImmutable()));
        MUTABLE_INSTANT.put(S10, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofSeconds(10)).toImmutable()));
        MUTABLE_INSTANT.put(S5, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofSeconds(5)).toImmutable()));
        MUTABLE_INSTANT.put(S1, MutableInstantData.of(MutableInstant.EPOCH.newInstance().plus(Duration.ofSeconds(1)).toImmutable()));
    }

    private GranularityToMutableInstant() {
        throw new AssertionError("not instantiable");
    }

    public static MutableInstantData getMutableInstant(Granularity granularity) {
        if (!MUTABLE_INSTANT.containsKey(granularity)) {
            throw new IllegalArgumentException("unknown granularity: " + granularity);
        }
        return MUTABLE_INSTANT.get(granularity);
    }
}
