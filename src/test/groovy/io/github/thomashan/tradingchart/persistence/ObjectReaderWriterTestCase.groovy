package io.github.thomashan.tradingchart.persistence

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

trait ObjectReaderWriterTestCase<E, BO extends BytesOut, OW extends ObjectWriter<E, BO>, BI extends BytesIn, OR extends ObjectReader<E, BI>> {
    private OW objectWriter
    private OR objectReader

    @AfterEach
    void tearDown() {
        objectWriter.close()
        objectReader.close()
    }

    @Test
    void testWriteAndRead() {
        List<E> objects = createObjects()

        objects.forEach(objectWriter::write)

        for (int i = 0; i < objects.size(); i++) {
            assert objectReader.read() == objects[i]
        }
    }

    abstract List<E> createObjects()
}
