package io.github.thomashan.tradingchart;

public interface Copyable<O> {
    O newInstance();

    O copyFrom(O input);
}
