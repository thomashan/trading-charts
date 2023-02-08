package io.github.thomashan.tradingchart.ui.data;

public class StringBuilderFactory {
    public static final StringBuilder INSTANCE = new StringBuilder();

    private StringBuilderFactory() {
        throw new AssertionError("not instantiable");
    }
}
