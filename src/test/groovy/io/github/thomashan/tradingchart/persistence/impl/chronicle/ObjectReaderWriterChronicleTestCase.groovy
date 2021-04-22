package io.github.thomashan.tradingchart.persistence.impl.chronicle

import io.github.thomashan.tradingchart.persistence.ObjectReaderWriterTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

trait ObjectReaderWriterChronicleTestCase<E> extends ObjectReaderWriterTestCase<E, BytesOutChronicle, ObjectWriterChronicle<E>, BytesInChronicle, ObjectReaderChronicle<E>> {
    private File chronicleDir

    abstract ObjectWriterChronicle<E> createObjectWriter(String chronicleDir)

    abstract ObjectReaderChronicle<E> createObjectReader(String chronicleDir)

    @BeforeEach
    void setUp() {
        this.chronicleDir = File.createTempDir(this.class.simpleName)
        this.io_github_thomashan_tradingchart_persistence_ObjectReaderWriterTestCase__objectWriter = createObjectWriter(chronicleDir.absolutePath)
        this.io_github_thomashan_tradingchart_persistence_ObjectReaderWriterTestCase__objectReader = createObjectReader(chronicleDir.absolutePath)
    }

    @AfterEach
    void tearDown() {
        super.tearDown()
        chronicleDir.deleteDir()
    }
}
