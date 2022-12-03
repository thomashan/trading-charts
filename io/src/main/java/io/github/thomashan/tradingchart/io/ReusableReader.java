package io.github.thomashan.tradingchart.io;

import java.io.*;

public class ReusableReader extends Reader {
    private InputStream inputStream;
    private final InputStreamReader inputStreamReader;

    public ReusableReader(InputStream inputStream) {
        super(inputStream);
        this.inputStream = inputStream;
        this.inputStreamReader = new InputStreamReader(inputStream);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return inputStreamReader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        if (inputStream instanceof FileInputStream fileInputStream) {
            fileInputStream.getChannel().position(0);
            return;
        }
        if (inputStream instanceof ReusableInputStream reusableInputStream) {
            reusableInputStream.close();
            return;
        }
        inputStreamReader.close();
    }
}
