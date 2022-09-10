package io.github.thomashan.tradingchart.io;

import io.github.thomashan.tradingchart.util.function.Consumers;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class BufferedReaderJmhTest extends ReaderJmhTestCase<BufferedReader> {
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runRead_ByteAtATime(BenchmarkState benchmarkState) throws Exception {
        super.read_ByteAtATime(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runRead_IntoCharArray(BenchmarkStateWithParam benchmarkState) throws Exception {
        super.read_IntoCharArray(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runRead_IntoCharBuffer(BenchmarkStateWithParam benchmarkState) throws Exception {
        super.read_IntoCharBuffer(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runRead_IntoCharBufferAndFlip(BenchmarkStateWithParam benchmarkState) throws Exception {
        super.read_IntoCharBufferAndFlip(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runReadLine(BenchmarkState benchmarkState) throws Exception {
        while (benchmarkState.reader.readLine() != null) { /* */ }
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runLines(BenchmarkState benchmarkState) {
        benchmarkState.reader.lines()
                .forEach(Consumers.nullConsumer());
    }

    public static class BenchmarkState extends ReaderJmhTestCase.BenchmarkState<BufferedReader> {
        @Override
        protected BufferedReader createReader(Path path) throws Exception {
            return Files.newBufferedReader(path, StandardCharsets.UTF_8);
        }
    }

    public static class BenchmarkStateWithParam extends ReaderJmhTestCase.BenchmarkStateWithParam<BufferedReader> {
        @Override
        protected BufferedReader createReader(Path path) throws Exception {
            return Files.newBufferedReader(path, StandardCharsets.UTF_8);
        }
    }
}
