package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.drive.ExternalDrive;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public abstract class CsvParserJmhTestCase<C extends CsvParser> {
    public abstract C getCsvParser();

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public long testParse_InputStream(BenchmarkState benchmarkState) {
        return getCsvParser().parse(benchmarkState.inputStream).count();
    }

    @State(Scope.Thread)
    public static class BenchmarkState {
        public InputStream inputStream;

        @Setup
        public void setUp() {
            this.inputStream = ExternalDrive.instance.getTestDataInputStream();
        }
    }
}
