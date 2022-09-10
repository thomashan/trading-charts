package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.ByteWatcherRegressionTestHelper;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.util.function.Consumers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.zip.ZipInputStream;

public abstract class CsvParserBwTestCase<O extends Ohlc<O, ?>> {
    private final long warmUpIterations = 1;
    private final long iterations = 1;
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper;
    private CsvParser<O> csvParser;

    @BeforeEach
    void setUp() {
        this.byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper();
        this.csvParser = createCsvParser();
    }

    protected abstract CsvParser<O> createCsvParser();

    /**
     * The limit should be 0 if no allocation were made
     *
     * @return
     */
    protected long limitPerIteration() {
        return 0;
    }

    @Test
    public void testParse_InputStream() throws Exception {
        // warm up the jvm
        ZipInputStream gbpAudZipInputStream = new ZipInputStream(this.getClass().getResourceAsStream(File.separator + "GBPAUD-M30.csv.zip"));
        gbpAudZipInputStream.getNextEntry();
        Runnable warmUp = () -> csvParser.parse(gbpAudZipInputStream, Consumers.nullConsumer());

        ZipInputStream eurUsdZipInputStream = new ZipInputStream(this.getClass().getResourceAsStream(File.separator + "EURUSD-S5.csv.zip"));
        eurUsdZipInputStream.getNextEntry();
        Runnable runnable = () -> csvParser.parse(eurUsdZipInputStream, Consumers.nullConsumer());

        byteWatcherRegressionTestHelper
                .warmUp(warmUp, warmUpIterations)
                .testAllocationNotExceeded(runnable, iterations * limitPerIteration(), iterations);
    }
}
