package ru.soloviev.domain.entities;

import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.interfaces.IAccount;
import ru.soloviev.domain.interfaces.IClock;
import ru.soloviev.domain.interfaces.IRate;
import ru.soloviev.domain.models.DepositInformation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.UUID;

import static ru.soloviev.domain.models.VerifiedOperations.*;

/**
 * Class of account that have percent of income that depends on balance,
 * also it have time during which client can not withdraw money,
 * it can not have negative balance
 */
public class DepositAccount implements IAccount {

    private final Duration creationTime;
    private final UUID id;
    private Duration lastUpdate;
    private boolean canReduce = false;
    private BigDecimal balance;

    private BigDecimal memorizedMoney;

    private DepositInformation depositInformation;

    /**
     * Constructor for deposit account
     * @param accountId id of this account
     * @param rate all information about rates
     * @param clock virtual clock
     */
    public DepositAccount(UUID accountId, IRate rate, IClock clock) {
        creationTime = clock.getVirtualDate();
        lastUpdate = clock.getVirtualDate();
        balance = BigDecimal.ZERO;
        memorizedMoney = BigDecimal.ZERO;
        depositInformation = rate.getDepositPercentInformation();
        id = accountId;
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
     * Getter for deposit information
     * @return deposit information of account
     */
    public DepositInformation getDepositInformation() {
        return depositInformation;
    }

    /**
     * Updater of date
     * @param clock virtual timer
     */
    @Override
    public void Update(IClock clock) throws RateException {
        canReduce = (clock.getVirtualDate().minus(creationTime)).compareTo(DepositInformation.getSpan()) >= 0;
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
    public BigDecimal CheckFutureBalance(Duration timeSpan) throws RateException {
        BigDecimal addictionPart = BigDecimal.ZERO;
        BigDecimal futureBalance = balance;
        Duration futureTime = lastUpdate;
        while (timeSpan.toDays() > 0) {
            futureTime = futureTime.plus(Duration.ofDays(1));
            timeSpan = timeSpan.minus(Duration.ofDays(1));

            addictionPart = addictionPart.add(((DepositInformation.Percent(futureBalance).divide(BigDecimal.valueOf(365), 5, RoundingMode.CEILING)).add(BigDecimal.ONE)).multiply(futureBalance));

            if (futureTime.toDays() % 30 == 0 && (futureTime.compareTo(creationTime) > 0)) {
                futureBalance = futureBalance.add(addictionPart);
                addictionPart = BigDecimal.ZERO;
            }
        }

        return futureBalance;
    }

    /**
     * Updater of rate
     * @param newRate new parameters for accounts
     */
    @Override
    public void ChangeRate(IRate newRate) {
        depositInformation = newRate.getDepositPercentInformation();
    }

    /**
     * Memorize money that were added to balance in future
     */
    public void MemorizeMoney() throws RateException {
        memorizedMoney = memorizedMoney.add(((DepositInformation.Percent(balance).divide(BigDecimal.valueOf(365))).add(BigDecimal.ONE)).multiply(balance));
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
        VerifiedFreezeTime(canReduce);

        balance = balance.subtract(money);
        return money;
    }
}
