package ru.soloviev.domain.entities;

import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.interfaces.*;
import ru.soloviev.domain.models.DepositInformation;
import ru.soloviev.domain.models.TransferPart;

import java.math.BigDecimal;
import java.util.*;

/**
 * Class for banks that have list of clients, id, accounts rate, and transfer functions
 */
public class Bank {

    private final List<IClient> clients;

    private final UUID bankId;

    private final IRate rate;

    /**
     * Constructor for bank
     * @param bankId id of new bank
     * @param rate information about types of account
     */
    public Bank(UUID bankId, IRate rate) {
        clients = new ArrayList<>();
        this.bankId = bankId;
        this.rate = rate;
    }

    /**
     * Getter for clients list
     * @return unmodifiable list of clients
     */
    public List<IClient> getClients() {
        return Collections.unmodifiableList(clients);
    }

    /**
     * Getter for bank id
     * @return id of this bank
     */
    public UUID getBankId() {
        return bankId;
    }

    /**
     * Getter for bank rate
     * @return information about account rates
     */
    public IRate getRate() {
        return rate;
    }

    /**
     * Add new account to client
     * @param clientId id of client for whom account was added
     * @param type type of new account
     * @param clock virtual time
     * @param accountCreator object that create account
     * @return id of new account
     * @throws AccountException if type of account is unknown by accountCreator
     */
    public UUID AddAccount(UUID clientId, String type, IClock clock, IAccountCreator accountCreator) throws AccountException {
        return clients.stream().filter(client -> client.getId() == clientId).findFirst().orElseThrow().AddAccount(accountCreator.Create(type, rate, clock));
    }

    /**
     * Getter for account
     * @param id of account that needed to get
     * @return account that have this id
     */
    public IAccount GetAccount(UUID id) {
        return clients.stream().filter(client -> client.getAccounts().stream().filter(account -> account.getId() == id).toList().size() != 0)
                .findFirst().orElseThrow().getAccounts().stream().filter(account -> account.getId() == id).findFirst().orElseThrow();
    }

    /**
     * Add money to account
     * @param accountId of account to whom money needed to add
     * @param sum of money that needed to add
     * @return part of transfer information
     * @throws AccountException if sum is negative
     */
    public TransferPart AddMoney(UUID accountId, BigDecimal sum) throws AccountException {
        BigDecimal finalSum = GetAccount(accountId).AddMoney(sum);
        return new TransferPart(bankId, accountId, finalSum, true);
    }

    /**
     * Reduce money on account
     * @param accountId of account on whom money needed to reduce
     * @param sum of money that needed to reduce
     * @return part of transfer information
     * @throws BankException if sum is more than limit and client is unverified
     * @throws AccountException if sum is negative
     */
    public TransferPart ReduceMoney(UUID accountId, BigDecimal sum) throws BankException, AccountException {
        IClient client = clients.stream().filter(cl -> cl.getAccounts().stream().filter(account -> account.getId() == accountId).toList().size() != 0)
                .findFirst().orElseThrow();
        if (!client.isVerified() && sum.compareTo(rate.getLimit()) > 0)
            throw BankException.CreateOperationExceedsLimit();

        IAccount account = GetAccount(accountId);
        if (!Objects.equals(account.getClass().getSimpleName(), "CreditAccount") && account.getBalance().compareTo(sum) < 0)
            throw BankException.CreateNotEnoughMoney();

        BigDecimal finalSum = account.ReduceMoney(sum, client.isVerified(), rate.getLimit());
        return new TransferPart(bankId, accountId, finalSum, false);
    }

    /**
     * Reduce money on account if it is because of cancel of transfer
     * @param accountId of account on whom money needed to reduce
     * @param sum of money that needed to reduce
     */
    public void AbsoluteReduceMoney(UUID accountId, BigDecimal sum) throws BankException, AccountException {
        IAccount account = GetAccount(accountId);
        account.ReduceMoney(sum, true, BigDecimal.ZERO);
    }

    /**
     * Add new client to bank
     * @param client is person whom needed to add
     * @return id of new client
     * @throws BankException if client already exists
     */
    public UUID RegisterClient(IClient client) throws BankException {
        if (clients.stream().anyMatch(cl -> cl == client)) throw BankException.CreateClientAlreadyExists();

        clients.add(client);
        return client.getId();
    }

    /**
     * Change limit in rate to a new one
     * @param newLimit new limit, should not be negative
     * @throws RateException if new limit is negative
     */
    public void ChangeLimit(BigDecimal newLimit) throws RateException {
        rate.ChangeLimit(newLimit);
    }

    /**
     * Change commission in rate to a new one
     * @param newCommission new commission, should not be negative
     * @throws RateException if new commission is negative
     */
    public void ChangeCommission(BigDecimal newCommission) throws RateException {
        BigDecimal oldCommission = rate.getCommission();
        rate.ChangeCommission(newCommission);
        for (IClient client : clients.stream().filter(client -> client.getAccounts().stream().anyMatch(account -> account.getClass().getSimpleName().equals("CreditAccount"))).toList()) {
            if (client.isSubscribed())
                client.getEmail().get().SendMail("Credit commission has been changed. Old commission: " + oldCommission + ", new commission: " + newCommission);
        }

        for (IAccount account : clients.stream().map(IClient::getAccounts).flatMap(List::stream).toList()) {
            account.ChangeRate(rate);
        }
    }

    /**
     * Change debit percent in rate to a new one
     * @param newPercent new debit percent, should not be negative
     * @throws RateException if new debit percent is negative
     */
    public void ChangeDebitPercent(BigDecimal newPercent) throws RateException {
        BigDecimal oldPercent = rate.getDebitPercent();
        rate.ChangeDebitPercent(newPercent);
        for (IClient client : clients.stream().filter(client -> client.getAccounts().stream().anyMatch(account -> account.getClass().getSimpleName().equals("DebitAccount"))).toList()) {
            if (client.isSubscribed())
                client.getEmail().get().SendMail("Debit percent has been changed. Old percent: " + oldPercent + ", new percent: " + newPercent);
        }

        for (IAccount account : clients.stream().map(IClient::getAccounts).flatMap(List::stream).toList()) {
            account.ChangeRate(rate);
        }
    }

    /**
     * Change deposit information percent in rate to a new one
     * @param newInformation new deposit information
     */
    public void ChangeDepositInformation(DepositInformation newInformation) {
        DepositInformation oldInformation = rate.getDepositPercentInformation();
        rate.ChangeDepositInformation(newInformation);
        for (IClient client : clients.stream().filter(client -> client.getAccounts().stream().anyMatch(account -> account.getClass().getSimpleName().equals("DepositAccount"))).toList()) {
            if (client.isSubscribed())
                client.getEmail().get().SendMail("Deposit information has been changed. Old percent: " + oldInformation.Show() + ", new percent: " + newInformation.Show());
        }

        for (IAccount account : clients.stream().map(IClient::getAccounts).flatMap(List::stream).toList()) {
            account.ChangeRate(rate);
        }
    }
}
