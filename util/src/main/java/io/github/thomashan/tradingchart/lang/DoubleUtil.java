package io.github.thomashan.tradingchart.lang;

import java.util.HashMap;
import java.util.Map;

import static io.github.thomashan.tradingchart.lang.ObjectConstruction.NOT_INSTANTIABLE;

public class DoubleUtil {
    private static final Map<Integer, Double> DECIMAL_PLACE_CACHE = new HashMap<>(Map.of(15, Math.pow(10, 15)));
    private static final Map<Integer, Double> LARGEST_ROUNDABLE_VALUES = new HashMap<>();

    static {
        LARGEST_ROUNDABLE_VALUES.put(1, 9.2233720368547763E170);
        LARGEST_ROUNDABLE_VALUES.put(2, 9.223372036854776E160);
        LARGEST_ROUNDABLE_VALUES.put(3, 9.223372036854776E150);
        LARGEST_ROUNDABLE_VALUES.put(4, 9.223372036854776E140);
        LARGEST_ROUNDABLE_VALUES.put(5, 9.223372036854777E130);
        LARGEST_ROUNDABLE_VALUES.put(6, 9.223372036854775E120);
        LARGEST_ROUNDABLE_VALUES.put(7, 9.223372036854775E110);
        LARGEST_ROUNDABLE_VALUES.put(8, 9.223372036854776E100);
        LARGEST_ROUNDABLE_VALUES.put(9, 9.223372036854776E90);
        LARGEST_ROUNDABLE_VALUES.put(10, 9.223372036854776E80);
        LARGEST_ROUNDABLE_VALUES.put(11, 9.223372036854775E70);
        LARGEST_ROUNDABLE_VALUES.put(12, 9223372.0368547760);
        LARGEST_ROUNDABLE_VALUES.put(13, 922337.20368547760);
        LARGEST_ROUNDABLE_VALUES.put(14, 92233.720368547760);
        LARGEST_ROUNDABLE_VALUES.put(15, 9223.3720368547770);
    }

    private DoubleUtil() {
        throw NOT_INSTANTIABLE;
    }

    public static double round(double value, int decimalPlaces) {
        if (decimalPlaces > 15 || decimalPlaces < 1) {
            return value;
        }
        double largestRoundableValue = LARGEST_ROUNDABLE_VALUES.getOrDefault(decimalPlaces, Double.MAX_VALUE);
        if (value > largestRoundableValue) {
            return value;
        }
        double multiplier = DECIMAL_PLACE_CACHE.computeIfAbsent(decimalPlaces, dp -> Math.pow(10, dp));
        return Math.round(value * multiplier) / multiplier;
    }
}
