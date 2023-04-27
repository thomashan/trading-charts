package io.github.thomashan.tradingchart.lang;

import static io.github.thomashan.tradingchart.lang.ObjectConstruction.NOT_INSTANTIABLE;

public class DoubleParser {
    private DoubleParser() {
        throw NOT_INSTANTIABLE;
    }

    public static double parseApprox(CharSequence charSequence) {
        return DoubleParserJavaLang.parseApprox(charSequence);
    }
}
