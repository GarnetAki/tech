package ru.soloviev.domain.models;

import ru.soloviev.domain.exceptions.RateException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

/**
 * Contains duration of unsafety status of deposit accounts and parts of deposit rate - intervals of amount
 * of money and percents for them
 */
public class DepositInformation {
    private static List<DepositInformationPart> parts;

    private static Duration span;

    /**
     * Constructor for deposit rate
     * @param parts intervals of amount of money and percents for them, first should start by 0,
     *              last end by -1, next interval should start with number that equals to end of
     *              previous interval
     * @param span duration of unsafety status of deposit accounts
     * @throws RateException if duration is negative number or parts of deposit rate are incorrect
     */
    public DepositInformation(List<DepositInformationPart> parts, Duration span) throws RateException {
        ValidateDepositInformation(parts, span);
        DepositInformation.parts = parts;
        DepositInformation.span = span;
    }

    /**
     * Getter for duration of unsafety status of deposit accounts
     * @return duration of unsafety status of deposit accounts
     */
    public static Duration getSpan() {
        return span;
    }

    /**
     * Find percent for amount of money
     * @param balance amount of money
     * @return percent in interval which contains amount of money
     * @throws RateException if input was negative
     */
    public static BigDecimal Percent(BigDecimal balance) throws RateException {
        for (DepositInformationPart part : parts) {
            if (part.getMaximalSum().compareTo(balance) > 0 || part.getMaximalSum().compareTo(BigDecimal.valueOf(-1)) == 0)
                return part.getPercent();
        }

        throw RateException.CreateInvalidPercentIntervals();
    }

    /**
     * Show full information about deposit rate
     * @return string that contains all information about duration and deposit rate parts
     */
    public String Show() {
        StringBuilder information = new StringBuilder("[Time to withdraw option:" + span.toDays() + "]\n");
        for (DepositInformationPart part : parts) {
            information.append("[" + part.getMinimalSum() + " to " + part.getMaximalSum() + " percent is " + part.getPercent() + "]\n");
        }

        return information.toString();
    }

    private static void ValidateDepositInformation(List<DepositInformationPart> parts, Duration span) throws RateException {
        if (Duration.ZERO.compareTo(span) >= 0) throw RateException.CreateInvalidTimeInterval();

        BigDecimal tmp = BigDecimal.ZERO;
        for (DepositInformationPart part : parts) {
            if (BigDecimal.ZERO.compareTo(part.getPercent()) > 0) throw RateException.CreateInvalidPercent();

            if (tmp.compareTo(part.getMinimalSum()) != 0) throw RateException.CreateInvalidPercentIntervals();

            if (part.getMinimalSum().compareTo(part.getMaximalSum()) >= 0 && part.getMaximalSum().compareTo(BigDecimal.valueOf(-1)) != 0)
                throw RateException.CreateInvalidPercentIntervals();

            tmp = part.getMaximalSum();
        }

        if (tmp.compareTo(BigDecimal.valueOf(-1)) != 0) throw RateException.CreateInvalidPercentIntervals();
    }
}
