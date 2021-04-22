package io.github.thomashan.lang

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows

class CharSequenceParserTest {
    @Test
    void testParseDoubleApprox() {
        List<String> goodStrings = [
                "0.8725499898645946", "0", "0d", "0.0", "0.0d", "0.1", "0.1d", ".1", ".1d", "-0.1", "-0.1d", "-.1",
                "-.1d", "0.1e-23", "0.1e-23d", "0.87264", "8.7264e-1", "0.9958238940927899", "0.10755873119123438",
                "0.10238593313905286", "0.47472308836728294", "0.23993888096337634",
                Double.MAX_VALUE.toString(),
                Double.MIN_VALUE.toString(),
                Double.NaN.toString(), Double.POSITIVE_INFINITY.toString(), "+${Double.POSITIVE_INFINITY.toString()}", Double.NEGATIVE_INFINITY.toString()
        ]

        goodStrings.forEach(this::assertDouble)

        List<String> badStrings = [null, "", "a", "Na", "NaNa", "NaNa", "+${Double.NaN.toString()}", "Infinit", "Infinitya",
                                   "-Infinit", "-Infinitya", "1.1.", "..1"]
        badStrings.each { assertThrows(NumberFormatException, () -> CharSequenceParser.parseDoubleApprox("")) }
    }

    @Test
    void testParseDoubleApprox_RandomString() {
        Random random = new Random()
        random.doubles()
                .limit(1_000)
                .mapToObj(Object::toString)
                .forEach(this::assertDouble)
    }

    private void assertDouble(String doubleString) {
        double exact = Double.parseDouble(doubleString)
        double parsed = CharSequenceParser.parseDoubleApprox(doubleString)
        double ulp = Math.ulp(exact)

        if (ulp != Double.NaN) {
            assertEquals(exact, parsed, 2 * ulp)
        } else {
            assertEquals(exact, parsed)
        }
    }
}
