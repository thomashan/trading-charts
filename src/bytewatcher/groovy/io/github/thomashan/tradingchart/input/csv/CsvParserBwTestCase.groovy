package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.ByteWatcherTestCase
import io.github.thomashan.tradingchart.consumer.Consumers
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc
import io.github.thomashan.tradingchart.drive.ExternalDrive
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class CsvParserBwTestCase<O extends Ohlc> extends ByteWatcherTestCase {
    final long warmUpIterations = 1
    final long iterations = 1
    protected CsvParser<O> csvParser

    @BeforeEach
    void setUp() {
        super.setUp()
        this.csvParser = createCsvParser()
    }

    abstract CsvParser<O> createCsvParser()

    @Test
    void testParse() {
        Runnable runnable = () -> {
            this.csvParser.parse(ExternalDrive.instance.testDataInputStream).forEach(Consumers.noOps())
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 0, iterations)
    }
}
