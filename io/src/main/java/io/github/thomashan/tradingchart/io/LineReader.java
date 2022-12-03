package io.github.thomashan.tradingchart.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.Objects;

public class LineReader extends Reader {
    private static int defaultCharBufferSize = 8192;

    // FIXME: thomas - is there a way to reus the Reader pointing to different files??
    private ReusableReader reusableReader;
    private CharBuffer charBuffer;
    private int nChars, nextChar;
    private boolean skipLineFeed = false;
    private static final int INVALIDATED = -2;
    private static final int UNMARKED = -1;
    private int markedChar = UNMARKED;
    private int readAheadLimit = 0; /* Valid only when markedChar > 0 */

    public LineReader(InputStream inputStream, int bufferSize) {
        super(inputStream);
        if (bufferSize <= 0)
            throw new IllegalArgumentException("Buffer size <= 0");
        this.reusableReader = new ReusableReader(inputStream);
        this.charBuffer = CharBuffer.allocate(bufferSize);
        nextChar = nChars = 0;
    }

    public LineReader(InputStream inputStream) {
        this(inputStream, defaultCharBufferSize);
    }

    @Override
    public int read(char[] charBuffer, int off, int len) throws IOException {
        return reusableReader.read(charBuffer, off, len);
    }

    @Override
    public void close() throws IOException {
        synchronized (lock) {
            if (reusableReader == null) {
                return;
            }
            reusableReader.close();
        }
    }

    private void ensureOpen() throws IOException {
        if (reusableReader == null) {
            throw new IOException("Stream closed");
        }
    }

    public int readLine(CharBuffer charBuffer) throws IOException {
        return readLine(charBuffer, false);
    }

    int readLine(CharBuffer inputCharBuffer, boolean ignoreLineFeed) throws IOException {
        Objects.requireNonNull(inputCharBuffer);
        inputCharBuffer.clear();

        int charsRead = 0;
        int startChar;
        synchronized (lock) {
            ensureOpen();
            boolean omitLineFeed = ignoreLineFeed || skipLineFeed;

            while (true) {
                if (nextChar >= nChars)
                    fillCharBuffer();
                if (nextChar >= nChars) { /* EOF */
                    return 0;
                }
                boolean eol = false;
                char c = 0;
                int i;

                /* Skip a leftover '\n', if necessary */
                if (omitLineFeed && (charBuffer.get(nextChar) == '\n'))
                    nextChar++;
                skipLineFeed = false;
                omitLineFeed = false;

                for (i = nextChar; i < nChars; i++) {
                    c = charBuffer.get(i);
                    if ((c == '\n') || (c == '\r')) {
                        eol = true;
                        break;
                    } else {
                        inputCharBuffer.append(c);
                    }
                }

                startChar = nextChar;
                nextChar = i;
                charsRead = charsRead + (nextChar - startChar);

                if (eol) {
                    nextChar++;
                    if (c == '\r') {
                        skipLineFeed = true;
                    }
                    return charsRead;
                }
            }
        }
    }

    /**
     * Fills the input buffer, taking the mark into account if it is valid.
     */
    private void fillCharBuffer() throws IOException {
        int dst;
        if (markedChar <= UNMARKED) {
            /* No mark */
            dst = 0;
        } else {
            /* Marked */
            int delta = nextChar - markedChar;
            if (delta >= readAheadLimit) {
                /* Gone past read-ahead limit: Invalidate mark */
                markedChar = INVALIDATED;
                readAheadLimit = 0;
                dst = 0;
            } else {
                if (readAheadLimit <= charBuffer.length()) {
                    /* Shuffle in the current buffer */
                    System.arraycopy(charBuffer.array(), markedChar, charBuffer.array(), 0, delta);
                } else {
                    /* Reallocate buffer to accommodate read-ahead limit */
                    char[] ncb = new char[readAheadLimit];
                    System.arraycopy(charBuffer.array(), markedChar, ncb, 0, delta);
                }
                markedChar = 0;
                dst = delta;
                nextChar = nChars = delta;
            }
        }

        int n;
        do {
            n = reusableReader.read(charBuffer.array(), dst, charBuffer.length() - dst);
        } while (n == 0);
        if (n > 0) {
            nChars = dst + n;
            nextChar = dst;
        }
    }
}
