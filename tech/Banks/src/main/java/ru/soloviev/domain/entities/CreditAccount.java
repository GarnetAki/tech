package ru.soloviev.domain.entities;

import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.interfaces.IAccount;
import ru.soloviev.domain.interfaces.IClock;
import ru.soloviev.domain.interfaces.IRate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

import static ru.soloviev.domain.models.VerifiedOperations.VerifiedAccountLimit;
import static ru.soloviev.domain.models.VerifiedOperations.VerifiedMoneyCount;

/**
 * Class of account that have not percent of income,
 * but can have negative balance and have commission if
 * it happened
 */
public class CreditAccount implements IAccount {
    private final UUID id;

    private BigDecimal balance;

    private BigDecimal commission;

    /**
     * Constructor for credit account
     * @param accountId id of this account
     * @param rate all information about rates
     */
    public CreditAccount(UUID accountId, IRate rate) {
        balance = BigDecimal.ZERO;
        commission = rate.getCommission();
        id = accountId;
    }

    /**
     * Getter for id
     * @return id of account
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Getter for balance
     * @return balance of account
     */
    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Getter for commission
     * @return commission of account
     */
    public BigDecimal getCommission() {
        return commission;
    }

    /**
     * Function of time skip
     * @param timeSpan time that wanted to skip
     * @return balance on account after skipped time (equal to balance)
     */
    @Override
    public BigDecimal CheckFutureBalance(Duration timeSpan) {
        return balance;
    }

    /**
     * Updater of rate
     * @param newRate new parameters for accounts
     */
    @Override
    public void ChangeRate(IRate newRate) {
        commission = newRate.getCommission();
    }

    /**
     * Updater of date (do nothing)
     * @param clock virtual timer
     */
    @Override
    public void Update(IClock clock) {
    }


    /**
     * Add money to account
     * @param money sum that we want to add, positive
     * @return how much money was added (for transfer history)
     * @throws AccountException if verified of operation was failed
     */
    @Override
    public BigDecimal AddMoney(BigDecimal money) throws AccountException {
        VerifiedMoneyCount(money);

        balance = balance.add(money);
        return money;
    }

    /**
     * Reduce money to account
     * @param money sum that we want to subtract, positive
     * @param verified flag, if true - account has not limit
     * @param limit maximal sum that can be subtracted
     * @return how much money was subtracted (for transfer history)
     * @throws BankException if verified of operation was failed with bank information
     * @throws AccountException if verified of operation was failed with balance information
     */
    @Override
    public BigDecimal ReduceMoney(BigDecimal money, boolean verified, BigDecimal limit) throws AccountException, BankException {
        VerifiedMoneyCount(money);
        VerifiedAccountLimit(verified, money, limit);

        if (BigDecimal.ZERO.compareTo(balance) > 0) money = money.add(commission);

        balance = balance.subtract(money);
        return money;
    }
}
