package io.github.thomashan.tradingchart.lang;

public class DoubleParser {
    private DoubleParser() {
        throw new AssertionError("not instantiable");
    }

    public static double parseApprox(CharSequence charSequence) {
        return DoubleParserJavaLang.parseApprox(charSequence);
    }
}
