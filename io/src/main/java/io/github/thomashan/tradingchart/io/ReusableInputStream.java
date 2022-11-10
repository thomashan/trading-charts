package io.github.thomashan.tradingchart.io;

import java.io.IOException;
import java.io.InputStream;

public class ReusableInputStream extends InputStream {
    private final InputStream inputStream;

    public ReusableInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public void close() throws IOException {
        inputStream.reset();
    }
}
