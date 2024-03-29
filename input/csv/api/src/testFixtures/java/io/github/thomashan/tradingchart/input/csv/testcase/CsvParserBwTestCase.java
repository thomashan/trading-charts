package io.github.thomashan.tradingchart.input.csv.testcase;

import io.github.thomashan.tradingchart.bytewatcher.ByteWatcherRegressionTestHelper;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.io.ReusableInputStream;
import io.github.thomashan.tradingchart.util.function.Consumers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.zip.ZipInputStream;

public abstract class CsvParserBwTestCase<O extends Ohlc<O, ?>> {
    private final long warmUpIterations = 10;
    private final long iterations = 1;
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper;
    private CsvParser<O> csvParser;
    private Path path;
    private ReusableInputStream reusableInputStream;

    @BeforeEach
    void setUp() throws Exception {
        this.byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper();
        this.csvParser = createCsvParser();

        ZipInputStream zipInputStream = new ZipInputStream(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("GBPAUD-M30.csv.zip")));
        zipInputStream.getNextEntry();
        this.path = Files.createTempFile(getClass().getSimpleName(), ".csv");
        Files.copy(zipInputStream, path, StandardCopyOption.REPLACE_EXISTING);
        this.reusableInputStream = new ReusableInputStream(new FileInputStream(path.toFile()));
    }

    @AfterEach
    void tearDown() throws Exception {
        reusableInputStream.close();
        Files.delete(path);
    }

    protected abstract CsvParser<O> createCsvParser();

    /**
     * The limit should be 0 if no allocation was made
     *
     * @return byte limit
     */
    protected long byteLimitPerIteration() {
        return 0;
    }

    @Test
    public void testParse_InputStream() {
        Runnable csvParserRunner = () -> csvParser.parse(reusableInputStream, Consumers.nullConsumer());
        byteWatcherRegressionTestHelper
                .warmUp(csvParserRunner, warmUpIterations)
                .testAllocationNotExceeded(csvParserRunner, iterations * byteLimitPerIteration(), iterations);
    }
}
