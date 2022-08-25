package io.github.thomashan.tradingchart.persistence.domain.ohlc;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.domain.price.Price;
import io.github.thomashan.tradingchart.persistence.BytesIn;
import io.github.thomashan.tradingchart.persistence.ObjectReader;

public interface OhlcReader<P extends Price<P>, O extends Ohlc<O, P>, BI extends BytesIn> extends ObjectReader<O, BI> {
}
