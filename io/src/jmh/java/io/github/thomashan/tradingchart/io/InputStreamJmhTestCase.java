package io.github.thomashan.tradingchart.io;

import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipInputStream;

import static io.github.thomashan.tradingchart.io.InputStreamConstants.EOF;

public abstract class InputStreamJmhTestCase<IS extends InputStream> {
    public void read_ByteAtATime(BenchmarkState<IS> benchmarkState) throws Exception {
        while (benchmarkState.inputStream.read() != EOF) { /* */ }
    }

    public void read_IntoByteArray(BenchmarkStateWithParam<IS> benchmarkState) throws Exception {
        byte[] read = new byte[benchmarkState.bytes];
        while (benchmarkState.inputStream.read(read) != EOF) { /* */ }
    }

    public void readAllBytes(BenchmarkState<IS> benchmarkState) throws Exception {
        benchmarkState.inputStream.readAllBytes();
    }

    public void readNBytes(BenchmarkStateWithParam<IS> benchmarkState) throws Exception {
        while (benchmarkState.inputStream.readNBytes(benchmarkState.bytes).length != 0) { /* */ }
    }

    public void readNBytes_IntoByteArray(BenchmarkStateWithParam<IS> benchmarkState) throws Exception {
        byte[] read = new byte[benchmarkState.bytes];
        while (benchmarkState.inputStream.readNBytes(read, 0, read.length) != 0) { /* */ }
    }

    @State(Scope.Thread)
    public static class BenchmarkState<IS extends InputStream> {
        public Path path;
        public IS inputStream;

        @Setup
        public void setUp() throws Exception {
            String fileName = "EURUSD-S5.csv";
            ZipInputStream zipInputStream = new ZipInputStream(this.getClass().getResourceAsStream(File.separator + fileName + ".zip"));
            zipInputStream.getNextEntry();
            this.path = Files.createTempFile(getClass().getName(), ".csv");
            Files.copy(zipInputStream, path, StandardCopyOption.REPLACE_EXISTING);
            this.inputStream = createInputStream(path);
        }

        @TearDown
        public void tearDown() throws Exception {
            inputStream.close();
            Files.delete(path);
        }

        protected IS createInputStream(Path path) throws Exception {
            return null;
        }
    }

    public static class BenchmarkStateWithParam<IS extends InputStream> extends BenchmarkState<IS> {
        @Param({"16384", "8192", "4096", "2048", "1024", "512", "256", "128", "64", "32", "16", "8", "4", "2", "1"})
        public int bytes;
    }
}
