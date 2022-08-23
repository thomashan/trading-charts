package io.github.thomashan.tradingchart.input.csv.bytebuffer;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.function.Consumer;

public class CsvParserByteBuffer<O extends Ohlc<O, ?>> implements CsvParser<O> {
    private static final int EOF = -1;
    private static final int NEW_LINE = '\n';
    private static final int COMMA = ',';

    @SuppressWarnings("unchecked")
    @Override
    public void parse(InputStream inputStream, Consumer<O> consumer) {
//        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
//            CharBuffer charBuffer = CharBuffer.allocate(512);
//            int numberOfBytesRead;
//            do {
//                // .read(charBuffer) from Readable class
//                numberOfBytesRead = inputStreamReader.read(charBuffer);
////                String string = charBuffer.flip().toString();
//            } while (numberOfBytesRead != 0);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }


        // time: 0.6s
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
//            CharBuffer charBuffer = CharBuffer.allocate(512);
//            int numberOfBytesRead;
//            do {
//                // .read(charBuffer) from Readable class
//                numberOfBytesRead = bufferedReader.read(charBuffer);
////                String string = charBuffer.flip().toString();
//            } while (numberOfBytesRead != 0);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }


        // time: 7.6s
//        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
//            int read;
//            while ((read = inputStreamReader.read()) != EOF) {
//            }
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }


        // time: 3.6s
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
//            int read;
//            while ((read = bufferedReader.read()) != EOF) {
//            }
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
//            String line;
//            do {
//                line = bufferedReader.readLine();
//            } while (line != null);
//            int a = 1;
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // document why reading ints directly from the input stream one at a time is slow!
        // time ~1min 5s
//        try {
//            int read;
//            while ((read = inputStream.read()) != EOF) {
//            }
//            inputStream.close();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // time: 1min 5s - 1 bytes
        // time: 32.7s - 2 bytes
        // time: 2s - 100 bytes
        // time: 1.5s - 256 bytes
//        byte[] read = new byte[1];
//        int bytesRead;
//        try {
//            do {
//                bytesRead = inputStream.read(read, 0, read.length);
//            } while (bytesRead != EOF);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }


        // time 1min 4s
//        int read;
//        try {
//            do {
//                read = inputStream.read();
//            } while (read != EOF);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // time ~1s
//        try {
//            byte[] bytes = inputStream.readAllBytes();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // time ~1min 5s with 1bytes
        // time ~33s with 2bytes
        // time ~2s with 100bytes
        // time ~1.6s with 256bytes
        // time ~1s with 1000bytes
//        try {
//            byte[] bytes;
//            do {
//                bytes = inputStream.readNBytes(256);
//            } while (bytes.length != 0);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }


        // time ~7s
//        int read;
//        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
//            while ((read = inputStreamReader.read()) != EOF) {
//            }
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // time ~1s
//        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
//            byte[] bytes = bufferedInputStream.readAllBytes();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // time 3s
//        int read;
//        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
//            while ((read = bufferedInputStream.read()) != EOF) {
//            }
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        // time ~1min 12s
//        int read;
//        try (InputStream newInputStream = Channels.newInputStream(Channels.newChannel(inputStream))) {
//            while ((read = newInputStream.read()) != EOF) {
//            }
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

    }
}
