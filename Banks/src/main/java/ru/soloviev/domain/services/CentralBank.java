package ru.soloviev.domain.services;

import ru.soloviev.domain.entities.Bank;
import ru.soloviev.domain.entities.Clock;
import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.interfaces.*;
import ru.soloviev.domain.models.DepositInformation;
import ru.soloviev.domain.models.Transfer;
import ru.soloviev.domain.models.TransferPart;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;


/**
 * Class of central bank that used like main service
 */
public class CentralBank implements ICentralBank {
    private final List<Bank> banks;

    private final IAccountCreator creator;

    private final List<Transfer> transfers;

    private final IClock clock;

    /**
     * Constructor for central bank
     * @param creator object that will create new accounts
     */
    public CentralBank(IAccountCreator creator)
    {
        transfers = new ArrayList<>();
        clock = new Clock();
        this.creator = creator;
        banks = new ArrayList<>();
    }


    /** {@inheritDoc} */
    @Override
    public List<Bank> getBanks() {
        return Collections.unmodifiableList(banks);
    }

    /** {@inheritDoc} */
    @Override
    public List<Transfer> getTransfers() {
        return Collections.unmodifiableList(transfers);
    }

    /** {@inheritDoc} */
    @Override
    public IClock getClock() {
        return clock;
    }

    /** {@inheritDoc} */
    @Override
    public UUID AddBank(IRate rate)
    {
        var newGuid = UUID.randomUUID();
        banks.add(new Bank(newGuid, rate));
        return newGuid;
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal CheckFutureBalance(Duration timeSpan, UUID bankId, UUID accountId) throws RateException
    {
        return GetBank(bankId).GetAccount(accountId).CheckFutureBalance(timeSpan);
    }

    /** {@inheritDoc} */
    @Override
    public UUID AddClient(UUID bankId, IClient client) throws BankException
    {
        return GetBank(bankId).RegisterClient(client);
    }

    /** {@inheritDoc} */
    @Override
    public UUID AddAccount(UUID bankId, UUID clientId, String type) throws AccountException
    {
        return GetBank(bankId).AddAccount(clientId, type, clock, creator);
    }

    /** {@inheritDoc} */
    @Override
    public void ChangeDebitPercent(UUID bankId, BigDecimal percent) throws RateException {
        GetBank(bankId).ChangeDebitPercent(percent);
    }

    /** {@inheritDoc} */
    @Override
    public void ChangeCommission(UUID bankId, BigDecimal commission) throws RateException {
        GetBank(bankId).ChangeCommission(commission);
    }

    /** {@inheritDoc} */
    @Override
    public void ChangeDepositInformation(UUID bankId, DepositInformation depositInformation)
    {
        GetBank(bankId).ChangeDepositInformation(depositInformation);
    }

    /** {@inheritDoc} */
    @Override
    public void ChangeLimit(UUID bankId, BigDecimal limit) throws RateException {
        GetBank(bankId).ChangeLimit(limit);
    }

    /** {@inheritDoc} */
    @Override
    public void DepositMoney(UUID bankId, UUID accountId, BigDecimal sum) throws AccountException
    {
        transfers.add(new Transfer(UUID.randomUUID(), new ArrayList<>(Collections.singleton(GetBank(bankId).AddMoney(accountId, sum)))));
    }

    /** {@inheritDoc} */
    @Override
    public void WithdrawMoney(UUID bankId, UUID accountId, BigDecimal sum) throws BankException, AccountException
    {
        transfers.add(new Transfer(UUID.randomUUID(), new ArrayList<>(Collections.singleton(GetBank(bankId).ReduceMoney(accountId, sum)))));
    }

    /** {@inheritDoc} */
    @Override
    public IClient GetClient(UUID id)
    {
        return banks.stream().filter(bank -> bank.getClients().stream().anyMatch(client -> client.getId() == id))
                .findFirst().orElseThrow().getClients().stream().filter(client -> client.getId() == id).findFirst().orElseThrow();
    }

    /** {@inheritDoc} */
    @Override
    public Bank GetBank(UUID bankId)
    {
        return banks.stream().filter(bank -> bank.getBankId() == bankId).findFirst().orElseThrow();
    }

    /** {@inheritDoc} */
    @Override
    public void CancelTransfer(UUID id) throws BankException, AccountException
    {
        for (TransferPart part: transfers.stream().filter(transfer -> transfer.id() == id).findFirst().orElseThrow().parts())
        {
            if (part.isInput())
            {
                GetBank(part.bankId()).AbsoluteReduceMoney(part.accountId(), part.sum());
            }
            else
            {
                GetBank(part.bankId()).AddMoney(part.accountId(), part.sum());
            }
        }

        transfers.remove(transfers.stream().filter(transfer -> transfer.id() == id).findFirst().orElseThrow());
    }

    /** {@inheritDoc} */
    @Override
    public void TransferMoney(BigDecimal sum, UUID firstBankId, UUID firstAccount, UUID secondBankId, UUID secondAccount) throws AccountException, BankException
    {
        GetBank(secondBankId).GetAccount(secondAccount);
        transfers.add(new Transfer(UUID.randomUUID(), new ArrayList<>(Arrays.asList(GetBank(firstBankId).ReduceMoney(firstAccount, sum),  GetBank(secondBankId).AddMoney(secondAccount, sum)))));
    }
}
