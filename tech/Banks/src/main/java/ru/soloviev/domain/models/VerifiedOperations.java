package ru.soloviev.domain.models;

import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;

import java.math.BigDecimal;

/**
 * Class with methods that verified that operation can be done
 */
public class VerifiedOperations {
    /**
     * Verification if money that used in operation is positive
     * @param money amount of money that will use in operation
     * @throws AccountException if money is not positive
     */
    public static void VerifiedMoneyCount(BigDecimal money) throws AccountException {
        if (BigDecimal.ZERO.compareTo(money) >= 0) throw AccountException.CreateInvalidMoneyCount();
    }

    /**
     * Verification if time with taboo on account was finished
     * @param canReduce flag, if true - taboo on account was finished
     * @throws AccountException if time with taboo has not done yet
     */
    public static void VerifiedFreezeTime(boolean canReduce) throws AccountException {
        if (!canReduce) throw AccountException.CreateWithdrawIsNotPossible();
    }

    /**
     * Verification if amount of money in operation not bigger than limit
     * @param verified flag, if true - account has not got limit
     * @param money amount of money that account is needed
     * @param limit maximal sum of money that can be used
     * @throws BankException if amount of money bigger than limit and account is unverified
     */
    public static void VerifiedAccountLimit(boolean verified, BigDecimal money, BigDecimal limit) throws BankException {
        if (!verified && money.compareTo(limit) > 0) throw BankException.CreateOperationExceedsLimit();
    }

    /**
     * Verification if money on account is enough
     * @param balance amount of money that account has
     * @param money amount of money that account is needed
     * @throws BankException if amount of money in account is not enough
     */
    public static void VerifiedBalance(BigDecimal balance, BigDecimal money) throws BankException {
        if (money.compareTo(balance) > 0) throw BankException.CreateNotEnoughMoney();
    }
}
