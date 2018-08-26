package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.DefaultHeader;
import io.github.thomashan.tradingchart.input.csv.OhlcCreator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface CsvParserStream<O extends Ohlc<?>> extends CsvParser<O> {
    String[] split(String string);

    Map<String, Integer> getHeaderIndexes();

    void setHeaderIndexes(Map<String, Integer> headerIndexes);

    BiFunction<String[], Map<String, Integer>, O> getCreateFunction();

    void setCreateFunction(BiFunction<String[], Map<String, Integer>, O> createFunction);

    @Override
    default Stream<O> parse(InputStream inputStream) {
        return parse(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines());
    }

    @Override
    default Stream<O> parse(Stream<String> inputRows) {
        // FIXME: we shouldn't need to use an AtomicBoolean to see if the current row is the first row
        AtomicBoolean isFirstRow = new AtomicBoolean(true);
        return inputRows
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
                    return getCreateFunction().apply(row, getHeaderIndexes());
                });
    }

    private void setDefaultHeaderIndexes(String[] row) {
        if (Objects.isNull(getHeaderIndexes())) {
            setHeaderIndexes(DefaultHeader.getDefaultHeaders(row));
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
        setHeaderIndexes(headerMap);
    }

    private boolean isHeader(boolean isFirstRow, String inputRow) {
        return isFirstRow && Character.isAlphabetic(inputRow.charAt(0));
    }
}
