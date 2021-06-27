package io.github.thomashan.tradingchart.persistence.domain.ohlc

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc
import io.github.thomashan.tradingchart.domain.price.Price
import io.github.thomashan.tradingchart.persistence.BytesOut
import io.github.thomashan.tradingchart.persistence.ObjectWriter

trait OhlcWriter<P extends Price, O extends Ohlc<P>, BO extends BytesOut> extends ObjectWriter<O, BO> {
}
