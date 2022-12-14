package io.github.thomashan.tradingchart.domain.ohlc;

import io.github.thomashan.tradingchart.ByteWatcherRegressionTestHelper;
import io.github.thomashan.tradingchart.domain.price.BidAsk;
import io.github.thomashan.tradingchart.domain.price.Mid;
import io.github.thomashan.tradingchart.time.MutableInstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OhlcJavaBwTest {
    private final long warmUpIterations = 1000;
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper;

    @BeforeEach
    void setUp() {
        byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper();
    }

    @Test
    void testNew_BidAskOhlc() {
        Runnable runnable = () -> {
            BidAskOhlc.emptyMinimal();    // 48 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 48, 1);
    }

    @Test
    void testNew_BidAskOhlc_Date() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = BidAskOhlc.emptyMinimal(); // 48 bytes
            ohlc.dateTime = MutableInstant.EPOCH;        // 24 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 72, 1);
    }

    @Test
    void testNew_BidAskOhlc_Ohlc() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = BidAskOhlc.emptyMinimal(); // 48 bytes
            ohlc.open = BidAsk.of(1, 1.1);               // 104 bytes
            ohlc.high = BidAsk.of(1, 1.1);               // 104 bytes
            ohlc.low = BidAsk.of(1, 1.1);                // 104 bytes
            ohlc.close = BidAsk.of(1, 1.1);              // 104 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 464, 1);
    }

    @Test
    void testNew_BidAskOhlc_Full() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = BidAskOhlc.emptyMinimal(); // 48 bytes
            ohlc.dateTime = MutableInstant.EPOCH;
            ;       // 24 bytes
            ohlc.open = BidAsk.of(1, 1.1);               // 104 bytes
            ohlc.high = BidAsk.of(1, 1.1);               // 104 bytes
            ohlc.low = BidAsk.of(1, 1.1);                // 104 bytes
            ohlc.close = BidAsk.of(1, 1.1);              // 104 bytes
            ohlc.volume = 1;
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 488, 1);
    }

    @Test
    void testNew_MidOhlc() {
        Runnable runnable = () -> {
            new MidOhlc(); // 48 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 48, 1);
    }

    @Test
    void testNew_MidOhlc_Date() {
        Runnable runnable = () -> {
            MidOhlc ohlc = MidOhlc.emptyMinimal(); // 48 bytes
            ohlc.dateTime = MutableInstant.EPOCH;  // 24 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 72, 1);
    }

    @Test
    void testNew_MidOhlc_Ohlc() {
        Runnable runnable = () -> {
            MidOhlc ohlc = MidOhlc.emptyMinimal(); // 48 bytes
            ohlc.open = Mid.of(1);                 // 72 bytes
            ohlc.high = Mid.of(1.1);               // 72 bytes
            ohlc.low = Mid.of(1.1);                // 72 bytes
            ohlc.close = Mid.of(1);                // 72 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 336, 1);
    }

    @Test
    void testNew_MidOhlc_Full() {
        Runnable runnable = () -> {
            MidOhlc ohlc = MidOhlc.emptyMinimal(); // 48 bytes
            ohlc.dateTime = MutableInstant.EPOCH;  // 24 bytes
            ohlc.open = Mid.of(1);                 // 72 bytes
            ohlc.high = Mid.of(1);                 // 72 bytes
            ohlc.low = Mid.of(1);                  // 72 bytes
            ohlc.close = Mid.of(1);                // 72 bytes
            ohlc.volume = 1;
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 360, 1);
    }
}
