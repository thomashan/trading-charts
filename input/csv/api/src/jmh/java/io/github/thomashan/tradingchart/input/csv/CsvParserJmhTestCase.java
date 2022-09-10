package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.util.function.Consumers;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class CsvParserJmhTestCase<O extends Ohlc<O, ?>, C extends CsvParser<O>> {
    public abstract C getCsvParser();

    public void testParse_InputStream(BenchmarkState benchmarkState) {
        getCsvParser().parse(benchmarkState.inputStream, Consumers.nullConsumer());
    }

    @State(Scope.Thread)
    public static class BenchmarkState {
        public InputStream inputStream;

        @Setup
        public void setUp() throws Exception {
            String fileName = "EURUSD-S5.csv";
            ZipFile zipFile = new ZipFile(this.getClass().getResource(File.separator + fileName + ".zip").getFile());
            ZipEntry zipEntry = zipFile.getEntry(fileName);
            this.inputStream = zipFile.getInputStream(zipEntry);
        }
    }
}
