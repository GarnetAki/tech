package ru.soloviev.domain.interfaces;

import ru.soloviev.domain.exceptions.AccountException;

/**
 * Interface for creating accounts using names of types
 */
public interface IAccountCreator {
    /**
     * Creator for accounts
     * @param type what type of account it will
     * @param rate parameters of new account
     * @param clock virtual clock to fix date of creation
     * @return new account
     * @throws AccountException if name of type does not exist
     */
    IAccount Create(String type, IRate rate, IClock clock) throws AccountException;
}
