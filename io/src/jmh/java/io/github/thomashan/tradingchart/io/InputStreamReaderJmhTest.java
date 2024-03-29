package io.github.thomashan.tradingchart.io;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class InputStreamReaderJmhTest extends ReaderJmhTestCase<InputStreamReader> {
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

    public static class BenchmarkState extends ReaderJmhTestCase.BenchmarkState<InputStreamReader> {
        @Override
        protected InputStreamReader createReader(Path path) throws Exception {
            return new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8);
        }
    }

    public static class BenchmarkStateWithParam extends ReaderJmhTestCase.BenchmarkStateWithParam<InputStreamReader> {
        @Override
        protected InputStreamReader createReader(Path path) throws Exception {
            return new InputStreamReader(Files.newInputStream(path), StandardCharsets.UTF_8);
        }
    }
}
