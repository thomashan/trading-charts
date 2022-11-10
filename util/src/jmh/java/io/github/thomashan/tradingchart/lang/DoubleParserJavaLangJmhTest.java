package io.github.thomashan.tradingchart.lang;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DoubleParserJavaLangJmhTest extends DoubleParserJmhTestCase<DoubleParserJavaLang> {
    @Override
    protected Consumer<String> getParseAction(BenchmarkState<DoubleParserJavaLang> benchmarkState) {
        return DoubleParserJavaLang::parseApprox;
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runParseDouble_SmallDoubles(BenchmarkState<DoubleParserJavaLang> benchmarkState) {
        super.parseDouble_SmallDoubles(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runParseDouble_LargeDoubles(BenchmarkState<DoubleParserJavaLang> benchmarkState) {
        super.parseDouble_LargeDoubles(benchmarkState);
    }
}
