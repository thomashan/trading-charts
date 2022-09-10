package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

public class CsvParserStreamIndexOfJmhTest extends CsvParserJmhTestCase<BidAskOhlc, CsvParserStreamIndexOf<BidAskOhlc>> {
    @Override
    public CsvParserStreamIndexOf<BidAskOhlc> getCsvParser() {
        return new CsvParserStreamIndexOf<>();
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runTestParse_InputStream(BenchmarkState benchmarkState) {
        super.testParse_InputStream(benchmarkState);
    }
}
