package io.github.thomashan.tradingchart.io;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class FileInputStreamJmhTest extends InputStreamJmhTestCase<FileInputStream> {
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runRead_ByteAtATime(BenchmarkState benchmarkState) throws Exception {
        super.read_ByteAtATime(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runRead_IntoByteArray(BenchmarkStateWithParam benchmarkState) throws Exception {
        super.read_IntoByteArray(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runReadAllBytes(BenchmarkState benchmarkState) throws Exception {
        super.readAllBytes(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runReadNBytes(BenchmarkStateWithParam benchmarkState) throws Exception {
        super.readNBytes(benchmarkState);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void runReadNBytes_IntoByteArray(BenchmarkStateWithParam benchmarkState) throws Exception {
        super.readNBytes_IntoByteArray(benchmarkState);
    }

    public static class BenchmarkState extends InputStreamJmhTestCase.BenchmarkState<FileInputStream> {
        @Override
        protected FileInputStream createInputStream(Path path) throws Exception {
            return new FileInputStream(path.toFile());
        }
    }

    public static class BenchmarkStateWithParam extends InputStreamJmhTestCase.BenchmarkStateWithParam<FileInputStream> {
        @Override
        protected FileInputStream createInputStream(Path path) throws Exception {
            return new FileInputStream(path.toFile());
        }
    }
}
