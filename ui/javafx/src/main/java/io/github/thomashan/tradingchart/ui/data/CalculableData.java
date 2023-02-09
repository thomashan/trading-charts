package io.github.thomashan.tradingchart.ui.data;

public interface CalculableData<D extends CalculableData<?>> extends Data {
    D add(double value);

    D minus(double value);
}
