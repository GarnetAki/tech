package ru.soloviev.console;

import ru.soloviev.domain.entities.*;
import ru.soloviev.domain.interfaces.*;
import ru.soloviev.domain.models.*;
import ru.soloviev.domain.services.*;

import java.util.Scanner;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.UUID;

public class CLI implements UI {
    private static final IAccountCreator creator = new AccountCreator();

    private String inputString;

    private final CentralBank centralBank = new CentralBank(creator);

    private final Scanner in = new Scanner(System.in);

    public CLI(){
        inputString = "";
    }

    @Override
    public void Run()
    {
        while (!inputString.equals("Stop"))
        {
            inputString = in.nextLine();
            Parse();
        }
    }

    private void Parse()
    {
        switch (inputString) {
            case "Create bank" -> CreateBank();
            case "Create client" -> CreateClient();
            case "Create account" -> CreateAccount();
            case "Change debit percent" -> ChangeDebitPercent();
            case "Change commission" -> ChangeCommission();
            case "Change deposit information" -> ChangeDepositInformation();
            case "Change limit" -> ChangeLimit();
            case "Deposit money" -> DepositMoney();
            case "Withdraw money" -> WithdrawMoney();
            case "Transfer money" -> TransferMoney();
            case "Show transfer history" -> ShowTransferHistory();
            case "Cancel transfer" -> CancelTransfer();
            case "Check future balance" -> CheckFutureBalance();
            case "Show banks" -> ShowBanks();
            case "Show clients" -> ShowClients();
            case "Show client accounts" -> ShowClientAccounts();
            case "Init or change client passport" -> InitOrChangeClientPassport();
            case "Init or change client address" -> InitOrChangeClientAddress();
            case "Init or change client email" -> InitOrChangeClientEmail();
            case "Change client firstname" -> ChangeClientFirstname();
            case "Change client lastname" -> ChangeClientLastname();
            case "Next day" -> NextDay();
        }
    }

    private void CreateBank()
    {
        System.out.println("Enter commission:");
        String commission = in.nextLine();
        System.out.println("Enter deposit percent:");
        String percent = in.nextLine();
        System.out.println("Enter limit:");
        String limit = in.nextLine();
        System.out.println("Enter time of deposit account withdraw block:");
        String span = in.nextLine();
        System.out.println("Enter count of deposit parts:");
        String count = in.nextLine();
        if (commission == null || percent == null || limit == null || count == null || span == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        var list = new ArrayList<DepositInformationPart>();
        for (int i = 0; i < Integer.parseInt(count); i++)
        {
            System.out.println("Enter minimal sum of deposit part:");
            String minSum = in.nextLine();
            System.out.println("Enter maximal sum of deposit part:");
            String maxSum = in.nextLine();
            System.out.println("Enter percent of deposit part:");
            String depositPercent = in.nextLine();
            if (minSum == null || maxSum == null || depositPercent == null)
            {
                System.out.println("You entered empty string");
                return;
            }

            list.add(new DepositInformationPart(BigDecimal.valueOf(Double.parseDouble(minSum)), BigDecimal.valueOf(Double.parseDouble(maxSum)), BigDecimal.valueOf(Double.parseDouble(depositPercent))));
        }

        try
        {
            System.out.println("Bank created, id: " + centralBank.AddBank(new Rate(BigDecimal.valueOf(Double.parseDouble(limit)), BigDecimal.valueOf(Double.parseDouble(percent)), BigDecimal.valueOf(Double.parseDouble(commission)), new DepositInformation(list, Duration.parse(span)))));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void CreateClient()
    {
        System.out.println("Enter id of bank:");
        String id = in.nextLine();
        System.out.println("Enter firstname:");
        String firstname = in.nextLine();
        System.out.println("Enter lastname:");
        String lastname = in.nextLine();
        System.out.println("Enter email:");
        String email = in.nextLine();
        System.out.println("Enter address:");
        String address = in.nextLine();
        System.out.println("Enter passport series:");
        String series = in.nextLine();
        System.out.println("Enter passport number:");
        String number = in.nextLine();
        if (firstname == null || lastname == null || id == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        Client.ClientBuilder newClient = Client.Builder();
        try
        {
            newClient.WithFirstname(firstname);
            newClient.WithLastname(lastname);
            if (!(address.isEmpty()||address.isBlank()))
                newClient.WithAddress(new Address(address));

            if (!(series.isEmpty()||series.isBlank()) && !(number.isEmpty()||number.isBlank()))
                newClient.WithPassport(new Passport(series, number));

            if (!(email.isEmpty()||email.isBlank()))
                newClient.WithEmail(new Email(email));

            System.out.println("Client created, id: " + centralBank.AddClient(UUID.fromString(id), newClient.Build()));
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void CreateAccount()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter client id:");
        String clientId = in.nextLine();
        System.out.println("Enter account type:");
        String type = in.nextLine();
        if (bankId == null || clientId == null || type == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.AddAccount(UUID.fromString(bankId), UUID.fromString(clientId), type);
            System.out.println("New account was created.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ChangeDebitPercent()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter debit percent:");
        String newPercent = in.nextLine();
        if (bankId == null || newPercent == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.ChangeDebitPercent(UUID.fromString(bankId), BigDecimal.valueOf(Double.parseDouble(newPercent)));
            System.out.println("Debit percent changed.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ChangeCommission()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter commission:");
        String commission = in.nextLine();
        if (bankId == null || commission == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.ChangeCommission(UUID.fromString(bankId), BigDecimal.valueOf(Double.parseDouble(commission)));
            System.out.println("Commission changed.");
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ChangeDepositInformation()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter time of deposit account withdraw block:");
        String span = in.nextLine();
        System.out.println("Enter count of deposit parts:");
        String count = in.nextLine();
        if (bankId == null || span == null || count == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        var list = new ArrayList<DepositInformationPart>();
        for (int i = 0; i < Integer.parseInt(count); i++)
        {
            System.out.println("Enter minimal sum of deposit part:");
            String minSum = in.nextLine();
            System.out.println("Enter maximal sum of deposit part:");
            String maxSum = in.nextLine();
            System.out.println("Enter percent of deposit part:");
            String depositPercent = in.nextLine();
            if (minSum == null || maxSum == null || depositPercent == null)
            {
                System.out.println("You entered empty string");
                return;
            }

            list.add(new DepositInformationPart(BigDecimal.valueOf(Double.parseDouble(minSum)), BigDecimal.valueOf(Double.parseDouble(maxSum)), BigDecimal.valueOf(Double.parseDouble(depositPercent))));
        }

        try
        {
            centralBank.ChangeDepositInformation(UUID.fromString(bankId), new DepositInformation(list, Duration.parse(span)));
            System.out.println("Deposit information changed.");
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ChangeLimit()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter limit:");
        String limit = in.nextLine();
        if (bankId == null || limit == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.ChangeLimit(UUID.fromString(bankId), BigDecimal.valueOf(Double.parseDouble(limit)));
            System.out.println("Limit changed.");
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void DepositMoney()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter account id:");
        String accountId = in.nextLine();
        System.out.println("Enter sum:");
        String sum = in.nextLine();
        if (bankId == null || accountId == null || sum == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.DepositMoney(UUID.fromString(bankId), UUID.fromString(accountId), BigDecimal.valueOf(Double.parseDouble(sum)));
            System.out.println("Money deposited.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void WithdrawMoney()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter account id:");
        String accountId = in.nextLine();
        System.out.println("Enter sum:");
        String sum = in.nextLine();
        if (bankId == null || accountId == null || sum == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.WithdrawMoney(UUID.fromString(bankId), UUID.fromString(accountId), BigDecimal.valueOf(Double.parseDouble(sum)));
            System.out.println("Money withdrawn.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void TransferMoney()
    {
        System.out.println("Enter bank id (to withdraw):");
        String firstBankId = in.nextLine();
        System.out.println("Enter account id (to withdraw):");
        String firstAccountId = in.nextLine();
        System.out.println("Enter bank id (to deposit):");
        String secondBankId = in.nextLine();
        System.out.println("Enter account id (to deposit):");
        String secondAccountId = in.nextLine();
        System.out.println("Enter sum:");
        String sum = in.nextLine();
        if (firstBankId == null || firstAccountId == null || secondBankId == null || secondAccountId == null || sum == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.TransferMoney(BigDecimal.valueOf(Double.parseDouble(sum)), UUID.fromString(firstBankId), UUID.fromString(firstAccountId), UUID.fromString(secondBankId), UUID.fromString(secondAccountId));
            System.out.println("Money transferred.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ShowTransferHistory()
    {
        for (Transfer transfer: centralBank.getTransfers())
        {
            System.out.println(transfer.id().toString());
            for (TransferPart part: transfer.parts())
            {
                String type = part.isInput() ? "deposit" : "withdraw";

                System.out.println("Account id: " + part.accountId() + " | sum: " + part.sum() + " | " + type);
            }

            System.out.println("---");
        }
    }

    private void CancelTransfer()
    {
        System.out.println("Enter transfer id:");
        String id = in.nextLine();
        if (id == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            centralBank.CancelTransfer(UUID.fromString(id));
            System.out.println("Transfer canceled.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void CheckFutureBalance()
    {
        System.out.println("Enter bank id:");
        String bankId = in.nextLine();
        System.out.println("Enter account id:");
        String accountId = in.nextLine();
        System.out.println("Enter days count:");
        String count = in.nextLine();
        if (bankId == null || accountId == null || count == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var balance = centralBank.CheckFutureBalance(Duration.parse(count), UUID.fromString(bankId), UUID.fromString(accountId));
            System.out.println("Future balance." + balance);
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ShowBanks()
    {
        int ind = 1;
        for (Bank bank: centralBank.getBanks())
        {
            System.out.println(ind + " | bank id: " + bank.getBankId());
            ind++;
        }
    }

    private void ShowClients()
    {
        System.out.println("Enter bank id:");
        String tmp = in.nextLine();
        if (tmp == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            int ind = 1;
            for (IClient client: centralBank.GetBank(UUID.fromString(tmp)).getClients())
            {
                System.out.println(ind + " | client name: " + client.getFirstname() + " " + client.getLastname() + " | client id: " + client.getId());
                ind++;
            }
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ShowClientAccounts()
    {
        System.out.println("Enter client id:");
        String tmp = in.nextLine();
        if (tmp == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            int ind = 1;
            for (IAccount account: centralBank.GetClient(UUID.fromString(tmp)).getAccounts())
            {
                System.out.println(ind + " | account balance: " + account.getBalance() + " | account id: " + account.getId());
                ind++;
            }
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void InitOrChangeClientEmail()
    {
        System.out.println("Enter client id:");
        String tmp = in.nextLine();
        System.out.println("Enter email:");
        String email = in.nextLine();
        if (email == null || tmp == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var id = UUID.fromString(tmp);
            var clientEmail = new Email(email);
            centralBank.GetClient(id).InitOrChangeEmail(clientEmail);
            System.out.println("Email initialized or changed.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void InitOrChangeClientAddress()
    {
        System.out.println("Enter client id:");
        String tmp = in.nextLine();
        System.out.println("Enter address:");
        String address = in.nextLine();
        if (address == null || tmp == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var id = UUID.fromString(tmp);
            var clientAddress = new Address(address);
            centralBank.GetClient(id).InitOrChangeAddress(clientAddress);
            System.out.println("Address initialized or changed.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void InitOrChangeClientPassport()
    {
        System.out.println("Enter client id:");
        String tmp = in.nextLine();
        System.out.println("Enter passport series:");
        String passportSeries = in.nextLine();
        System.out.println("Enter passport number:");
        String passportNumber = in.nextLine();
        if (passportNumber == null || passportSeries == null || tmp == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var id = UUID.fromString(tmp);
            var passport = new Passport(passportSeries, passportNumber);
            centralBank.GetClient(id).InitOrChangePassport(passport);
            System.out.println("Passport initialized or changed.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ChangeClientFirstname()
    {
        System.out.println("Enter client id:");
        String tmp = in.nextLine();
        System.out.println("Enter firstname:");
        String firstname = in.nextLine();
        if (firstname == null || tmp == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var id = UUID.fromString(tmp);
            centralBank.GetClient(id).ChangeFirstname(firstname);
            System.out.println("Firstname changed.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void ChangeClientLastname()
    {
        System.out.println("Enter client id:");
        String tmp = in.nextLine();
        System.out.println("Enter lastname:");
        String lastname = in.nextLine();
        if (lastname == null || tmp == null)
        {
            System.out.println("You entered empty string");
            return;
        }

        try
        {
            var id = UUID.fromString(tmp);
            centralBank.GetClient(id).ChangeLastname(lastname);
            System.out.println("Lastname changed.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void NextDay()
    {
        try {
            centralBank.getClock().NextDay();
            System.out.println("Day increased.");
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
