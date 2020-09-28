package io.github.thomashan.tradingchart

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ObjectSizeTest {
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper

    @BeforeEach
    void setUp() {
        byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper()
    }

    @Test
    void testRunNothing() {
        ByteWatcher bw = new ByteWatcher()
        Runnable runnable1 = { /* */ }

        println(Thread.currentThread().name)

        bw.printAllAllocations()

        byteWatcherRegressionTestHelper
                .warmUp(runnable1, 10000000)
                .testAllocationNotExceeded(runnable1, 0, 10000000)
    }

    @Test
    void testCreateObject() {
        ByteWatcher bw = new ByteWatcher()
        Runnable runnable1 = { new Object() }

        println(Thread.currentThread().name)

        bw.printAllAllocations()

        byteWatcherRegressionTestHelper
                .warmUp(runnable1, 10000000)
                .testAllocationNotExceeded(runnable1, 0, 10000000)
    }
}
