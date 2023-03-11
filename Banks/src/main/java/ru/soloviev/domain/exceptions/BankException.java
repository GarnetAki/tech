package ru.soloviev.domain.exceptions;

/**
 * Class of exceptions that uses in bank and centralBank classes
 */
public class BankException extends Exception{
    private BankException(String message){
        super(message);
    }

    /**
     * Exception that creates when client does not exist
     * @return exception with message that client does not exist
     */
    public static BankException CreateClientDoesNotExist(){
        return new BankException("Client does not exist.");
    }

    /**
     * Exception that creates when client already exists
     * @return exception with message that client already exists
     */
    public static BankException CreateClientAlreadyExists() {
        return new BankException("Client already exists.");
    }

    /**
     * Exception that creates when money is not enough
     * @return exception with message that money is not enough
     */
    public static BankException CreateNotEnoughMoney() {
        return new BankException("Money is not enough.");
    }

    /**
     * Exception that creates when operation exceeds limit
     * @return exception with message that operation exceeds limit
     */
    public static BankException CreateOperationExceedsLimit() {
        return new BankException("Operation exceeds limit.");
    }
}
