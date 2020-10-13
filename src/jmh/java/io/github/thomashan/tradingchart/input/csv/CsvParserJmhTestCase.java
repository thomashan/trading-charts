package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.ohlc.Ohlc;
import io.github.thomashan.tradingchart.price.BidAsk;
import org.openjdk.jmh.annotations.*;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class CsvParserJmhTestCase<C extends CsvParser> {
    public abstract C getCsvParser();

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testParse_InputStream(ThreadState threadState) {
        Stream<Ohlc<BidAsk>> ohlcStream = getCsvParser().parse(threadState.inputStream);
    }

    @State(Scope.Thread)
    public static class ThreadState {
        // need to put this into maven repo
        public InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("EURUSD-S5-csv-0.0.1.csv");
    }
}
