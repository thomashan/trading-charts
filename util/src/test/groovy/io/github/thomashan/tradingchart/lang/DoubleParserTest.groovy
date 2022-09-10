package io.github.thomashan.tradingchart.lang


import org.junit.jupiter.api.Test

import java.util.stream.Collectors
import java.util.stream.IntStream

class DoubleParserTest {
    @Test
    void testParseCharSequence_Random() {
        Random random = new Random() // creating Random object

        Map<String, Double> randomDoubles = IntStream.range(1, 1000)
            .mapToDouble(integer -> random.nextDouble())
            .mapToObj(doubleValue -> Map.entry(Double.toString(doubleValue), doubleValue))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))

        randomDoubles.entrySet().stream()
            .forEach(entry -> {
                double ulp = Math.ulp(entry.value)
                double parsed = DoubleParser.parseApprox(entry.key)
                assert parsed - 10 * ulp < parsed
                assert parsed + 10 * ulp > parsed
            })
    }
}
