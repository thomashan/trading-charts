package io.github.thomashan.tradingchart.persistence

trait ObjectReaderWriterTestCase<E, BO extends BytesOut, OW extends ObjectWriter<E, BO>, BI extends BytesIn, OR extends ObjectReader<E, BI>> {
    private OW objectWriter
    private OR objectReader

    abstract void testWriteAndRead()
}
