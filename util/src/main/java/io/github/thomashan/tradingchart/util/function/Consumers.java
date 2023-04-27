package io.github.thomashan.tradingchart.util.function;

import java.util.function.Consumer;

import static io.github.thomashan.tradingchart.lang.ObjectConstruction.NOT_INSTANTIABLE;

public class Consumers {
    @SuppressWarnings("rawtypes")
    private static final Consumer nullConsumer = o -> { /* */ };

    private Consumers() {
        throw NOT_INSTANTIABLE;
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> nullConsumer() {
        return (Consumer<T>) nullConsumer;
    }
}
