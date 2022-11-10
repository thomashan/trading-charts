package io.github.thomashan.tradingchart.lang

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.util.stream.Collectors

class DoubleParserGeoMapTest {
    private Random random
    private int streamSize
    private DoubleParserGeoMap doubleParser

    @BeforeEach
    void setUp() {
        this.random = new Random()
        this.streamSize = 10_000
        this.doubleParser = new DoubleParserGeoMap()
    }

    @Test
    void testParseApproxCharSequence_Small() {
        assertParsedDouble(1)
    }

    @Test
    void testParseApproxCharSequence_RandomLarge() {
        assertParsedDouble(Double.MAX_VALUE)
    }

    private void assertParsedDouble(double randomNumberBound) {
        Map<String, Double> randomDoubles = random.doubles(streamSize, 0, randomNumberBound)
            .mapToObj(doubleValue -> Map.entry(Double.toString(doubleValue), doubleValue))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))

        randomDoubles.entrySet().stream()
            .forEach(entry -> {
                double parsed = doubleParser.parse(entry.key)
                assert parsed == entry.value
            })
    }
}
