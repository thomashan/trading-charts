package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.lang.CharSequenceParser
import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc

import java.time.Instant
import java.util.function.Consumer

trait BidAskOhlcCsvParser extends NewCsvParser<BidAskOhlc> {

    @Override
    int getExpectedHeaderLength() {
        return 10
    }

    @Override
    Map<String, Consumer<CharSequence>> createSetter() {
        return ["dateTime": (CharSequence charSequence) -> ohlc.dateTime = OhlcCreator.DATE_TIME_FORMATTER.parse(charSequence, Instant::from),
                "openAsk" : (CharSequence charSequence) -> ohlc.open.ask = CharSequenceParser.parseDoubleApprox(charSequence),
                "openBid" : (CharSequence charSequence) -> ohlc.open.bid = CharSequenceParser.parseDoubleApprox(charSequence),
                "highAsk" : (CharSequence charSequence) -> ohlc.high.ask = CharSequenceParser.parseDoubleApprox(charSequence),
                "highBid" : (CharSequence charSequence) -> ohlc.high.bid = CharSequenceParser.parseDoubleApprox(charSequence),
                "lowAsk"  : (CharSequence charSequence) -> ohlc.low.ask = CharSequenceParser.parseDoubleApprox(charSequence),
                "lowBid"  : (CharSequence charSequence) -> ohlc.low.bid = CharSequenceParser.parseDoubleApprox(charSequence),
                "closeAsk": (CharSequence charSequence) -> ohlc.close.ask = CharSequenceParser.parseDoubleApprox(charSequence),
                "closeBid": (CharSequence charSequence) -> ohlc.close.bid = CharSequenceParser.parseDoubleApprox(charSequence),
                "volume"  : (CharSequence charSequence) -> ohlc.volume = CharSequenceParser.parseDoubleApprox(charSequence)
        ]
    }
}
