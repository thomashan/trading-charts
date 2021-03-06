package io.github.thomashan.tradingchart.persistence.domain.ohlc

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc
import io.github.thomashan.tradingchart.domain.price.Price
import io.github.thomashan.tradingchart.persistence.BytesIn
import io.github.thomashan.tradingchart.persistence.ObjectReader

trait OhlcReader<P extends Price, O extends Ohlc<P>, BI extends BytesIn> extends ObjectReader<O, BI> {
}
