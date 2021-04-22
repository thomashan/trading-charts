package io.github.thomashan.lang

import java.nio.CharBuffer
import java.nio.DirectByteBuffer

class CharSequenceParser {
    static final int INT_DECIMAL_DIGITS = 9
    static final int MAX_DECIMAL_DIGITS = 16
    static final int MAX_DECIMAL_EXPONENT = 308
    static final int BIG_DECIMAL_EXPONENT = 324
    static final int MIN_DECIMAL_EXPONENT = -324
    private static final double[] TINY_10_POW = [1e-16, 1e-32, 1e-64, 1e-128, 1e-256]
    private static final double[] BIG_10_POW = [1e16, 1e32, 1e64, 1e128, 1e256]
    private static final double[] SMALL_10_POW = [1.0e0, 1.0e1, 1.0e2, 1.0e3, 1.0e4, 1.0e5, 1.0e6, 1.0e7, 1.0e8, 1.0e9,
                                                  1.0e10, 1.0e11, 1.0e12, 1.0e13, 1.0e14, 1.0e15, 1.0e16, 1.0e17, 1.0e18, 1.0e19,
                                                  1.0e20, 1.0e21, 1.0e22]
    private static final int MAX_SMALL_TEN = SMALL_10_POW.length - 1
    private static final String INFINITY_REP = "Infinity"
    private static final int INFINITY_LENGTH = INFINITY_REP.length()
    private static final String NAN_REP = "NaN"
    private static final int NAN_LENGTH = NAN_REP.length()
    private static ThreadLocal<CharBuffer> digitsBuffer = ThreadLocal.withInitial(() -> DirectByteBuffer.allocateDirect(128).asCharBuffer())

    private CharSequenceParser() {
        throw new AssertionError("can't instantiate ${this.class.simpleName}")
    }

    static double parseDoubleApprox(CharSequence charSequence) throws NumberFormatException {
        if (Objects.isNull(charSequence)) {
            throw new NumberFormatException("null String")
        }

        CharBuffer digits = digitsBuffer.get()
        digits.clear()
        // for reference of the original implementation refer to jdk.internal.math.FloatingDecimal.readJavaFormatString(String)
        boolean isNegative = false
        boolean signSeen = false

        int length = charSequence.length()
        if (length == 0) {
            throw new NumberFormatException("empty String")
        }

        int i = 0
        switch (charSequence[i]) {
            case '-':
                isNegative = true
            case '+':
                i++
                signSeen = true
        }
        char character = charSequence[i]
        if (character == 'N') {
            if (charSequence.length() != NAN_LENGTH) {
                throwException(charSequence)
            }
            for (int j = 0; j < NAN_LENGTH; j++, i++) {
                if (charSequence[i] != NAN_REP.charAt(j)) {
                    throwException(charSequence)
                }
            }
            return Double.NaN
        } else if (character == 'I') {
            if ((!signSeen && charSequence.length() != INFINITY_LENGTH) || (signSeen && charSequence.length() != INFINITY_LENGTH + 1)) {
                throwException(charSequence)
            }
            for (int j = 0; j < INFINITY_LENGTH; j++, i++) {
                if (charSequence[i] != INFINITY_REP.charAt(j)) {
                    throwException(charSequence)
                }
            }
            return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY
        }

        int leadingZeroes = 0
        int trailingZeroes = 0
        boolean decimalPointSeen = false
        int decimalPointIndex = 0
        int numberOfDigits = 0

        while (i < length) {
            character = charSequence[i]
            if (character == '0') {
                leadingZeroes++
            } else if (character == '.') {
                if (decimalPointSeen) {
                    throw new NumberFormatException("multiple decimal points")
                }
                decimalPointIndex = i
                if (signSeen) {
                    decimalPointIndex -= 1
                }
                decimalPointSeen = true
            } else {
                break
            }
            i++
        }

        while (i < length) {
            character = charSequence[i]
            if (character >= '1' && character <= '9') {
                digits.put(numberOfDigits++, character)
                trailingZeroes = 0
            } else if (character == '0') {
                digits.put(numberOfDigits++, character)
                trailingZeroes++
            } else if (character == '.') {
                if (decimalPointSeen) {
                    throw new NumberFormatException("multiple decimal points")
                }
                decimalPointIndex = i
                if (signSeen) {
                    decimalPointIndex -= 1
                }
                decimalPointSeen = true
            } else {
                break
            }
            i++
        }
        numberOfDigits -= trailingZeroes
        boolean isZero = numberOfDigits == 0
        if (isZero && leadingZeroes == 0) {
            throwException(charSequence)
        }

        int decimalExponent = decimalPointSeen ? decimalPointIndex - leadingZeroes : numberOfDigits + trailingZeroes
        if ((i < length) && ((character == 'e') || (character == 'E'))) {
            int exponentSign = 1
            int exponentValue = 0
            int reallyBig = Integer.MAX_VALUE / 10
            boolean exponentOverflow = false
            switch (charSequence[++i]) {
                case '-':
                    exponentSign = -1
                case '+':
                    i++
            }
            int exponentAt = i

            while (i < length) {
                if (exponentValue >= reallyBig) {
                    exponentOverflow = true
                }
                character = charSequence[i++]
                if (character >= '0' && character <= '9') {
                    exponentValue = exponentValue * 10 + ((int) character - (int) '0')
                } else {
                    i--
                    break
                }
            }

            int exponentLimit = BIG_DECIMAL_EXPONENT + numberOfDigits + trailingZeroes
            if (exponentOverflow || (exponentValue > exponentLimit)) {
                if (!exponentOverflow && (exponentSign == 1 && decimalExponent < 0)
                        && (exponentValue + decimalExponent) < exponentLimit) {
                    decimalExponent += exponentValue
                } else {
                    decimalExponent = exponentSign * exponentLimit
                }
            } else {
                decimalExponent = decimalExponent + exponentSign * exponentValue
            }

            if (i == exponentAt) {
                throwException(charSequence)
            }
        }
        if (i < length && ((i != length - 1) ||
                (charSequence[i] != 'f' &&
                        charSequence[i] != 'F' &&
                        charSequence[i] != 'd' &&
                        charSequence[i] != 'D'))) {
            throwException(charSequence)
        }
        if (isZero) {
            return isNegative ? -0 : 0
        }

        return returnValue(isNegative, decimalExponent, digits, numberOfDigits)
    }

    private static void throwException(CharSequence charSequence) {
        throw new NumberFormatException("For input string: ${charSequence}");
    }

    private static double returnValue(boolean isNegative, int decimalExponent, CharSequence digits, int numberOfDigits) {
        // for reference of the original implementation refer to jdk.internal.math.FloatingDecimal.ASCIIBinaryBuffer.doubleValue()
        int totalDigits = Math.min(numberOfDigits, MAX_DECIMAL_DIGITS + 2)

        int integerValue = (int) digits.charAt(0) - (int) '0'
        int integerDigits = Math.min(totalDigits, INT_DECIMAL_DIGITS)
        for (int i = 1; i < integerDigits; i++) {
            integerValue = integerValue * 10 + (int) digits.charAt(i) - (int) '0'
        }
        long longValue = (long) integerValue
        for (int i = integerDigits; i < totalDigits; i++) {
            longValue = longValue * 10L + (long) ((int) digits.charAt(i) - (int) '0')
        }
        double doubleValue = (double) longValue
        int exponent = decimalExponent - totalDigits

        if (numberOfDigits <= MAX_DECIMAL_DIGITS) {
            if (exponent == 0 || doubleValue == 0.0) {
                return isNegative ? -doubleValue : doubleValue
            } else if (exponent >= 0) {
                if (exponent <= MAX_SMALL_TEN) {
                    double value = doubleValue * SMALL_10_POW[exponent]
                    return isNegative ? -value : value
                }
                int slop = MAX_DECIMAL_DIGITS - totalDigits
                if (exponent <= MAX_SMALL_TEN + slop) {
                    doubleValue *= SMALL_10_POW[slop]
                    double value = doubleValue * SMALL_10_POW[exponent - slop]
                    return isNegative ? -value : value
                }
            } else {
                if (exponent >= -MAX_SMALL_TEN) {
                    double value = doubleValue / SMALL_10_POW[-exponent]
                    return isNegative ? -value : value
                }
            }
        }

        if (exponent > 0) {
            if (decimalExponent > MAX_DECIMAL_EXPONENT + 1) {
                return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY
            }
            if ((exponent & 15) != 0) {
                doubleValue *= SMALL_10_POW[exponent & 15]
            }
            if ((exponent >>= 4) != 0) {
                int j
                for (j = 0; exponent > 1; j++, exponent >>= 1) {
                    if ((exponent & 1) != 0) {
                        doubleValue *= BIG_10_POW[j]
                    }
                }
                double value = doubleValue * BIG_10_POW[j]
                if (Double.isInfinite(value)) {
                    value = doubleValue / 2.0
                    value *= BIG_10_POW[j]
                    if (Double.isInfinite(value)) {
                        return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY
                    }
                    value = Double.MAX_VALUE
                }
                doubleValue = value
            }
        } else if (exponent < 0) {
            exponent = -exponent
            if (decimalExponent < MIN_DECIMAL_EXPONENT - 1) {
                return isNegative ? -0.0 : 0.0
            }
            if ((exponent & 15) != 0) {
                doubleValue /= SMALL_10_POW[exponent & 15]
            }
            if ((exponent >>= 4) != 0) {
                int j
                for (j = 0; exponent > 1; j++, exponent >>= 1) {
                    if ((exponent & 1) != 0) {
                        doubleValue *= TINY_10_POW[j]
                    }
                }
                double value = doubleValue * TINY_10_POW[j]
                if (value == 0.0) {
                    value = doubleValue * 2.0
                    value *= TINY_10_POW[j]
                    if (value == 0.0) {
                        return isNegative ? -0.0 : 0.0
                    }
                    value = Double.MIN_VALUE
                }
                doubleValue = value
            }
        }

        // omitted as we don't need to worry about long string and converting it into a double
    }
}
