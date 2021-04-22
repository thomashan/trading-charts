package io.github.thomashan.tradingchart.consumer

import java.util.function.Consumer

class Consumers {
    private Consumers() {
        throw new AssertionError(this.class.simpleName + " can't be instantiated")
    }

    static final <T> Consumer<T> noOps() {
        return (t) -> { /* */ }
    }
}
