package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.BidAskOhlc;
import io.github.thomashan.tradingchart.drive.ExternalDrive;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

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
        private File file;

        @Setup
        public void setUp() {
            try {
                this.file = File.createTempFile("csv_parser_jmh", "");
                FileOutputStream outputStream = new FileOutputStream(file);
                ExternalDrive.instance.download("/test_data/EURUSD-S5-0.0.1.csv.gz", outputStream);
                this.inputStream = new GZIPInputStream(new FileInputStream(file));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                if (file != null) {
                    file.deleteOnExit();
                }
            }
        }
    }
}
