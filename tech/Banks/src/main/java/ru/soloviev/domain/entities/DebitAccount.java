package ru.soloviev.domain.entities;

import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.interfaces.IAccount;
import ru.soloviev.domain.interfaces.IClock;
import ru.soloviev.domain.interfaces.IRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.UUID;

import static ru.soloviev.domain.models.VerifiedOperations.*;

/**
 * Class of account that have percent of income,
 * but can not have negative balance
 */
public class DebitAccount implements IAccount {
    private final Duration creationTime;
    private final UUID id;
    private Duration lastUpdate;
    private BigDecimal balance;

    private BigDecimal memorizedMoney;

    private BigDecimal percent;

    /**
     * Constructor for debit account
     * @param accountId id of this account
     * @param rate all information about rates
     * @param clock virtual clock
     */
    public DebitAccount(UUID accountId, IRate rate, IClock clock) {
        creationTime = clock.getVirtualDate();
        lastUpdate = clock.getVirtualDate();
        balance = BigDecimal.ZERO;
        memorizedMoney = BigDecimal.ZERO;
        id = accountId;
        percent = rate.getDebitPercent();
        clock.Attach(this);
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
     * Getter for memorized money
     * @return memorized money of account
     */
    public BigDecimal getMemorizedMoney() {
        return memorizedMoney;
    }

    /**
     * Getter for debit percent
     * @return debit percent of account
     */
    public BigDecimal getPercent() {
        return percent;
    }

    /**
     * Updater of rate
     * @param newRate new parameters for accounts
     */
    @Override
    public void ChangeRate(IRate newRate) {
        percent = newRate.getDebitPercent();
    }

    /**
     * Updater of date
     * @param clock virtual timer
     */
    @Override
    public void Update(IClock clock) {
        lastUpdate = clock.getVirtualDate();
        MemorizeMoney();

        if (clock.getVirtualDate().toDays() % 30 == 0 && (clock.getVirtualDate().compareTo(creationTime) > 0))
            GiveMemorizedMoney();
    }

    /**
     * Function of time skip
     * @param timeSpan time that wanted to skip
     * @return balance on account after skipped time
     */
    @Override
    public BigDecimal CheckFutureBalance(Duration timeSpan) {
        BigDecimal addictionPart = BigDecimal.ZERO;
        BigDecimal futureBalance = balance;
        Duration futureTime = lastUpdate;
        while (timeSpan.toDays() > 0) {
            futureTime = futureTime.plus(Duration.ofDays(1));
            timeSpan = timeSpan.minus(Duration.ofDays(1));

            addictionPart = addictionPart.add(((percent.divide(BigDecimal.valueOf(365), 5, RoundingMode.CEILING)).add(BigDecimal.ONE)).multiply(futureBalance));

            if (futureTime.toDays() % 30 == 0 && (futureTime.toDays() > creationTime.toDays())) {
                futureBalance = futureBalance.add(addictionPart);
                addictionPart = BigDecimal.ZERO;
            }
        }

        return futureBalance;
    }

    /**
     * Memorize money that were added to balance in future
     */
    public void MemorizeMoney() {
        memorizedMoney = memorizedMoney.add(((percent.divide(BigDecimal.valueOf(365))).add(BigDecimal.ONE)).multiply(balance));
    }

    /**
     * Add memorized money to balance
     */
    public void GiveMemorizedMoney() {
        balance = balance.add(memorizedMoney);
        memorizedMoney = BigDecimal.ZERO;
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
    public BigDecimal ReduceMoney(BigDecimal money, boolean verified, BigDecimal limit) throws BankException, AccountException {
        VerifiedAccountLimit(verified, money, limit);
        VerifiedBalance(balance, money);
        VerifiedMoneyCount(money);

        balance = balance.subtract(money);
        return money;
    }
}
