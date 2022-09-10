package io.github.thomashan.tradingchart.input.csv.stream;

import io.github.thomashan.tradingchart.domain.ohlc.Ohlc;
import io.github.thomashan.tradingchart.input.csv.CsvHeader;

import java.util.function.BiFunction;

public abstract class BaseCsvParserStream<O extends Ohlc<O, ?>> implements CsvParserStream<O> {
    private CsvHeader csvHeader;
    private BiFunction<String[], CsvHeader, O> createFunction;

    BaseCsvParserStream() {
        this.createFunction = null;
    }

    BaseCsvParserStream(BiFunction<String[], CsvHeader, O> createFunction) {
        this.createFunction = createFunction;
    }

    @Override
    public CsvHeader getCsvHeader() {
        return csvHeader;
    }

    @Override
    public void setCsvHeader(CsvHeader csvHeader) {
        this.csvHeader = csvHeader;
    }

    @Override
    public BiFunction<String[], CsvHeader, O> getCreateFunction() {
        return createFunction;
    }

    @Override
    public void setCreateFunction(BiFunction<String[], CsvHeader, O> createFunction) {
        this.createFunction = createFunction;
    }
}
