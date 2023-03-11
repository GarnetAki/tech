package ru.soloviev.domain.services;

import ru.soloviev.domain.entities.CreditAccount;
import ru.soloviev.domain.entities.DebitAccount;
import ru.soloviev.domain.entities.DepositAccount;
import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.interfaces.IAccount;
import ru.soloviev.domain.interfaces.IAccountCreator;
import ru.soloviev.domain.interfaces.IClock;
import ru.soloviev.domain.interfaces.IRate;

import java.util.Objects;
import java.util.UUID;

/**
 * Class for creating accounts using names of types
 * It can create "Credit", "Debit" and "Deposit" accounts
 */
public class AccountCreator implements IAccountCreator {

    /**
     * Creator for accounts
     * @param type - what type of account it will
     * @param rate - parameters of new account
     * @param clock - virtual clock to fix date of creation
     * @return new account
     * @throws AccountException - if name of type does not exist
     */
    @Override
    public IAccount Create(String type, IRate rate, IClock clock) throws AccountException {
        var newId = UUID.randomUUID();

        if (Objects.equals(type, "Credit")) {
            return new CreditAccount(newId, rate);
        }else if (Objects.equals(type, "Debit")) {
            return new DebitAccount(newId, rate, clock);
        }else if (Objects.equals(type, "Deposit")){
            return new DepositAccount(newId, rate, clock);
        }else {
            throw AccountException.CreateInvalidAccountType();
        }
    }
}
