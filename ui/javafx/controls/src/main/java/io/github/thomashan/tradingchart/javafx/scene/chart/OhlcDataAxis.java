package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.OhlcData;

import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.NUMBER_FORMAT_HIGH_EXPONENT;
import static io.github.thomashan.tradingchart.javafx.scene.chart.AxisConstants.NUMBER_FORMAT_LOW_EXPONENT;

public abstract class OhlcDataAxis<O extends OhlcData<O, ?>> extends DataAxis<O> {
    @Override
    protected String getFormatterString(double tickUnit, double ratio) {
        int exp = (int) Math.floor(Math.log10(tickUnit));
        if (exp > 1) {
            return NUMBER_FORMAT_HIGH_EXPONENT;
        } else if (exp == 1) {
            return NUMBER_FORMAT_LOW_EXPONENT;
        }
        final boolean ratioHasFrac = Math.rint(ratio) != ratio;
        final StringBuilder formatterB = new StringBuilder(NUMBER_FORMAT_LOW_EXPONENT);
        int n = ratioHasFrac ? Math.abs(exp) + 1 : Math.abs(exp);
        if (n > 0) formatterB.append(".");
        for (int i = 0; i < n; ++i) {
            formatterB.append("0");
        }
        return formatterB.toString();
    }
}
