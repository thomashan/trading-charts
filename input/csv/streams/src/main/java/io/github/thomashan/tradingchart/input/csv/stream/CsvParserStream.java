package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvHeader;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.DefaultCsvHeader;
import io.github.thomashan.tradingchart.input.csv.OhlcCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface CsvParserStream<O extends Ohlc<O, ?>> extends CsvParser<O> {
    String[] split(String string);

    CsvHeader getCsvHeader();

    void setCsvHeader(CsvHeader csvHeader);

    BiFunction<String[], CsvHeader, O> getCreateFunction();

    void setCreateFunction(BiFunction<String[], CsvHeader, O> createFunction);

    private void setDefaultHeaderIndexes(String[] row) {
        if (Objects.isNull(getCsvHeader())) {
            setCsvHeader(DefaultCsvHeader.getDefaultHeaders(row));
        }
    }

    private void setCreateFunction(String[] headers) {
        if (Objects.isNull(getCreateFunction())) {
            setCreateFunction(OhlcCreator.getCreator(headers));
        }
    }

    private void setHeaderInfo(String headerString) {
        String[] header = split(headerString);
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            headerMap.put(header[i], i);
        }
        setCsvHeader(CsvHeader.getCsvHeader(headerMap));
    }

    private boolean isHeader(boolean isFirstRow, String inputRow) {
        return isFirstRow && Character.isAlphabetic(inputRow.charAt(0));
    }

    @Override
    default void parse(InputStream inputStream, Consumer<O> consumer) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            // FIXME: we shouldn't need to use an AtomicBoolean to see if the current row is the first row
            AtomicBoolean isFirstRow = new AtomicBoolean(true);
            bufferedReader.lines()
                    .map(inputRow -> {
                        if (isHeader(isFirstRow.get(), inputRow)) {
                            isFirstRow.set(false);
                            setHeaderInfo(inputRow);
                            return Optional.<String[]>empty();
                        }
                        isFirstRow.set(false);
                        return Optional.of(split(inputRow));
                    })
                    .flatMap(Optional::stream)
                    .map(row -> {
                        setDefaultHeaderIndexes(row);
                        setCreateFunction(row);
                        return getCreateFunction().apply(row, getCsvHeader());
                    })
                    .forEach(consumer);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
