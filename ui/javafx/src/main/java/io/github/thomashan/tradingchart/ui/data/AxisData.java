package io.github.thomashan.tradingchart.ui.data;

import io.github.thomashan.tradingchart.Copyable;

public interface AxisData<D extends AxisData<?>> extends
        Copyable<D>,
        CalculableData<D>,
        SummarisableData<D>,
        PopulateFromCharSequence<D> {
}
