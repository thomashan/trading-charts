package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.Granularity;

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

public class MutableInstantFormatter {
    private static final String YEAR = "yyyy";
    private static final String YEAR_MONTH = "yyyy-MM";
    private static final String YEAR_MONTH_DAY = "yyyy-MM-dd";
    private static final String YEAR_MONTH_DAY_HOUR = "yyyy-MM-dd'T'HH:mm";
    private static final String YEAR_MONTH_DAY_HOUR_SECOND = "yyyy-MM-dd'T'HH:mm:ss";
    public static final Map<Granularity, String> TICK_FORMATTER_STRINGS = new HashMap<>();

    static {
        TICK_FORMATTER_STRINGS.put(M12, YEAR);
        TICK_FORMATTER_STRINGS.put(M6, YEAR);
        TICK_FORMATTER_STRINGS.put(M3, YEAR);
        TICK_FORMATTER_STRINGS.put(M1, YEAR_MONTH);
        TICK_FORMATTER_STRINGS.put(W1, YEAR_MONTH);
        TICK_FORMATTER_STRINGS.put(D1, YEAR_MONTH_DAY);
        TICK_FORMATTER_STRINGS.put(H12, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(H6, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(H4, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(H3, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(H2, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(H1, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m45, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m30, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m15, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m10, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m5, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m3, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m2, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(m1, YEAR_MONTH_DAY_HOUR);
        TICK_FORMATTER_STRINGS.put(S30, YEAR_MONTH_DAY_HOUR_SECOND);
        TICK_FORMATTER_STRINGS.put(S15, YEAR_MONTH_DAY_HOUR_SECOND);
        TICK_FORMATTER_STRINGS.put(S10, YEAR_MONTH_DAY_HOUR_SECOND);
        TICK_FORMATTER_STRINGS.put(S5, YEAR_MONTH_DAY_HOUR_SECOND);
        TICK_FORMATTER_STRINGS.put(S1, YEAR_MONTH_DAY_HOUR_SECOND);
    }
}
