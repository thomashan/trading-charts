package io.github.thomashan.tradingchart.io

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.nio.BufferOverflowException
import java.nio.CharBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.zip.ZipInputStream

import static org.junit.jupiter.api.Assertions.assertThrows

class LineReaderTest {
    private Path path
    private LineReader lineReader

    @BeforeEach
    void setUp() {
        String fileName = "EURUSD-S5.csv"
        ZipInputStream zipInputStream = new ZipInputStream(this.getClass().getResourceAsStream(File.separator + fileName + ".zip"))
        zipInputStream.nextEntry
        this.path = Files.createTempFile(this.class.name, ".csv")
        Files.copy(zipInputStream, path, StandardCopyOption.REPLACE_EXISTING)
        this.lineReader = new LineReader(Files.newInputStream(path))
    }

    @AfterEach
    void tearDown() {
        lineReader.close()
        Files.delete(path)
    }

    @Test
    void testReadLineCharBuffer() {
        CharBuffer charBuffer = CharBuffer.allocate(512)
        int i = 0;
        while (lineReader.readLine(charBuffer) > 0) { /* */
            i++
        }
        assert 3_000_001 == i
    }

    @Test
    void testReadLineCharBuffer_NullCharBuffer() {
        assertThrows(NullPointerException, () -> lineReader.readLine(null))
    }

    @Test
    void testReadLineCharBuffer_CharBufferTooSmallThrowsException() {
        assertThrows(BufferOverflowException, () -> lineReader.readLine(CharBuffer.allocate(1)))
    }
}
