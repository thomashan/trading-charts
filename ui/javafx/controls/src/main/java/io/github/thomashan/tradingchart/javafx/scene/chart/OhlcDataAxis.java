package io.github.thomashan.tradingchart.javafx.scene.chart;

import io.github.thomashan.tradingchart.ui.data.OhlcData;

public abstract class OhlcDataAxis<O extends OhlcData<O, ?>> extends DataAxis<O> {
    @Override
    protected String getFormatterString(double tickUnit, double ratio) {
        int exp = (int) Math.floor(Math.log10(tickUnit));
        if (exp > 1) {
            return "#,##0";
        } else if (exp == 1) {
            return "0";
        }
        final boolean ratioHasFrac = Math.rint(ratio) != ratio;
        final StringBuilder formatterB = new StringBuilder("0");
        int n = ratioHasFrac ? Math.abs(exp) + 1 : Math.abs(exp);
        if (n > 0) formatterB.append(".");
        for (int i = 0; i < n; ++i) {
            formatterB.append("0");
        }
        return formatterB.toString();
    }
}
