package io.github.thomashan.tradingchart.input.csv;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CsvHeader {
    private static final Map<Map<String, Integer>, CsvHeader> CACHE = new HashMap<>();
    private final Map<String, Integer> columnNameToIndex;
    private final Map<Integer, String> indexToColumnName;

    private CsvHeader(Map<String, Integer> columnNameToIndex) {
        this.columnNameToIndex = columnNameToIndex;
        this.indexToColumnName = columnNameToIndex.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public static CsvHeader getCsvHeader(Map<String, Integer> columnNameToIndex) {
        if (CACHE.containsKey(columnNameToIndex)) {
            return CACHE.get(columnNameToIndex);
        }
        CsvHeader csvHeader = new CsvHeader(columnNameToIndex);
        CACHE.put(columnNameToIndex, csvHeader);
        return csvHeader;
    }

    public Map<String, Integer> getColumnNameToIndex() {
        return columnNameToIndex;
    }

    public Map<Integer, String> getIndexToColumnName() {
        return indexToColumnName;
    }

    @Override
    public int hashCode() {
        return columnNameToIndex.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return columnNameToIndex.equals(obj);
    }
}
