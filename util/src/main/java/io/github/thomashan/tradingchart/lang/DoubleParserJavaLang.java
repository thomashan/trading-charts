package io.github.thomashan.tradingchart.lang;

import java.nio.CharBuffer;
import java.util.Objects;

/**
 * Original implementation from {@link java.lang.Double#parseDouble(String)}
 */
class DoubleParserJavaLang {
    private static final CharSequence NAN_REP = "NaN";
    private static final int NAN_LENGTH = NAN_REP.length();
    private static final CharSequence INFINITY_REP = "Infinity";
    private static final int INFINITY_LENGTH = INFINITY_REP.length();
    private static final int BIG_DECIMAL_EXPONENT = 324; // i.e. abs(MIN_DECIMAL_EXPONENT)
    private static final int MAX_DECIMAL_DIGITS = 15;
    private static final int INT_DECIMAL_DIGITS = 9;
    private static final double[] SMALL_10_POW = {
            1.0e0,
            1.0e1, 1.0e2, 1.0e3, 1.0e4, 1.0e5,
            1.0e6, 1.0e7, 1.0e8, 1.0e9, 1.0e10,
            1.0e11, 1.0e12, 1.0e13, 1.0e14, 1.0e15,
            1.0e16, 1.0e17, 1.0e18, 1.0e19, 1.0e20,
            1.0e21, 1.0e22
    };
    private static final int MAX_SMALL_TEN = SMALL_10_POW.length - 1;
    private static final int MAX_DECIMAL_EXPONENT = 308;
    private static final double[] BIG_10_POW = {1e16, 1e32, 1e64, 1e128, 1e256};
    private static final int MIN_DECIMAL_EXPONENT = -324;
    private static final double[] TINY_10_POW = {1e-16, 1e-32, 1e-64, 1e-128, 1e-256};
    private static final ThreadLocal<CharBuffer> charSequences = ThreadLocal.withInitial(() -> CharBuffer.allocate(32));


    private DoubleParserJavaLang() {
        throw new AssertionError("not instantiable");
    }

    @SuppressWarnings("fallthrough")
    public static double parseApprox(CharSequence charSequence) {
        Objects.nonNull(charSequence);
        int length = charSequence.length();
        if (length == 0) {
            throw new NumberFormatException("empty String");
        }

        boolean isNegative = false;
        boolean signSeen = false;
        int decExp;
        char c;

        parseNumber:
        try {
            int i = 0;
            switch (charSequence.charAt(i)) {
                case '-':
                    isNegative = true;
                    // FALLTHROUGH
                case '+':
                    i++;
                    signSeen = true;
            }
            c = charSequence.charAt(i);
            if (c == 'N') { // Check for NaN
                if ((length - i) == NAN_LENGTH && NAN_REP.equals(charSequence.subSequence(i, i + NAN_LENGTH))) {
                    return Double.NaN;
                }
                // something went wrong, throw exception
                break parseNumber;
            } else if (c == 'I') { // Check for Infinity strings
                if ((length - i) == INFINITY_LENGTH && INFINITY_REP.equals(charSequence.subSequence(i, i + INFINITY_LENGTH))) {
                    return isNegative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                }
                // something went wrong, throw exception
                break parseNumber;
            }  // look for and process decimal floating-point string
            // hex decimal is not supported

            CharBuffer digits = charSequences.get().clear();
            boolean decSeen = false;
            int nDigits = 0;
            int decPt = 0;
            int nLeadZero = 0;
            int nTrailZero = 0;

            skipLeadingZerosLoop:
            while (i < length) {
                c = charSequence.charAt(i);
                if (c == '0') {
                    nLeadZero++;
                } else if (c == '.') {
                    if (decSeen) {
                        // already saw one ., this is the 2nd.
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i;
                    if (signSeen) {
                        decPt -= 1;
                    }
                    decSeen = true;
                } else {
                    break skipLeadingZerosLoop;
                }
                i++;
            }
            digitLoop:
            while (i < length) {
                c = charSequence.charAt(i);
                if (c >= '1' && c <= '9') {
                    digits.put(nDigits++, c);
                    nTrailZero = 0;
                } else if (c == '0') {
                    digits.put(nDigits++, c);
                    nTrailZero++;
                } else if (c == '.') {
                    if (decSeen) {
                        // already saw one ., this is the 2nd.
                        throw new NumberFormatException("multiple points");
                    }
                    decPt = i;
                    if (signSeen) {
                        decPt -= 1;
                    }
                    decSeen = true;
                } else {
                    break digitLoop;
                }
                i++;
            }
            nDigits -= nTrailZero;
            //
            // At this point, we've scanned all the digits and decimal
            // point we're going to see. Trim off leading and trailing
            // zeros, which will just confuse us later, and adjust
            // our initial decimal exponent accordingly.
            // To review:
            // we have seen i total characters.
            // nLeadZero of them were zeros before any other digits.
            // nTrailZero of them were zeros after any other digits.
            // if ( decSeen ), then a . was seen after decPt characters
            // ( including leading zeros which have been discarded )
            // nDigits characters were neither lead nor trailing
            // zeros, nor point
            //
            //
            // special hack: if we saw no non-zero digits, then the
            // answer is zero!
            // Unfortunately, we feel honor-bound to keep parsing!
            //
            boolean isZero = (nDigits == 0);
            if (isZero && nLeadZero == 0) {
                // we saw NO DIGITS AT ALL,
                // not even a crummy 0!
                // this is not allowed.
                break parseNumber; // go throw exception
            }
            //
            // Our initial exponent is decPt, adjusted by the number of
            // discarded zeros. Or, if there was no decPt,
            // then its just nDigits adjusted by discarded trailing zeros.
            //
            if (decSeen) {
                decExp = decPt - nLeadZero;
            } else {
                decExp = nDigits + nTrailZero;
            }

            //
            // Look for 'e' or 'E' and an optionally signed integer.
            //
            if ((i < length) && (((c = charSequence.charAt(i)) == 'e') || (c == 'E'))) {
                int expSign = 1;
                int expVal = 0;
                int reallyBig = Integer.MAX_VALUE / 10;
                boolean expOverflow = false;
                switch (charSequence.charAt(++i)) {
                    case '-':
                        expSign = -1;
                        //FALLTHROUGH
                    case '+':
                        i++;
                }
                int expAt = i;
                expLoop:
                while (i < length) {
                    if (expVal >= reallyBig) {
                        // the next character will cause integer
                        // overflow.
                        expOverflow = true;
                    }
                    c = charSequence.charAt(i++);
                    if (c >= '0' && c <= '9') {
                        expVal = expVal * 10 + ((int) c - (int) '0');
                    } else {
                        i--;           // back up.
                        break expLoop; // stop parsing exponent.
                    }
                }
                int expLimit = BIG_DECIMAL_EXPONENT + nDigits + nTrailZero;
                if (expOverflow || (expVal > expLimit)) {
                    // There is still a chance that the exponent will be safe to
                    // use: if it would eventually decrease due to a negative
                    // decExp, and that number is below the limit.  We check for
                    // that here.
                    if (!expOverflow && (expSign == 1 && decExp < 0)
                            && (expVal + decExp) < expLimit) {
                        // Cannot overflow: adding a positive and negative number.
                        decExp += expVal;
                    } else {
                        //
                        // The intent here is to end up with
                        // infinity or zero, as appropriate.
                        // The reason for yielding such a small decExponent,
                        // rather than something intuitive such as
                        // expSign*Integer.MAX_VALUE, is that this value
                        // is subject to further manipulation in
                        // doubleValue() and floatValue(), and I don't want
                        // it to be able to cause overflow there!
                        // (The only way we can get into trouble here is for
                        // really outrageous nDigits+nTrailZero, such as 2
                        // billion.)
                        //
                        decExp = expSign * expLimit;
                    }
                } else {
                    // this should not overflow, since we tested
                    // for expVal > (MAX+N), where N >= abs(decExp)
                    decExp = decExp + expSign * expVal;
                }

                // if we saw something not a digit ( or end of string )
                // after the [Ee][+-], without seeing any digits at all
                // this is certainly an error. If we saw some digits,
                // but then some trailing garbage, that might be ok.
                // so we just fall through in that case.
                // HUMBUG
                if (i == expAt) {
                    break parseNumber; // certainly bad
                }
            }
            //
            // We parsed everything we could.
            // If there are leftovers, then this is not good input!
            //
            if (i < length &&
                    ((i != length - 1) ||
                            (charSequence.charAt(i) != 'f' &&
                                    charSequence.charAt(i) != 'F' &&
                                    charSequence.charAt(i) != 'd' &&
                                    charSequence.charAt(i) != 'D'))) {
                break parseNumber; // go throw exception
            }
            if (isZero) {
                return isNegative ? -0.0d : 0.0d;
            }
            return parseWith(isNegative, decExp, digits, nDigits);
        } catch (StringIndexOutOfBoundsException e) {
        }
        throw new NumberFormatException("For input string: \"" + charSequence + "\"");
    }

    private static double parseWith(boolean isNegative, int decExponent, CharBuffer digits, int nDigits) {
        int kDigits = Math.min(nDigits, MAX_DECIMAL_DIGITS + 1);
        //
        // convert the lead kDigits to a long integer.
        //
        // (special performance hack: start to do it using int)
        int iValue = (int) digits.get(0) - (int) '0';
        int iDigits = Math.min(kDigits, INT_DECIMAL_DIGITS);
        for (int i = 1; i < iDigits; i++) {
            iValue = iValue * 10 + (int) digits.get(i) - (int) '0';
        }
        long lValue = iValue;
        for (int i = iDigits; i < kDigits; i++) {
            lValue = lValue * 10L + (long) ((int) digits.get(i) - (int) '0');
        }
        double dValue = (double) lValue;
        int exp = decExponent - kDigits;
        //
        // lValue now contains a long integer with the value of
        // the first kDigits digits of the number.
        // dValue contains the (double) of the same.
        //

        if (nDigits <= MAX_DECIMAL_DIGITS) {
            //
            // possibly an easy case.
            // We know that the digits can be represented
            // exactly. And if the exponent isn't too outrageous,
            // the whole thing can be done with one operation,
            // thus one rounding error.
            // Note that all our constructors trim all leading and
            // trailing zeros, so simple values (including zero)
            // will always end up here
            //
            if (exp == 0 || dValue == 0.0) {
                return (isNegative) ? -dValue : dValue; // small floating integer
            } else if (exp >= 0) {
                if (exp <= MAX_SMALL_TEN) {
                    //
                    // Can get the answer with one operation,
                    // thus one roundoff.
                    //
                    double rValue = dValue * SMALL_10_POW[exp];
                    return (isNegative) ? -rValue : rValue;
                }
                int slop = MAX_DECIMAL_DIGITS - kDigits;
                if (exp <= MAX_SMALL_TEN + slop) {
                    //
                    // We can multiply dValue by 10^(slop)
                    // and it is still "small" and exact.
                    // Then we can multiply by 10^(exp-slop)
                    // with one rounding.
                    //
                    dValue *= SMALL_10_POW[slop];
                    double rValue = dValue * SMALL_10_POW[exp - slop];
                    return (isNegative) ? -rValue : rValue;
                }
                //
                // Else we have a hard case with a positive exp.
                //
            } else {
                if (exp >= -MAX_SMALL_TEN) {
                    //
                    // Can get the answer in one division.
                    //
                    double rValue = dValue / SMALL_10_POW[-exp];
                    return (isNegative) ? -rValue : rValue;
                }
                //
                // Else we have a hard case with a negative exp.
                //
            }
        }

        //
        // Harder cases:
        // The sum of digits plus exponent is greater than
        // what we think we can do with one error.
        //
        // Start by approximating the right answer by,
        // naively, scaling by powers of 10.
        //
        if (exp > 0) {
            if (decExponent > MAX_DECIMAL_EXPONENT + 1) {
                //
                // Lets face it. This is going to be
                // Infinity. Cut to the chase.
                //
                return (isNegative) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
            }
            if ((exp & 15) != 0) {
                dValue *= SMALL_10_POW[exp & 15];
            }
            if ((exp >>= 4) != 0) {
                int j;
                for (j = 0; exp > 1; j++, exp >>= 1) {
                    if ((exp & 1) != 0) {
                        dValue *= BIG_10_POW[j];
                    }
                }
                //
                // The reason for the weird exp > 1 condition
                // in the above loop was so that the last multiply
                // would get unrolled. We handle it here.
                // It could overflow.
                //
                double t = dValue * BIG_10_POW[j];
                if (Double.isInfinite(t)) {
                    //
                    // It did overflow.
                    // Look more closely at the result.
                    // If the exponent is just one too large,
                    // then use the maximum finite as our estimate
                    // value. Else call the result infinity
                    // and punt it.
                    // ( I presume this could happen because
                    // rounding forces the result here to be
                    // an ULP or two larger than
                    // Double.MAX_VALUE ).
                    //
                    t = dValue / 2.0;
                    t *= BIG_10_POW[j];
                    if (Double.isInfinite(t)) {
                        return (isNegative) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
                    }
                    t = Double.MAX_VALUE;
                }
                dValue = t;
            }
        } else if (exp < 0) {
            exp = -exp;
            if (decExponent < MIN_DECIMAL_EXPONENT - 1) {
                //
                // Lets face it. This is going to be
                // zero. Cut to the chase.
                //
                return (isNegative) ? -0.0 : 0.0;
            }
            if ((exp & 15) != 0) {
                dValue /= SMALL_10_POW[exp & 15];
            }
            if ((exp >>= 4) != 0) {
                int j;
                for (j = 0; exp > 1; j++, exp >>= 1) {
                    if ((exp & 1) != 0) {
                        dValue *= TINY_10_POW[j];
                    }
                }
                //
                // The reason for the weird exp > 1 condition
                // in the above loop was so that the last multiply
                // would get unrolled. We handle it here.
                // It could underflow.
                //
                double t = dValue * TINY_10_POW[j];
                if (t == 0.0) {
                    //
                    // It did underflow.
                    // Look more closely at the result.
                    // If the exponent is just one too small,
                    // then use the minimum finite as our estimate
                    // value. Else call the result 0.0
                    // and punt it.
                    // ( I presume this could happen because
                    // rounding forces the result here to be
                    // an ULP or two less than
                    // Double.MIN_VALUE ).
                    //
                    t = dValue * 2.0;
                    t *= TINY_10_POW[j];
                    if (t == 0.0) {
                        return (isNegative) ? -0.0 : 0.0;
                    }
                    t = Double.MIN_VALUE;
                }
                dValue = t;
            }
        }

        return dValue;
    }
}
