package io.github.thomashan.tradingchart.domain.ohlc;

import io.github.thomashan.tradingchart.ByteWatcherRegressionTestHelper;
import io.github.thomashan.tradingchart.domain.price.BidAsk;
import io.github.thomashan.tradingchart.domain.price.Mid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

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
            BidAskOhlc ohlc = BidAskOhlc.emptyMinimal();    // 48 bytes
            ohlc.setDateTime(Instant.now());         // 24 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 72, 1);
    }

    @Test
    void testNew_BidAskOhlc_Ohlc() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = BidAskOhlc.emptyMinimal();    // 48 bytes
            ohlc.setOpen(BidAsk.of(1, 1.1));         // 104 bytes
            ohlc.setHigh(BidAsk.of(1, 1.1));         // 104 bytes
            ohlc.setLow(BidAsk.of(1, 1.1));          // 104 bytes
            ohlc.setClose(BidAsk.of(1, 1.1));        // 104 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 464, 1);
    }

    @Test
    void testNew_BidAskOhlc_Full() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = BidAskOhlc.emptyMinimal();     // 48 bytes
            ohlc.setDateTime(Instant.now());          // 24 bytes
            ohlc.setOpen(BidAsk.of(1, 1.1));          // 104 bytes
            ohlc.setHigh(BidAsk.of(1, 1.1));          // 104 bytes
            ohlc.setLow(BidAsk.of(1, 1.1));           // 104 bytes
            ohlc.setClose(BidAsk.of(1, 1.1));         // 104 bytes
            ohlc.setVolume(1);
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 488, 1);
    }

    @Test
    void testNew_MidOhlc() {
        Runnable runnable = () -> {
            new MidOhlc();                         // 48 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 48, 1);
    }

    @Test
    void testNew_MidOhlc_Date() {
        Runnable runnable = () -> {
            MidOhlc ohlc = MidOhlc.emptyMinimal();          // 48 bytes
            ohlc.setDateTime(Instant.now());         // 24 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 72, 1);
    }

    @Test
    void testNew_MidOhlc_Ohlc() {
        Runnable runnable = () -> {
            MidOhlc ohlc = MidOhlc.emptyMinimal();        // 48 bytes
            ohlc.setOpen(Mid.of(1));               // 72 bytes
            ohlc.setHigh(Mid.of(1.1));             // 72 bytes
            ohlc.setLow(Mid.of(1.1));              // 72 bytes
            ohlc.setClose(Mid.of(1));              // 72 bytes
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 336, 1);
    }

    @Test
    void testNew_MidOhlc_Full() {
        Runnable runnable = () -> {
            MidOhlc ohlc = MidOhlc.emptyMinimal();          // 48 bytes
            ohlc.setDateTime(Instant.now());         // 24 bytes
            ohlc.setOpen(Mid.of(1));                 // 72 bytes
            ohlc.setHigh(Mid.of(1));                 // 72 bytes
            ohlc.setLow(Mid.of(1));                  // 72 bytes
            ohlc.setClose(Mid.of(1));                // 72 bytes
            ohlc.setVolume(1);
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 360, 1);
    }
}
