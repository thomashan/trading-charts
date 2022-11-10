package io.github.thomashan.tradingchart.io;

import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipInputStream;

public abstract class ReaderJmhTestCase<R extends Reader> {
    public void read_ByteAtATime(BenchmarkState<R> benchmarkState) throws Exception {
        while (benchmarkState.reader.read() != InputStreamConstants.EOF) { /* */ }
    }

    public void read_IntoCharArray(BenchmarkStateWithParam<R> benchmarkState) throws Exception {
        char[] read = new char[benchmarkState.chars];
        while (benchmarkState.reader.read(read) != InputStreamConstants.EOF) { /* */ }
    }

    public void read_IntoCharBuffer(BenchmarkStateWithParam<R> benchmarkState) throws Exception {
        CharBuffer charBuffer = CharBuffer.allocate(benchmarkState.chars);
        while (benchmarkState.reader.read(charBuffer) > 0) { /* */ }
    }

    public void read_IntoCharBufferAndFlip(BenchmarkStateWithParam<R> benchmarkState) throws Exception {
        CharBuffer charBuffer = CharBuffer.allocate(benchmarkState.chars);
        while (benchmarkState.reader.read(charBuffer) > 0) {
            charBuffer.flip();
        }
    }

    @State(Scope.Thread)
    public static class BenchmarkState<R extends Reader> {
        public Path path;
        public R reader;

        @Setup
        public void setUp() throws Exception {
            String fileName = "EURUSD-S5.csv";
            ZipInputStream zipInputStream = new ZipInputStream(this.getClass().getResourceAsStream(File.separator + fileName + ".zip"));
            zipInputStream.getNextEntry();
            this.path = Files.createTempFile(getClass().getName(), ".csv");
            Files.copy(zipInputStream, path, StandardCopyOption.REPLACE_EXISTING);
            this.reader = createReader(path);
        }

        @TearDown
        public void tearDown() throws Exception {
            reader.close();
            Files.delete(path);
        }

        protected R createReader(Path path) throws Exception {
            return null;
        }
    }

    public static class BenchmarkStateWithParam<R extends Reader> extends BenchmarkState<R> {
        @Param({"16384", "8192", "4096", "2048", "1024", "512", "256", "128", "64", "32", "16", "8", "4", "2", "1"})
        public int chars;
    }
}
