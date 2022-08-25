package io.github.thomashan.tradingchart.persistence.domain.ohlc;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.domain.price.Price;
import io.github.thomashan.tradingchart.persistence.BytesOut;
import io.github.thomashan.tradingchart.persistence.ObjectWriter;

public interface OhlcWriter<P extends Price<P>, O extends Ohlc<O, P>, BO extends BytesOut<?>> extends ObjectWriter<O, BO> {
    @Override
    void close();
}
