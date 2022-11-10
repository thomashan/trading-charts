package io.github.thomashan.tradingchart.io;

import java.io.IOException;
import java.io.Reader;

public class ReusableReader extends Reader {
    private final Reader reader;

    public ReusableReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return reader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        reader.reset();
    }
}
