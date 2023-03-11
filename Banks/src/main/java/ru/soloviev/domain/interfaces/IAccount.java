package ru.soloviev.domain.interfaces;

import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.exceptions.RateException;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

/**
 * Interface for different types of account with getters for id and balance,
 * with time skip function, updaters of time and rate, functions of add and reduce money
 */
public interface IAccount {
    /**
     * Getter for id
     * @return id of account
     */
    UUID getId();

    /**
     * Getter for balance
     * @return balance of account
     */
    BigDecimal getBalance();

    /**
     * Function of time skip
     * @param timeSpan time that wanted to skip
     * @return balance on account after skipped time
     */
    BigDecimal CheckFutureBalance(Duration timeSpan) throws RateException;

    /**
     * Updater of date
     * @param clock virtual timer
     */
    void Update(IClock clock) throws RateException;

    /**
     * Updater of rate
     * @param newRate new parameters for accounts
     */
    void ChangeRate(IRate newRate);

    /**
     * Add money to account
     * @param money sum that we want to add, positive
     * @return how much money was added (for transfer history)
     * @throws AccountException if verified of operation was failed
     */
    BigDecimal AddMoney(BigDecimal money) throws AccountException;

    /**
     * Reduce money to account
     * @param money sum that we want to subtract, positive
     * @param verified flag, if true - account has not limit
     * @param limit maximal sum that can be subtracted
     * @return how much money was subtracted (for transfer history)
     * @throws BankException if verified of operation was failed with bank information
     * @throws AccountException if verified of operation was failed with balance information
     */
    BigDecimal ReduceMoney(BigDecimal money, boolean verified, BigDecimal limit) throws BankException, AccountException;
}
