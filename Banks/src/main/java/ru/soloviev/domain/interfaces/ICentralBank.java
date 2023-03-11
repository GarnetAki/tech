package ru.soloviev.domain.interfaces;

import ru.soloviev.domain.entities.Bank;
import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.models.DepositInformation;
import ru.soloviev.domain.models.Transfer;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * Interface of central bank that used like main service
 */
public interface ICentralBank {
    /**
     * Getter for banks list
     * @return list of banks
     */
    List<Bank> getBanks();

    /**
     * Getter for virtual clock
     * @return virtual clock
     */
    IClock getClock();

    /**
     * Getter for transfers list
     * @return list of transfers
     */
    List<Transfer> getTransfers();

    /**
     * Create new bank
     * @param rate information about accounts in new bank
     * @return id of new bank
     */
    UUID AddBank(IRate rate);

    /**
     * Skip time to check future balance on account
     * @param timeSpan time duration that needed to skip
     * @param bankId id of bank that have needed account
     * @param accountId id of account time of which needed to skip
     * @return balance on account after needed time
     */
    BigDecimal CheckFutureBalance(Duration timeSpan, UUID bankId, UUID accountId) throws RateException;

    /**
     * Add new client to bank
     * @param bankId id of bank in which needed to add new client
     * @param client new client that needed to add into bank
     * @return id of new client
     * @throws BankException if client already exist in bank
     */
    UUID AddClient(UUID bankId, IClient client) throws BankException;

    /**
     * Create new account to client
     * @param bankId id of bank in which client exist
     * @param clientId id of client for whom needed to add new account
     * @param type name of type of new account
     * @return id of new account
     * @throws AccountException if client does not exist
     */
    UUID AddAccount(UUID bankId, UUID clientId, String type) throws AccountException;

    /**
     * Add money to account
     * @param bankId id of bank that have needed account
     * @param accountId id of account that needed to take money
     * @param sum amount of money that is added to account
     * @throws AccountException if bank have not got account
     */
    void DepositMoney(UUID bankId, UUID accountId, BigDecimal sum) throws AccountException;

    /**
     * Reduce money from account
     * @param bankId id of bank that have needed account
     * @param accountId id of account that needed to give money
     * @param sum amount of money that is reduced from account
     * @throws AccountException if bank have not got account
     * @throws BankException if account can not spend this amount of money
     */
    void WithdrawMoney(UUID bankId, UUID accountId, BigDecimal sum) throws BankException, AccountException;

    /**
     * Change debit percent in rate to a new one
     * @param bankId id of bank that needed to change rate
     * @param percent new debit percent, should not be negative
     * @throws RateException if new debit percent is negative
     */
    void ChangeDebitPercent(UUID bankId, BigDecimal percent) throws RateException;

    /**
     * Change commission in rate to a new one
     * @param bankId id of bank that needed to change rate
     * @param commission new commission, should not be negative
     * @throws RateException if new commission is negative
     */
    void ChangeCommission(UUID bankId, BigDecimal commission) throws RateException;

    /**
     * Change deposit information in rate to a new one
     * @param bankId id of bank that needed to change rate
     * @param depositInformation new deposit information
     */
    void ChangeDepositInformation(UUID bankId, DepositInformation depositInformation);

    /**
     * Change limit in rate to a new one
     * @param bankId id of bank that needed to change rate
     * @param limit new limit, should not be negative
     * @throws RateException if new limit is negative
     */
    void ChangeLimit(UUID bankId, BigDecimal limit) throws RateException;

    /**
     * Getter for client
     * @param id of client whom need to get
     * @return client with specified id
     */
    IClient GetClient(UUID id);

    /**
     * Getter for bank
     * @param bankId of bank which need to get
     * @return bank with specified id
     */
    Bank GetBank(UUID bankId);

    /**
     * Cancel transfer
     * @param id of transfer that needed to cancel
     */
    void CancelTransfer(UUID id) throws BankException, AccountException;

    /**
     * Transfer money from first account to second account
     * @param sum amount of money that needed to transfer
     * @param firstBankId id of bank with first account
     * @param firstAccount id of first account
     * @param secondBankId id of bank with second account
     * @param secondAccount id of second account
     * @throws AccountException if first account can not give needed sum of money
     * @throws BankException if one of accounts does not exist
     */
    void TransferMoney(BigDecimal sum, UUID firstBankId, UUID firstAccount, UUID secondBankId, UUID secondAccount) throws AccountException, BankException;
}
