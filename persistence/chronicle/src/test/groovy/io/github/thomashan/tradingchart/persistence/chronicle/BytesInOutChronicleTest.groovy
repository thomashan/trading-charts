package io.github.thomashan.tradingchart.persistence.chronicle

import io.github.thomashan.tradingchart.persistence.BytesInOutTestCase
import net.openhft.chronicle.queue.ChronicleQueue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class BytesInOutChronicleTest implements BytesInOutTestCase<BytesOutChronicle, BytesInChronicle> {
    private File chronicleDir
    private ChronicleQueue chronicleQueue

    @BeforeEach
    void setUp() {
        this.chronicleDir = File.createTempDir(this.class.simpleName)
        print(chronicleDir.toString())
        this.chronicleQueue = ChronicleQueue.single(chronicleDir.toString())
        BytesInOutTestCase.super.setUp()
    }

    @AfterEach
    void tearDown() {
        chronicleDir.deleteDir()
    }

    @Override
    BytesOutChronicle createBytesOut() {
        return new BytesOutChronicle(chronicleQueue.acquireAppender())
    }

    @Override
    BytesInChronicle createBytesIn() {
        return new BytesInChronicle(chronicleQueue.createTailer())
    }
}
