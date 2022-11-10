package io.github.thomashan.tradingchart.lang;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DoubleJmhTest extends DoubleParserJmhTestCase<Double> {
    @Override
    protected Consumer<String> getParseAction(BenchmarkState<Double> benchmarkState) {
        return Double::parseDouble;
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runParseDouble_SmallDoubles(DoubleParserJmhTestCase.BenchmarkState<Double> benchmarkState) {
        super.parseDouble_SmallDoubles(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runParseDouble_LargeDoubles(DoubleParserJmhTestCase.BenchmarkState<Double> benchmarkState) {
        super.parseDouble_LargeDoubles(benchmarkState);
    }
}
