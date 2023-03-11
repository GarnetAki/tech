package ru.soloviev.domain.exceptions;

/**
 * Class of exceptions that uses in client class
 */
public class ClientException extends Exception{
    private ClientException(String message){
        super(message);
    }

    /**
     * Exception that creates when passport is invalid
     * @return exception with message that passport is invalid
     */
    public static ClientException CreateInvalidPassport() {
        return new ClientException("Passport is invalid.");
    }

    /**
     * Exception that creates when address is invalid
     * @return exception with message that address is invalid
     */
    public static ClientException CreateInvalidAddress() {
        return new ClientException("Address is invalid.");
    }
}
