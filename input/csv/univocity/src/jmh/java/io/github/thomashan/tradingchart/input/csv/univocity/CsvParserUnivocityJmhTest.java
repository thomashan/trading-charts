package io.github.thomashan.tradingchart.input.csv.univocity;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.input.csv.CsvParserJmhTestCase;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

public class CsvParserUnivocityJmhTest extends CsvParserJmhTestCase<BidAskOhlc, CsvParserUnivocity<BidAskOhlc>> {
    @Override
    public CsvParserUnivocity<BidAskOhlc> getCsvParser() {
        return new CsvParserUnivocity<>(true);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runTestParse_InputStream(BenchmarkState benchmarkState) {
        super.testParse_InputStream(benchmarkState);
    }
}
