package io.github.thomashan.tradingchart.lang;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DoubleParserGeoMapJmhTest extends DoubleParserJmhTestCase<DoubleParserGeoMap> {
    @Override
    protected Consumer<String> getParseAction(DoubleParserJmhTestCase.BenchmarkState<DoubleParserGeoMap> benchmarkState) {
        return doubleString -> benchmarkState.doubleParser.parse(doubleString);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runParseApprox_SmallDoubles(BenchmarkState benchmarkState) {
        super.parseDouble_SmallDoubles(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runParseApprox_LargeDoubles(BenchmarkState benchmarkState) {
        super.parseDouble_LargeDoubles(benchmarkState);
    }

    @State(Scope.Thread)
    public static class BenchmarkState extends DoubleParserJmhTestCase.BenchmarkState<DoubleParserGeoMap> {
        @Override
        protected DoubleParserGeoMap createParser() {
            return new DoubleParserGeoMap();
        }
    }
}
