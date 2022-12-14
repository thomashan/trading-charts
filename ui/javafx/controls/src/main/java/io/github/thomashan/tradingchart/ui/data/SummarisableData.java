package io.github.thomashan.tradingchart.ui.data;

public interface SummarisableData<D extends SummarisableData<?>> extends Data {
    double getValue();

    double getHigh();

    double getLow();

    D setValue(double value);
}
