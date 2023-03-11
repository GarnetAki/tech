package ru.soloviev.bankServiceTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.soloviev.domain.entities.Client;
import ru.soloviev.domain.exceptions.AccountException;
import ru.soloviev.domain.exceptions.BankException;
import ru.soloviev.domain.exceptions.ClientException;
import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.models.*;
import ru.soloviev.domain.services.AccountCreator;
import ru.soloviev.domain.services.CentralBank;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankTest {
    CentralBank centralBank;

    Rate rate;

    @BeforeEach
    void setUp() throws RateException {
        List<DepositInformationPart> depPartList = new ArrayList<>();
        depPartList.add(new DepositInformationPart(BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE));
        depPartList.add(new DepositInformationPart(BigDecimal.TEN, BigDecimal.valueOf(-1), BigDecimal.ONE));
        DepositInformation depInfo = new DepositInformation(depPartList, Duration.ofDays(5));
        rate = new Rate(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, depInfo);
        centralBank = new CentralBank(new AccountCreator());
    }

    @Test
    @DisplayName("Create bank and change information")
    void testBankParameters() throws RateException {
        UUID id = centralBank.AddBank(rate);
        assertEquals(BigDecimal.TEN, centralBank.GetBank(id).getRate().getLimit());
        assertEquals(BigDecimal.TEN, centralBank.GetBank(id).getRate().getCommission());
        assertEquals(BigDecimal.TEN, centralBank.GetBank(id).getRate().getDebitPercent());
        centralBank.ChangeCommission(id, BigDecimal.ONE);
        centralBank.ChangeDebitPercent(id, BigDecimal.ONE);
        centralBank.ChangeLimit(id, BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, centralBank.GetBank(id).getRate().getLimit());
        assertEquals(BigDecimal.ONE, centralBank.GetBank(id).getRate().getCommission());
        assertEquals(BigDecimal.ONE, centralBank.GetBank(id).getRate().getDebitPercent());
    }

    @Test
    @DisplayName("Create client and add email, passport, address")
    void testClientParameters() throws BankException, ClientException {
        UUID id = centralBank.AddBank(rate);
        Client.ClientBuilder newClient = Client.Builder();
        newClient.WithFirstname("Abob").WithLastname("Abobovich");
        Client client = newClient.Build();
        UUID clientId = centralBank.AddClient(id, client);
        Client.ClientBuilder newClient2 = Client.Builder();
        newClient2.WithFirstname("Aboba").WithLastname("Abobovicha").WithEmail(new Email("qwesad"));
        Client client2 = newClient2.Build();
        UUID clientId2 = centralBank.AddClient(id, client2);
        assertFalse(centralBank.GetClient(clientId).isVerified());
        assertTrue(centralBank.GetClient(clientId2).isSubscribed());
        centralBank.GetClient(clientId).InitOrChangeAddress(new Address("Asd Asd 12 12"));
        centralBank.GetClient(clientId).InitOrChangeEmail(new Email("Asdasd"));
        centralBank.GetClient(clientId).InitOrChangePassport(new Passport("1111", "111111"));
        assertTrue(centralBank.GetClient(clientId).isVerified());
    }

    @Test
    @DisplayName("Create account and test functions")
    void testAccountFunctions() throws BankException, AccountException, RateException {
        UUID id = centralBank.AddBank(rate);
        Client.ClientBuilder newClient = Client.Builder();
        newClient.WithFirstname("Abob").WithLastname("Abobovich");
        Client client = newClient.Build();
        UUID clientId = centralBank.AddClient(id, client);
        UUID accountId = centralBank.AddAccount(id, clientId, "Debit");
        UUID accountId2 = centralBank.AddAccount(id, clientId, "Deposit");
        UUID accountId3 = centralBank.AddAccount(id, clientId, "Credit");
        centralBank.DepositMoney(id, accountId, BigDecimal.TEN);
        centralBank.DepositMoney(id, accountId2, BigDecimal.TEN);
        centralBank.DepositMoney(id, accountId3, BigDecimal.TEN);
        centralBank.WithdrawMoney(id, accountId, BigDecimal.ONE);
        assertEquals(BigDecimal.TEN, centralBank.GetClient(clientId).getAccounts().get(1).getBalance());
        assertEquals(BigDecimal.valueOf(9), centralBank.GetClient(clientId).getAccounts().get(0).getBalance());
        assertTrue(BigDecimal.valueOf((10.0/365.0 + 1) * 9.0 * 31.0).subtract(centralBank.CheckFutureBalance(Duration.ofDays(45), id, accountId)).compareTo(BigDecimal.ONE) < 0);
        assertTrue(BigDecimal.valueOf((10.0/365.0 + 1) * 10.0 * 30.0).subtract(centralBank.CheckFutureBalance(Duration.ofDays(45), id, accountId2)).compareTo(BigDecimal.valueOf(2)) < 0);
        assertEquals(BigDecimal.valueOf(10), centralBank.CheckFutureBalance(Duration.ofDays(40), id, accountId3));
    }

    @Test
    @DisplayName("Test transfers and transfer history")
    void testTransferFunctions() throws BankException, AccountException {
        UUID id = centralBank.AddBank(rate);
        Client.ClientBuilder newClient = Client.Builder();
        newClient.WithFirstname("Abob").WithLastname("Abobovich");
        Client client = newClient.Build();
        UUID clientId = centralBank.AddClient(id, client);
        Client.ClientBuilder newClient2 = Client.Builder();
        newClient2.WithFirstname("Aboba").WithLastname("Abobovicha").WithEmail(new Email("qwesad"));
        Client client2 = newClient2.Build();
        UUID clientId2 = centralBank.AddClient(id, client2);
        UUID accountId = centralBank.AddAccount(id, clientId, "Debit");
        UUID accountId2 = centralBank.AddAccount(id, clientId2, "Debit");
        centralBank.DepositMoney(id, accountId, BigDecimal.TEN);
        centralBank.DepositMoney(id, accountId2, BigDecimal.TEN);
        centralBank.TransferMoney(BigDecimal.ONE, id, accountId, id, accountId2);
        assertEquals(BigDecimal.valueOf(9), centralBank.GetClient(clientId).getAccounts().get(0).getBalance());
        assertEquals(BigDecimal.valueOf(11), centralBank.GetClient(clientId2).getAccounts().get(0).getBalance());
        assertEquals(2, centralBank.getTransfers().get(2).parts().size());
    }
}
