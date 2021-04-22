package io.github.thomashan.tradingchart.input.csv

import io.github.thomashan.tradingchart.ByteWatcherTestCase
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc
import io.github.thomashan.tradingchart.drive.ExternalDrive
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class NewCsvParserBwTestCase<O extends Ohlc> extends ByteWatcherTestCase {
    final long warmUpIterations = 1
    final long iterations = 1
    protected NewCsvParser<O> csvParser

    @BeforeEach
    void setUp() {
        super.setUp()
        this.csvParser = createCsvParser()
    }

    abstract NewCsvParser<O> createCsvParser()

    @Test
    void testParseRowAtATime() {
        Runnable runnable = () -> {
            this.csvParser.parseRowAtATime(ExternalDrive.instance.testDataInputStream)
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 0, iterations)
    }
}
