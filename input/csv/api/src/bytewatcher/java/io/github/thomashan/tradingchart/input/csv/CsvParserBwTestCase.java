package io.github.thomashan.tradingchart.input.csv;

import io.github.thomashan.tradingchart.ByteWatcherRegressionTestHelper;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.util.function.Consumers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class CsvParserBwTestCase<O extends Ohlc<O, ?>> {
    private final long warmUpIterations = 0;
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
        ZipFile gbpAudZipFile = new ZipFile(this.getClass().getResource("/GBPAUD-M30.csv.zip").getFile());
        ZipEntry gbpAudZipEntry = gbpAudZipFile.getEntry("GBPAUD-M30.csv");
        csvParser.parse(gbpAudZipFile.getInputStream(gbpAudZipEntry), Consumers.nullConsumer());

        ZipFile eurUsdZipFile = new ZipFile(this.getClass().getResource("/EURUSD-S5.csv.zip").getFile());
        ZipEntry eurUsdZipEntry = eurUsdZipFile.getEntry("EURUSD-S5.csv");
        InputStream inputStream = eurUsdZipFile.getInputStream(eurUsdZipEntry);
//        Supplier<InputStream> inputStreamSupplier = () -> {
//            try {
//                return eurUsdZipFile.getInputStream(eurUsdZipEntry);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        };
        Runnable runnable = () -> csvParser.parse(inputStream, Consumers.nullConsumer());


        // 8,643,571,072
        // 8,475,570,856
        // 8,403,570,856
        // 8,547,570,856
        // 7,779,565,680
        // 2,338,800,704
        // 2,338,974,040
        // 10,048,787,520
        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, iterations * limitPerIteration(), iterations);
    }
}
