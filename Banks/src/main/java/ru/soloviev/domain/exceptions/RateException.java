package ru.soloviev.domain.exceptions;

/**
 * Class of exceptions that uses in rate class
 */
public class RateException extends Exception {
    private RateException(String message) {
        super(message);
    }

    /**
     * Exception that creates when limit is invalid
     * @return exception with message that limit is invalid
     */
    public static RateException CreateInvalidLimit() {
        return new RateException("Limit is invalid.");
    }

    /**
     * Exception that creates when commission is invalid
     * @return exception with message that commission is invalid
     */
    public static RateException CreateInvalidCommission() {
        return new RateException("Commission is invalid.");
    }

    /**
     * Exception that creates when percent is invalid
     * @return exception with message that percent is invalid
     */
    public static RateException CreateInvalidPercent() {
        return new RateException("Percent is invalid.");
    }

    /**
     * Exception that creates when percent intervals are invalid
     * @return exception with message that percent intervals are invalid
     */
    public static RateException CreateInvalidPercentIntervals() {
        return new RateException("Percent intervals are invalid.");
    }

    /**
     * Exception that creates when time interval is invalid
     * @return exception with message that time interval is invalid
     */
    public static RateException CreateInvalidTimeInterval() {
        return new RateException("Time interval are invalid.");
    }
}
