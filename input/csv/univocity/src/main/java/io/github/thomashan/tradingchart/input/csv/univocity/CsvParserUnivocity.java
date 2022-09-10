package io.github.thomashan.tradingchart.input.csv.univocity;

import com.univocity.parsers.csv.CsvParserSettings;
import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvParser;
import io.github.thomashan.tradingchart.input.csv.DefaultHeader;
import io.github.thomashan.tradingchart.input.csv.OhlcCreator;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class CsvParserUnivocity<O extends Ohlc<O, ?>> implements CsvParser<O> {
    private final CsvParserSettings csvParserSettings;
    private final com.univocity.parsers.csv.CsvParser csvParser;
    private Map<String, Integer> headerIndexes;
    private BiFunction<String[], Map<String, Integer>, O> createFunction;

    CsvParserUnivocity(boolean extractHeader) {
        this.csvParserSettings = new CsvParserSettings();
        csvParserSettings.setHeaderExtractionEnabled(extractHeader);
        this.csvParser = new com.univocity.parsers.csv.CsvParser(csvParserSettings);
    }

    private void setHeaderIndexes(String[] row) {
        if (Objects.isNull(headerIndexes)) {
            this.headerIndexes = DefaultHeader.getDefaultHeaders(row);
        }
    }

    private void setCreateFunction(String[] row) {
        if (Objects.isNull(createFunction)) {
            this.createFunction = OhlcCreator.getCreator(row);
        }
    }

    @Override
    public void parse(InputStream inputStream, Consumer<O> consumer) {
        csvParser.parseAll(inputStream).stream()
                .map(it -> {
                    setHeaderIndexes(it);
                    setCreateFunction(it);
                    return createFunction.apply(it, headerIndexes);
                })
                .forEach(consumer);
    }
}
