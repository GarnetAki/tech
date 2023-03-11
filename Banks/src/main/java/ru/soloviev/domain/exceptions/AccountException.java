package ru.soloviev.domain.exceptions;

/**
 * Class of exceptions that uses in account class
 */
public class AccountException extends Exception{
    private AccountException(String message){
        super(message);
    }

    /**
     * Exception that creates when account type does not exist
     * @return exception with message that account type does not exist
     */
    public static AccountException CreateInvalidAccountType(){
        return new AccountException("Account type is invalid.");
    }

    /**
     * Exception that creates when money count is negative
     * @return exception with message that money count is negative
     */
    public static AccountException CreateInvalidMoneyCount() {
        return new AccountException("Money count must be positive.");
    }

    /**
     * Exception that creates when withdraw is not possible
     * @return exception with message that withdraw is not possible
     */
    public static AccountException CreateWithdrawIsNotPossible() {
        return new AccountException("Withdraw period is not came yet.");
    }
}
