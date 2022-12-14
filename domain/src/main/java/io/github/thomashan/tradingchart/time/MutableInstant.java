package io.github.thomashan.tradingchart.time;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Objects;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoUnit.DAYS;

public class MutableInstant implements Temporal, TemporalAdjuster, Comparable<MutableInstant>, Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1;
    /**
     * The minimum supported epoch second.
     */
    private static final long MIN_SECOND = -31557014167219200L;
    /**
     * The maximum supported epoch second.
     */
    private static final long MAX_SECOND = 31556889864403199L;
    /**
     * Nanos per second.
     */
    private static final long NANOS_PER_SECOND = 1000_000_000L;
    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    /**
     * Seconds per hour.
     */
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    /**
     * Seconds per day.
     */
    private static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;

    private static final ThreadLocal<MutableInstant> FROM = ThreadLocal.withInitial(() -> new MutableInstant(0, 0));

    /**
     * Constant for the 1970-01-01T00:00:00Z epoch mutable instant.
     */
    public static final MutableInstant EPOCH = new MutableInstant(0, 0);
    /**
     * The minimum supported {@code MutableInstant}, '-1000000000-01-01T00:00Z'.
     * This could be used by an application as a "far past" mutable instant.
     * <p>
     * This is one year earlier than the minimum {@code LocalDateTime}.
     * This provides sufficient values to handle the range of {@code ZoneOffset}
     * which affect the mutable instant in addition to the local date-time.
     * The value is also chosen such that the value of the year fits in
     * an {@code int}.
     */
    public static final MutableInstant MIN = MutableInstant.ofEpochSecond(MIN_SECOND, 0);
    /**
     * The maximum supported {@code MutableInstant}, '1000000000-12-31T23:59:59.999999999Z'.
     * This could be used by an application as a "far future" mutable instant.
     * <p>
     * This is one year later than the maximum {@code LocalDateTime}.
     * This provides sufficient values to handle the range of {@code ZoneOffset}
     * which affect the mutable instant in addition to the local date-time.
     * The value is also chosen such that the value of the year fits in
     * an {@code int}.
     */
    public static final MutableInstant MAX = MutableInstant.ofEpochSecond(MAX_SECOND, 999_999_999);
    private long seconds;
    private int nanos;

    private MutableInstant(long epochSecond, int nanos) {
        this.seconds = epochSecond;
        this.nanos = nanos;
    }

    @Override
    public int compareTo(MutableInstant otherInstant) {
        int cmp = Long.compare(seconds, otherInstant.seconds);
        if (cmp != 0) {
            return cmp;
        }
        return nanos - otherInstant.nanos;
    }

    @Override
    public boolean isSupported(TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            return unit.isTimeBased() || unit == DAYS;
        }
        return unit != null && unit.isSupportedBy(this);
    }

    @Override
    public MutableInstant with(TemporalField field, long newValue) {
        if (field instanceof ChronoField chronoField) {
            chronoField.checkValidValue(newValue);
            switch (chronoField) {
                case MILLI_OF_SECOND: {
                    int nval = (int) newValue * 1000_000;
                    return (nval != nanos ? adjust(seconds, nval) : this);
                }
                case MICRO_OF_SECOND: {
                    int nval = (int) newValue * 1000;
                    return (nval != nanos ? adjust(seconds, nval) : this);
                }
                case NANO_OF_SECOND:
                    return (newValue != nanos ? adjust(seconds, (int) newValue) : this);
                case INSTANT_SECONDS:
                    return (newValue != seconds ? adjust(newValue, nanos) : this);
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.adjustInto(this, newValue);
    }

    private MutableInstant adjust(long seconds, int nanoOfSecond) {
        if ((seconds | nanoOfSecond) == 0) {
            return EPOCH;
        }
        if (seconds < MIN_SECOND || seconds > MAX_SECOND) {
            throw new DateTimeException("Mutable instant exceeds minimum or maximum mutable instant");
        }
        this.seconds = seconds;
        this.nanos = nanoOfSecond;
        return this;
    }

    @Override
    public Temporal plus(long amountToAdd, TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            switch ((ChronoUnit) unit) {
                case NANOS:
                    return plusNanos(amountToAdd);
                case MICROS:
                    return plus(amountToAdd / 1000_000, (amountToAdd % 1000_000) * 1000);
                case MILLIS:
                    return plusMillis(amountToAdd);
                case SECONDS:
                    return plusSeconds(amountToAdd);
                case MINUTES:
                    return plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_MINUTE));
                case HOURS:
                    return plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_HOUR));
                case HALF_DAYS:
                    return plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_DAY / 2));
                case DAYS:
                    return plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_DAY));
            }
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
        return unit.addTo(this, amountToAdd);
    }

    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        MutableInstant end = from(endExclusive);
        if (unit instanceof ChronoUnit chronoUnit) {
            switch (chronoUnit) {
                case NANOS:
                    return nanosUntil(end);
                case MICROS:
                    return nanosUntil(end) / 1000;
                case MILLIS:
                    return Math.subtractExact(end.toEpochMilli(), toEpochMilli());
                case SECONDS:
                    return secondsUntil(end);
                case MINUTES:
                    return secondsUntil(end) / SECONDS_PER_MINUTE;
                case HOURS:
                    return secondsUntil(end) / SECONDS_PER_HOUR;
                case HALF_DAYS:
                    return secondsUntil(end) / (12 * SECONDS_PER_HOUR);
                case DAYS:
                    return secondsUntil(end) / (SECONDS_PER_DAY);
            }
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
        return unit.between(this, end);
    }

    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return field == INSTANT_SECONDS || field == NANO_OF_SECOND || field == MICRO_OF_SECOND || field == MILLI_OF_SECOND;
        }
        return field != null && field.isSupportedBy(this);
    }

    @Override
    public long getLong(TemporalField field) {
        if (field instanceof ChronoField) {
            switch ((ChronoField) field) {
                case NANO_OF_SECOND:
                    return nanos;
                case MICRO_OF_SECOND:
                    return nanos / 1000;
                case MILLI_OF_SECOND:
                    return nanos / 1000_000;
                case INSTANT_SECONDS:
                    return seconds;
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.getFrom(this);
    }

    @Override
    public Temporal adjustInto(Temporal temporal) {
        return temporal.with(INSTANT_SECONDS, seconds).with(NANO_OF_SECOND, nanos);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof MutableInstant otherInstant)
                && this.seconds == otherInstant.seconds
                && this.nanos == otherInstant.nanos;
    }

    @Override
    public int hashCode() {
        return ((int) (seconds ^ (seconds >>> 32))) + 51 * nanos;
    }

    @Override
    public String toString() {
        return DateTimeFormatter.ISO_INSTANT.format(this);
    }

    public long getEpochSecond() {
        return seconds;
    }

    public int getNano() {
        return nanos;
    }

    public MutableInstant plusSeconds(long secondsToAdd) {
        return plus(secondsToAdd, 0);
    }

    public MutableInstant plusMillis(long millisToAdd) {
        return plus(millisToAdd / 1000, (millisToAdd % 1000) * 1000_000);
    }

    public MutableInstant plusNanos(long nanosToAdd) {
        return plus(0, nanosToAdd);
    }

    public long toEpochMilli() {
        if (seconds < 0 && nanos > 0) {
            long millis = Math.multiplyExact(seconds + 1, 1000);
            long adjustment = nanos / 1000_000 - 1000;
            return Math.addExact(millis, adjustment);
        } else {
            long millis = Math.multiplyExact(seconds, 1000);
            return Math.addExact(millis, nanos / 1000_000);
        }
    }

    private long nanosUntil(MutableInstant end) {
        long secsDiff = Math.subtractExact(end.seconds, seconds);
        long totalNanos = Math.multiplyExact(secsDiff, NANOS_PER_SECOND);
        return Math.addExact(totalNanos, end.nanos - nanos);
    }

    private long secondsUntil(MutableInstant end) {
        long secsDiff = Math.subtractExact(end.seconds, seconds);
        long nanosDiff = end.nanos - nanos;
        if (secsDiff > 0 && nanosDiff < 0) {
            secsDiff--;
        } else if (secsDiff < 0 && nanosDiff > 0) {
            secsDiff++;
        }
        return secsDiff;
    }

    /**
     * Returns this mutable instant with the specified duration added.
     * <p>
     *
     * @param secondsToAdd the seconds to add, positive or negative
     * @param nanosToAdd   the nanos to add, positive or negative
     * @return an {@code Instant} based on this instant with the specified seconds added, not null
     * @throws DateTimeException   if the result exceeds the maximum or minimum instant
     * @throws ArithmeticException if numeric overflow occurs
     */
    private MutableInstant plus(long secondsToAdd, long nanosToAdd) {
        if ((secondsToAdd | nanosToAdd) == 0) {
            return this;
        }
        long epochSec = Math.addExact(seconds, secondsToAdd);
        epochSec = Math.addExact(epochSec, nanosToAdd / NANOS_PER_SECOND);
        nanosToAdd = nanosToAdd % NANOS_PER_SECOND;
        long nanoAdjustment = nanos + nanosToAdd;  // safe int+NANOS_PER_SECOND
        return ofEpochSecondMutable(epochSec, nanoAdjustment);
    }

    /**
     * Obtains an instance of {@code MutableInstant} using seconds from the
     * epoch of 1970-01-01T00:00:00Z and nanosecond fraction of second.
     * <p>
     * This method allows an arbitrary number of nanoseconds to be passed in.
     * The factory will alter the values of the second and nanosecond in order
     * to ensure that the stored nanosecond is in the range 0 to 999,999,999.
     * For example, the following will result in exactly the same mutable instant:
     * <pre>
     *  MutableInstant.ofEpochSecond(3, 1);
     *  MutableInstant.ofEpochSecond(4, -999_999_999);
     *  MutableInstant.ofEpochSecond(2, 1000_000_001);
     * </pre>
     *
     * @param epochSecond    the number of seconds from 1970-01-01T00:00:00Z
     * @param nanoAdjustment the nanosecond adjustment to the number of seconds, positive or negative
     * @return a mutable instant, not null
     * @throws DateTimeException   if the mutable instant exceeds the maximum or minimum mutable instant
     * @throws ArithmeticException if numeric overflow occurs
     */
    private static MutableInstant ofEpochSecond(long epochSecond, long nanoAdjustment) {
        long secs = Math.addExact(epochSecond, Math.floorDiv(nanoAdjustment, NANOS_PER_SECOND));
        int nos = (int) Math.floorMod(nanoAdjustment, NANOS_PER_SECOND);
        return new MutableInstant(secs, nos);
    }

    /**
     * Obtains an instance of {@code MutableInstant} using seconds from the
     * epoch of 1970-01-01T00:00:00Z and nanosecond fraction of second.
     * <p>
     * This method allows an arbitrary number of nanoseconds to be passed in.
     * The factory will alter the values of the second and nanosecond in order
     * to ensure that the stored nanosecond is in the range 0 to 999,999,999.
     * For example, the following will result in exactly the same mutable instant:
     * <pre>
     *  MutableInstant.ofEpochSecond(3, 1);
     *  MutableInstant.ofEpochSecond(4, -999_999_999);
     *  MutableInstant.ofEpochSecond(2, 1000_000_001);
     * </pre>
     *
     * @param epochSecond    the number of seconds from 1970-01-01T00:00:00Z
     * @param nanoAdjustment the nanosecond adjustment to the number of seconds, positive or negative
     * @return a mutable instant, not null
     * @throws DateTimeException   if the mutable instant exceeds the maximum or minimum mutable instant
     * @throws ArithmeticException if numeric overflow occurs
     */
    public MutableInstant ofEpochSecondMutable(long epochSecond, long nanoAdjustment) {
        long secs = Math.addExact(epochSecond, Math.floorDiv(nanoAdjustment, NANOS_PER_SECOND));
        int nos = (int) Math.floorMod(nanoAdjustment, NANOS_PER_SECOND);
        this.seconds = secs;
        this.nanos = nos;
        return this;
    }

    public static MutableInstant from(TemporalAccessor temporal) {
        if (temporal instanceof MutableInstant) {
            return (MutableInstant) temporal;
        }
        Objects.requireNonNull(temporal, "temporal");
        try {
            long instantSecs = temporal.getLong(INSTANT_SECONDS);
            int nanoOfSecond = temporal.get(NANO_OF_SECOND);
            return FROM.get().ofEpochSecondMutable(instantSecs, nanoOfSecond);
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain Instant from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }

    public static MutableInstant parse(final CharSequence text) {
        return DateTimeFormatter.ISO_INSTANT.parse(text, MutableInstant::from);
    }
}
