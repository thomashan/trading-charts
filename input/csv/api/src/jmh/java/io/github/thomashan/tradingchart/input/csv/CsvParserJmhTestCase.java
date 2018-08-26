package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import org.openjdk.jmh.annotations.*;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class CsvParserJmhTestCase<C extends CsvParser> {
    public abstract C getCsvParser();

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Stream<BidAskOhlc> testParse_InputStream(BenchmarkState benchmarkState) {
        return getCsvParser().parse(benchmarkState.inputStream);
    }

    @State(Scope.Thread)
    public static class BenchmarkState {
        public InputStream inputStream;

        @Setup
        public void setUp() {
//            this.inputStream = ExternalDrive.instance.getTestDataInputStream();
        }
    }
}
