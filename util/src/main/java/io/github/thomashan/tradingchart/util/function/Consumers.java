package io.github.thomashan.tradingchart.util.function;

import java.util.function.Consumer;

public class Consumers {
    @SuppressWarnings("rawtypes")
    private static final Consumer nullConsumer = o -> { /* */ };

    private Consumers() {
        throw new AssertionError("not instantiable");
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> nullConsumer() {
        return (Consumer<T>) nullConsumer;
    }
}
