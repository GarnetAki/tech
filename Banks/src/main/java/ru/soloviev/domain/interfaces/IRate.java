package ru.soloviev.domain.interfaces;

import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.models.DepositInformation;

import java.math.BigDecimal;

/**
 * Interface that have all information of rates inside bank
 */
public interface IRate {

    /**
     * Getter for limit
     * @return limit for one payment for unverified clients
     */
    BigDecimal getLimit();

    /**
     * Getter for debit percent
     * @return percent for debit accounts, 1 percent per year - "0.01"
     */
    BigDecimal getDebitPercent();

    /**
     * Getter for commission
     * @return money that will additionally pay if credit account have negative balance
     */
    BigDecimal getCommission();

    /**
     * Getter for deposit information
     * @return all information about deposit accounts
     */
    DepositInformation getDepositPercentInformation();

    /**
     * Change deposit information to a new one
     * @param newInformation new deposit information
     */
    void ChangeDepositInformation(DepositInformation newInformation);

    /**
     * Change debit percent to a new one
     * @param newPercent new percent, 1 percent per year - "0.01"
     * @throws RateException if new percent is negative
     */
    void ChangeDebitPercent(BigDecimal newPercent) throws RateException;

    /**
     * Change commission to a new one
     * @param newCommission new commission
     * @throws RateException if new commission is negative
     */
    void ChangeCommission(BigDecimal newCommission) throws RateException;

    /**
     * Change limit to a new one
     * @param newLimit new limit
     * @throws RateException if new limit is negative
     */
    void ChangeLimit(BigDecimal newLimit) throws RateException;
}
