package io.github.thomashan.tradingchart.persistence

import org.junit.jupiter.api.Test

trait ObjectReaderWriterTestCase<E, BO extends BytesOut, OW extends ObjectWriter<E, BO>, BI extends BytesIn, OR extends ObjectReader<E, BI>> {
    private OW objectWriter
    private OR objectReader

    @Test
    abstract void testWriteAndRead()
}
