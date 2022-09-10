package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.util.function.Consumers;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipInputStream;

public abstract class CsvParserJmhTestCase<O extends Ohlc<O, ?>, C extends CsvParser<O>> {
    public abstract C getCsvParser();

    public void testParse_InputStream(BenchmarkState benchmarkState) {
        getCsvParser().parse(benchmarkState.inputStream, Consumers.nullConsumer());
    }

    @State(Scope.Thread)
    public static class BenchmarkState {
        public Path path;
        public InputStream inputStream;

        @Setup
        public void setUp() throws Exception {
            String fileName = "EURUSD-S5.csv";
            ZipInputStream zipInputStream = new ZipInputStream(this.getClass().getResourceAsStream(File.separator + fileName + ".zip"));
            zipInputStream.getNextEntry();
            this.path = Files.createTempFile(getClass().getName(), ".csv");
            Files.copy(zipInputStream, path, StandardCopyOption.REPLACE_EXISTING);
            this.inputStream = Files.newInputStream(path);
        }

        @TearDown
        public void tearDown() throws Exception {
            inputStream.close();
            Files.delete(path);
        }
    }
}
