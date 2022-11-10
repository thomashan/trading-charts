package io.github.thomashan.tradingchart.lang;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public abstract class DoubleParserJmhTestCase<T> {
    protected abstract Consumer<String> getParseAction(BenchmarkState<T> benchmarkState);

    public void parseDouble_SmallDoubles(DoubleParserJmhTestCase.BenchmarkState<T> benchmarkState) {
        benchmarkState.smallDoubleStrings.forEach(getParseAction(benchmarkState));
    }

    public void parseDouble_LargeDoubles(DoubleParserJmhTestCase.BenchmarkState<T> benchmarkState) {
        benchmarkState.largeDoubleStrings.forEach(getParseAction(benchmarkState));
    }

    @State(Scope.Thread)
    public static class BenchmarkState<T> {
        private Random random;
        public List<String> smallDoubleStrings;
        public List<String> largeDoubleStrings;
        public T doubleParser;

        @Setup
        public void setUp() {
            this.random = new Random();
            this.smallDoubleStrings = random.doubles(1_000_000, 0, 1)
                    .mapToObj(Double::toString)
                    .toList();
            this.largeDoubleStrings = random.doubles(1_000_000, 0, Double.MAX_VALUE)
                    .mapToObj(Double::toString)
                    .toList();
            this.doubleParser = createParser();
        }

        protected T createParser() {
            return null;
        }
    }
}
