package io.github.thomashan.tradingchart.lang

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.util.stream.Collectors

class DoubleParserJavaLangTest {
    private Random random
    private int streamSize

    @BeforeEach
    void setUp() {
        this.random = new Random()
        this.streamSize = 10_000
    }

    @Test
    void testParseApproxCharSequence_RandomSmall() {
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
                double parsed = DoubleParserJavaLang.parseApprox(entry.key)
                double ulp = Math.ulp(parsed)
                assert parsed + 8 * ulp >= entry.value && parsed - 8 * ulp <= entry.value
            })
    }
}
