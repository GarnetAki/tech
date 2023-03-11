package ru.soloviev.domain.models;

import java.math.BigDecimal;

/**
 * Class that have information about one interval of deposit rate
 */
public class DepositInformationPart {
    private final BigDecimal maximalSum;
    private final BigDecimal percent;
    private final BigDecimal minimalSum;

    /**
     * Constructor for part of information about deposit rate
     * @param minimalSum start of interval, with this number
     * @param maximumSum end of interval, without this number
     * @param percent percent of interval, 1 percent per year - "0.01"
     */
    public DepositInformationPart(BigDecimal minimalSum, BigDecimal maximumSum, BigDecimal percent) {
        this.minimalSum = minimalSum;
        this.maximalSum = maximumSum;
        this.percent = percent;
    }

    /**
     * Getter for minimal sum
     * @return start of interval
     */
    public BigDecimal getMinimalSum() {
        return minimalSum;
    }

    /**
     * Getter for maximal sum
     * @return end of interval
     */
    public BigDecimal getMaximalSum() {
        return maximalSum;
    }

    /**
     * Getter for percent
     * @return percent for interval
     */
    public BigDecimal getPercent() {
        return percent;
    }
}
