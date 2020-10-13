package io.github.thomashan.tradingchart.ohlc;

import io.github.thomashan.tradingchart.ByteWatcherRegressionTestHelper;
import io.github.thomashan.tradingchart.price.BidAsk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

public class OhlcBwTest {
    private final long warmUpIterations = 1000;
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper;

    @BeforeEach
    void setUp() {
        byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper();
    }

    @Test
    void testNew() {
        Runnable runnable = () -> {
            Ohlc<BidAsk> ohlc = new Ohlc<>();        // 48 bytes
            ohlc.setDateTime(ZonedDateTime.now()); // 268 bytes
            ohlc.setOpen(BidAsk.of(1, 1.1));       // 104 bytes
            ohlc.setHigh(BidAsk.of(1, 1.1));       // 104 bytes
            ohlc.setLow(BidAsk.of(1, 1.1));        // 104 bytes
            ohlc.setClose(BidAsk.of(1, 1.1));      // 104 bytes
            ohlc.setVolume(1);
        };

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 728, 1);
    }
}
