package io.github.thomashan.tradingchart.ui.data;

import static io.github.thomashan.tradingchart.lang.ObjectConstruction.NOT_INSTANTIABLE;

public class StringBuilderFactory {
    public static final StringBuilder INSTANCE = new StringBuilder();

    private StringBuilderFactory() {
        throw NOT_INSTANTIABLE;
    }
}
