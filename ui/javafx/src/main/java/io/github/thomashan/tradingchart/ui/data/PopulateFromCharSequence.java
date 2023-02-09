package io.github.thomashan.tradingchart.ui.data;

public interface PopulateFromCharSequence<O> {
    O populate(CharSequence charSequence);
}
