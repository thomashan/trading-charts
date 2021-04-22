package io.github.thomashan.tradingchart;

import org.junit.jupiter.api.BeforeEach;

public abstract class ByteWatcherTestCase {
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper;

    @BeforeEach
    protected void setUp() {
        byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper();
    }

    protected ByteWatcherRegressionTestHelper getByteWatcherRegressionTestHelper() {
        return byteWatcherRegressionTestHelper;
    }

    protected abstract long getWarmUpIterations();

    protected abstract long getIterations();
}
