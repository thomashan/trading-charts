package io.github.thomashan.tradingchart;

import org.junit.jupiter.api.Test;

public class JavaObjectSizeBwTest extends ByteWatcherTestCase {
    @Override
    protected long getWarmUpIterations() {
        return 1000;
    }

    @Override
    protected long getIterations() {
        return 1000;
    }

    @Test
    void testRunNothing_Lambda_ManyTimes() {
        Runnable runnable = () -> { /* */ };

        getByteWatcherRegressionTestHelper()
                .warmUp(runnable, getWarmUpIterations())
                .testAllocationNotExceeded(runnable, 0, getIterations());
    }

    @Test
    void testRunNothing_Lambda_Once() {
        Runnable runnable = () -> { /* */ };

        getByteWatcherRegressionTestHelper()
                .testAllocationNotExceeded(runnable, 0, 1);
    }

    @Test
    void testRunNothing_AnonClass_ManyTimes() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };

        getByteWatcherRegressionTestHelper()
                .warmUp(runnable, getWarmUpIterations())
                .testAllocationNotExceeded(runnable, 0, getIterations());
    }

    @Test
    void testRunNothing_AnonClass_Once() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };

        getByteWatcherRegressionTestHelper()
                .testAllocationNotExceeded(runnable, 0, 1);
    }

    @Test
    void testCreateObject_Lambda_ManyTimes() {
        Runnable runnable = () -> new Object();

        getByteWatcherRegressionTestHelper()
                .warmUp(runnable, getWarmUpIterations())
                .testAllocationNotExceeded(runnable, 16 * getIterations(), getIterations());
    }

    @Test
    void testCreateObject_Lambda_Once() {
        Runnable runnable = () -> new Object();

        getByteWatcherRegressionTestHelper()
                .testAllocationNotExceeded(runnable, 16, 1);
    }

    @Test
    void testCreateObject_AnonClass_ManyTimes() {
        Runnable runnable = () -> new Object();

        getByteWatcherRegressionTestHelper()
                .warmUp(runnable, getWarmUpIterations())
                .testAllocationNotExceeded(runnable, 16 * getIterations(), getIterations());
    }

    @Test
    void testCreateObject_AnonClass_Once() {
        Runnable runnable = () -> new Object();

        getByteWatcherRegressionTestHelper()
                .testAllocationNotExceeded(runnable, 16, 1);
    }

    @Test
    void testCreateObject_MethodReference_ManyTimes() {
        Runnable runnable = Object::new;

        getByteWatcherRegressionTestHelper()
                .warmUp(runnable, getWarmUpIterations())
                .testAllocationNotExceeded(runnable, 16 * getIterations(), getIterations());
    }

    @Test
    void testCreateObject_MethodReference_Once() {
        Runnable runnable = Object::new;

        getByteWatcherRegressionTestHelper()
                .testAllocationNotExceeded(runnable, 16, 1);
    }
}
