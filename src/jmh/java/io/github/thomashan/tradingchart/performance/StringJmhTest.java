package io.github.thomashan.tradingchart.performance;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StringJmhTest {
    private static final String EMPTY = "";

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String testToString_StringAppendDouble(BenchmarkState benchmarkState) {
        return EMPTY + benchmarkState.random.nextDouble();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public String testToString_StringValueOf(BenchmarkState benchmarkState) {
        return String.valueOf(benchmarkState.random.nextDouble());
    }

    @State(Scope.Thread)
    public static class BenchmarkState {
        Random random = new Random();
    }
}
