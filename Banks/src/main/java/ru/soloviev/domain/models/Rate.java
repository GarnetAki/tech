package ru.soloviev.domain.models;

import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.interfaces.IRate;

import java.math.BigDecimal;

/**
 * Class that have all information of rates inside bank
 */
public class Rate implements IRate {
    private BigDecimal debitPercent;
    private BigDecimal limit;
    private BigDecimal commission;
    private DepositInformation depositPercentInformation;

    /**
     * Constructor for rate
     * @param limit limit for one payment for unverified clients
     * @param debitPercent percent for debit accounts, 1 percent per year - "0.01"
     * @param commission money that will additionally pay if credit account have negative balance
     * @param depositPercentInformation all information about deposit accounts
     * @throws RateException if limit, commission or percent is negative
     */
    public Rate(BigDecimal limit, BigDecimal debitPercent, BigDecimal commission, DepositInformation depositPercentInformation) throws RateException {
        VerifiedCommission(commission);
        VerifiedPercent(debitPercent);
        VerifiedLimit(limit);

        this.limit = limit;
        this.debitPercent = debitPercent;
        this.commission = commission;
        this.depositPercentInformation = depositPercentInformation;
    }

    /**
     * Getter for limit
     * @return limit for one payment for unverified clients
     */
    @Override
    public BigDecimal getLimit() {
        return limit;
    }

    /**
     * Getter for debit percent
     * @return percent for debit accounts, 1 percent per year - "0.01"
     */
    @Override
    public BigDecimal getDebitPercent() {
        return debitPercent;
    }

    /**
     * Getter for commission
     * @return money that will additionally pay if credit account have negative balance
     */
    @Override
    public BigDecimal getCommission() {
        return commission;
    }

    /**
     * Getter for deposit information
     * @return all information about deposit accounts
     */
    @Override
    public DepositInformation getDepositPercentInformation() {
        return depositPercentInformation;
    }

    /**
     * Change commission to a new one
     * @param newCommission new commission
     * @throws RateException if new commission is negative
     */
    @Override
    public void ChangeCommission(BigDecimal newCommission) throws RateException {
        VerifiedCommission(newCommission);
        commission = newCommission;
    }

    /**
     * Change debit percent to a new one
     * @param newPercent new percent, 1 percent per year - "0.01"
     * @throws RateException if new percent is negative
     */
    @Override
    public void ChangeDebitPercent(BigDecimal newPercent) throws RateException {
        VerifiedPercent(newPercent);
        debitPercent = newPercent;
    }

    /**
     * Change deposit information to a new one
     * @param newInformation new deposit information
     */
    @Override
    public void ChangeDepositInformation(DepositInformation newInformation) {
        depositPercentInformation = newInformation;
    }

    /**
     * Change limit to a new one
     * @param newLimit new limit
     * @throws RateException if new limit is negative
     */
    @Override
    public void ChangeLimit(BigDecimal newLimit) throws RateException {
        VerifiedLimit(newLimit);
        limit = newLimit;
    }

    private static void VerifiedLimit(BigDecimal limit) throws RateException {
        if (BigDecimal.ZERO.compareTo(limit) > 0) throw RateException.CreateInvalidLimit();
    }

    private static void VerifiedCommission(BigDecimal commission) throws RateException {
        if (BigDecimal.ZERO.compareTo(commission) > 0) throw RateException.CreateInvalidCommission();
    }

    private static void VerifiedPercent(BigDecimal percent) throws RateException {
        if (BigDecimal.ZERO.compareTo(percent) > 0) throw RateException.CreateInvalidPercent();
    }
}
