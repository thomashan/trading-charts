package io.github.thomashan.tradingchart.input.csv.charbuffer;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvHeader;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.DefaultCsvHeader;
import io.github.thomashan.tradingchart.input.csv.OhlcCreator;
import io.github.thomashan.tradingchart.io.LineReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CsvParserCharBuffer<O extends Ohlc<O, ?>> implements CsvParser<O> {
    private static final int DEFAULT_LINE_LENGTH = 128;
    private static final int DEFAULT_COLUMN_LENGTH = 128;
    private final CharBuffer lineBuffer;
    private final CharBuffer columnBuffer;
    private int numberOfColumns;
    private CsvHeader csvHeader;
    private BiFunction<Integer, CharSequence, O> ohlcCreator;

    public CsvParserCharBuffer() {
        this.lineBuffer = CharBuffer.allocate(DEFAULT_LINE_LENGTH);
        this.columnBuffer = CharBuffer.allocate(DEFAULT_COLUMN_LENGTH);
    }

    CsvParserCharBuffer(BiFunction<Integer, CharSequence, O> ohlcCreator) {
        this();
        this.ohlcCreator = ohlcCreator;
    }

    @Override
    public void parse(InputStream inputStream, Consumer<O> consumer) {
        try (LineReader lineReader = new LineReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            int lineNumber = 0;
            while (lineReader.readLine(lineBuffer) > 0) {
                lineBuffer.flip();
                if (numberOfColumns == 0) {
                    this.numberOfColumns = getNumberOfColumns();
                }
                if (isHeader(lineNumber)) {
                    setHeaderInfo();
                } else if (lineNumber == 0) {
                    setDefaultHeaderInfo();
                }

                if (Objects.isNull(ohlcCreator)) {
                    this.ohlcCreator = OhlcCreator.getCreator(numberOfColumns, csvHeader);
                }

                int columnNumber = 0;
                if (charsLeftInLine()) {
                    while (true) {
                        columnBuffer.clear();
                        while (charsLeftInLine()) {
                            char character = lineBuffer.get();
                            if (',' == character) {
                                break;
                            } else {
                                columnBuffer.append(character);
                            }
                        }
                        columnBuffer.flip();
                        O ohlc = ohlcCreator.apply(columnNumber, columnBuffer);
                        columnNumber++;

                        if (!charsLeftInLine()) {
                            consumer.accept(ohlc);
                            break;
                        }
                    }
                }
                lineNumber++;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setDefaultHeaderInfo() {
        this.csvHeader = DefaultCsvHeader.getDefaultHeaders(numberOfColumns);
    }

    private int getNumberOfColumns() {
        int columnNumber = 0;
        while (true) {
            while (charsLeftInLine()) {
                char character = lineBuffer.get();
                if (',' == character) {
                    break;
                }
            }
            columnNumber++;
            if (!charsLeftInLine()) {
                break;
            }
        }
        lineBuffer.flip();
        return columnNumber;
    }

    private boolean isHeader(int lineNumber) {
        return lineNumber == 0 && Character.isAlphabetic(lineBuffer.get(0));
    }

    private void setHeaderInfo() {
        Map<String, Integer> headerIndexes = new HashMap<>();
        int columnNumber = 0;
        while (true) {
            columnBuffer.clear();
            while (charsLeftInLine()) {
                char character = lineBuffer.get();
                if (',' == character) {
                    break;
                } else {
                    columnBuffer.append(character);
                }
            }

            headerIndexes.put(columnBuffer.flip().toString(), columnNumber);
            columnNumber++;
            if (!charsLeftInLine()) {
                break;
            }
        }
        if (columnNumber == 10 || columnNumber == 6) {
            this.csvHeader = CsvHeader.getCsvHeader(headerIndexes);
        } else {
            throw new IllegalArgumentException("there must be 10 fields for bid, ask and 6 fields for mid");
        }
    }

    private boolean charsLeftInLine() {
        return lineBuffer.length() > 0;
    }
}
