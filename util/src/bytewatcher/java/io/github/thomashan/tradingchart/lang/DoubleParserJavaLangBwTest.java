package io.github.thomashan.tradingchart.lang;

import io.github.thomashan.tradingchart.bytewatcher.ByteWatcherRegressionTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class DoubleParserJavaLangBwTest {
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper;
    private Random random;

    @BeforeEach
    void setUp() {
        this.byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper();
        this.random = new Random();
    }

    @Test
    public void testParse_InputStream() {
        List<String> randomDoubles = random.doubles(1_000, 0, Double.MAX_VALUE)
                .mapToObj(doubleValue -> Double.toString(doubleValue))
                .toList();
        Runnable runnable = () -> randomDoubles.forEach(doubleString -> DoubleParserJavaLang.parseApprox(doubleString));

        byteWatcherRegressionTestHelper
                .warmUp(runnable, 200)
                .testAllocationNotExceeded(runnable, 0, 1);
    }
}
