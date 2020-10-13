package io.github.thomashan.tradingchart

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Groovy adds a lot of overhead in loading object into memory for the first time.
 * Depending on the following:
 * * sequence of tests executed
 * * jvm used (as JIT is controlled by the jvm)
 * the results may be vastly different especially if there's not warm up.
 */
class ObjectSizeGroovyTest {
    private final long warmUpIterations = 1000
    private final long iterations = 1000
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper


    @BeforeEach
    void setUp() {
        byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper()
    }

    @Test
    void testRunNothing_Lambda_ManyTimes() {
        Runnable runnable = () -> { /* */ }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 16 * iterations, iterations)
    }

    @Test
    void testRunNothing_Lambda_Once() {
        Runnable runnable = () -> { /* */ }

        byteWatcherRegressionTestHelper
                .testAllocationNotExceeded(runnable, 136, 1)
    }

    @Test
    void testRunNothing_Closure_ManyTimes() {
        Runnable runnable = { /* */ }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 16 * iterations, iterations)
    }

    @Test
    void testRunNothing_Closure_Once() {
        Runnable runnable = { /* */ }

        byteWatcherRegressionTestHelper
                .testAllocationNotExceeded(runnable, 136, 1)
    }

    @Test
    void testRunNothing_AnonClass_ManyTimes() {
        Runnable runnable = new Runnable() {
            @Override
            void run() {

            }
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 0, iterations)
    }

    @Test
    void testRunNothing_AnonClass_Once() {
        Runnable runnable = new Runnable() {
            @Override
            void run() {

            }
        }

        byteWatcherRegressionTestHelper
                .testAllocationNotExceeded(runnable, 0, 1)
    }

    @Test
    void testCreateObject_Closure_ManyTimes() {
        Runnable runnable = { new Object() }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 32 * iterations, iterations)
    }

    @Test
    void testCreateObject_Closure_Once() {
        Runnable runnable = { new Object() }

        byteWatcherRegressionTestHelper
                .testAllocationNotExceeded(runnable, 208, 1)
    }

    @Test
    void testCreateObject_Lambda_ManyTimes() {
        Runnable runnable = () -> { new Object() }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 32 * iterations, iterations)
    }

    @Test
    void testCreateObject_Lambda_Once() {
        Runnable runnable = () -> { new Object() }

        byteWatcherRegressionTestHelper
                .testAllocationNotExceeded(runnable, 208, 1)
    }

    @Test
    void testCreateObject_AnonClass_ManyTimes() {
        Runnable runnable = new Runnable() {
            @Override
            void run() {
                new Object()
            }
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 16 * iterations, iterations)
    }

    @Test
    void testCreateObject_AnonClass_Once() {
        Runnable runnable = new Runnable() {
            @Override
            void run() {
                new Object()
            }
        }

        byteWatcherRegressionTestHelper
                .testAllocationNotExceeded(runnable, 72, 1)
    }

    @Test
    void testCreateObject_MethodReference_ManyTimes() {
        Runnable runnable = Object::new

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 200 * iterations, iterations)
    }

    @Test
    void testCreateObject_MethodReference_Once() {
        Runnable runnable = Object::new

        byteWatcherRegressionTestHelper
                .testAllocationNotExceeded(runnable, 1760, 1)
    }
}
