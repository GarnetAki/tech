package ru.soloviev.domain.interfaces;

import ru.soloviev.domain.models.Address;
import ru.soloviev.domain.models.Email;
import ru.soloviev.domain.models.Passport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for client of bank that have personal information and
 * list of accounts
 */
public interface IClient {
    /**
     * Getter for list of accounts
     * @return list of accounts
     */
    List<IAccount> getAccounts();

    /**
     * Flag if client is verified and can use accounts without limit
     * @return true if client is verified, else return false
     */
    boolean isVerified();

    /**
     * Flag if client is subscribed and take mails about rate updates
     * @return true if client is subscribed, else return false
     */
    boolean isSubscribed();

    /**
     * Getter for client id
     * @return id of client
     */
    UUID getId();

    /**
     * Getter for client firstname
     * @return firstname of client
     */
    String getFirstname();

    /**
     * Getter for client lastname
     * @return lastname of client
     */
    String getLastname();

    /**
     * Getter for client email
     * @return email of client, null possible
     */
    Optional<Email> getEmail();

    /**
     * Getter for client address
     * @return address of client, null possible
     */
    Optional<Address> getAddress();

    /**
     * Getter for client passport
     * @return passport of client, null possible
     */
    Optional<Passport> getPassport();

    /**
     * Add new account to client
     * @param account that was wanted to add to client
     * @return id of this account
     */
    UUID AddAccount(IAccount account);

    /**
     * Initialize address if client have not got one, else change address to a new one
     * @param newAddress new address of client
     */
    void InitOrChangeAddress(Address newAddress);

    /**
     * Initialize passport if client have not got one, else change passport to a new one
     * @param newPassport new passport of client
     */
    void InitOrChangePassport(Passport newPassport);

    /**
     * Initialize email if client have not got one, else change email to a new one
     * @param newEmail new email of client
     */
    void InitOrChangeEmail(Email newEmail);

    /**
     * Change firstname to a new one
     * @param newFirstname new firstname of client
     */
    void ChangeFirstname(String newFirstname);

    /**
     * Change lastname to a new one
     * @param newLastname new lastname of client
     */
    void ChangeLastname(String newLastname);
}
