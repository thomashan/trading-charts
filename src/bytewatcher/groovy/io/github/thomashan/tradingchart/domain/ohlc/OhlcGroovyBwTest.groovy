package io.github.thomashan.tradingchart.domain.ohlc

import io.github.thomashan.tradingchart.ByteWatcherRegressionTestHelper
import io.github.thomashan.tradingchart.domain.price.BidAsk
import io.github.thomashan.tradingchart.domain.price.Mid
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.ZonedDateTime

class OhlcGroovyBwTest {
    private final long warmUpIterations = 1000
    private ByteWatcherRegressionTestHelper byteWatcherRegressionTestHelper

    @BeforeEach
    void setUp() {
        byteWatcherRegressionTestHelper = new ByteWatcherRegressionTestHelper()
    }

    @Test
    void testNew_BidAskOhlc() {
        Runnable runnable = () -> {
            new BidAskOhlc()    // 64 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 64, 1)
    }

    @Test
    void testNew_BidAskOhlc_Date() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = new BidAskOhlc()    // 64 bytes
            ohlc.setDateTime(ZonedDateTime.now()) // 264 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 328, 1)
    }

    @Test
    void testNew_BidAskOhlc_Ohlc() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = new BidAskOhlc()    // 64 bytes
            ohlc.setOpen(BidAsk.of(1, 1.1))       // 176 bytes
            ohlc.setHigh(BidAsk.of(1, 1.1))       // 176 bytes
            ohlc.setLow(BidAsk.of(1, 1.1))        // 176 bytes
            ohlc.setClose(BidAsk.of(1, 1.1))      // 176 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 768, 1)
    }

    @Test
    void testNew_BidAskOhlc_Full() {
        Runnable runnable = () -> {
            BidAskOhlc ohlc = new BidAskOhlc()    // 64 bytes
            ohlc.setDateTime(ZonedDateTime.now()) // 264 bytes
            ohlc.setOpen(BidAsk.of(1, 1.1))       // 176 bytes
            ohlc.setHigh(BidAsk.of(1, 1.1))       // 176 bytes
            ohlc.setLow(BidAsk.of(1, 1.1))        // 176 bytes
            ohlc.setClose(BidAsk.of(1, 1.1))      // 176 bytes
            ohlc.setVolume(1)                     // 48 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 1080, 1)
    }

    @Test
    void testNew_MidOhlc() {
        Runnable runnable = () -> {
            new MidOhlc()                         // 64 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 64, 1)
    }

    @Test
    void testNew_MidOhlc_Date() {
        Runnable runnable = () -> {
            MidOhlc ohlc = new MidOhlc()          // 64 bytes
            ohlc.setDateTime(ZonedDateTime.now()) // 264 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 328, 1)
    }

    @Test
    void testNew_MidOhlc_Ohlc() {
        Runnable runnable = () -> {
            MidOhlc ohlc = new MidOhlc()          // 64 bytes
            ohlc.setOpen(Mid.of(1))               // 120 bytes
            ohlc.setHigh(Mid.of(1.1))             // 120 bytes
            ohlc.setLow(Mid.of(1.1))              // 120 bytes
            ohlc.setClose(Mid.of(1))              // 120 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 544, 1)
    }

    @Test
    void testNew_MidOhlc_Full() {
        Runnable runnable = () -> {
            MidOhlc ohlc = new MidOhlc()          // 64 bytes
            ohlc.setDateTime(ZonedDateTime.now()) // 264 bytes
            ohlc.setOpen(Mid.of(1))               // 120 bytes
            ohlc.setHigh(Mid.of(1))               // 120 bytes
            ohlc.setLow(Mid.of(1))                // 120 bytes
            ohlc.setClose(Mid.of(1))              // 120 bytes
            ohlc.setVolume(1)                     // 48 bytes
        }

        byteWatcherRegressionTestHelper
                .warmUp(runnable, warmUpIterations)
                .testAllocationNotExceeded(runnable, 856, 1)
    }
}
