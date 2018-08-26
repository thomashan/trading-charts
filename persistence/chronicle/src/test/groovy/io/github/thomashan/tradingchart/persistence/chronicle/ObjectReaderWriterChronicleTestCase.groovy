package io.github.thomashan.tradingchart.persistence.chronicle

import io.github.thomashan.tradingchart.persistence.ObjectReaderWriterTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

trait ObjectReaderWriterChronicleTestCase<E> extends ObjectReaderWriterTestCase<E, BytesOutChronicle, ObjectWriterChronicle<E>, BytesInChronicle, ObjectReaderChronicle<E>> {
    private File chronicleDir
    ObjectWriterChronicle<E> objectWriter
    ObjectReaderChronicle<E> objectReader

    abstract ObjectWriterChronicle<E> createObjectWriter(String chronicleDir)

    abstract ObjectReaderChronicle<E> createObjectReader(String chronicleDir)

    @BeforeEach
    void setUp() {
        this.chronicleDir = File.createTempDir(this.class.simpleName)
        this.objectWriter = createObjectWriter(chronicleDir.absolutePath)
        this.objectReader = createObjectReader(chronicleDir.absolutePath)
    }

    @AfterEach
    void tearDown() {
        objectWriter.close()
        objectReader.close()
        chronicleDir.deleteDir()
    }
}
