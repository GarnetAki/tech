package ru.soloviev.domain.entities;

import ru.soloviev.domain.interfaces.IAccount;
import ru.soloviev.domain.interfaces.IClient;
import ru.soloviev.domain.models.Address;
import ru.soloviev.domain.models.Email;
import ru.soloviev.domain.models.Passport;

import java.util.*;

/**
 * Class for client of bank that have personal information and
 * list of accounts
 */
public class Client implements IClient {
    private final List<IAccount> accounts;
    private final UUID id;
    private boolean isVerified;
    private boolean isSubscribed;
    private String firstname;

    private String lastname;

    private Optional<Address> address;

    private Optional<Email> email;

    private Optional<Passport> passport;

    private Client(String firstname, String lastname, Optional<Address> address, Optional<Passport> passport, Optional<Email> email) {
        accounts = new ArrayList<>();
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.passport = passport;
        this.email = email;
        id = UUID.randomUUID();
        SetVerified();
        SetSubscribed();
    }

    /**
     * Construct new builder to create a client
     * @return builder for client
     */
    public static ClientBuilder Builder() {
        return new ClientBuilder();
    }

    /**
     * Getter for list of accounts
     * @return list of accounts
     */
    @Override
    public List<IAccount> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    /**
     * Flag if client is verified and can use accounts without limit
     * @return true if client is verified, else return false
     */
    @Override
    public boolean isVerified() {
        return isVerified;
    }

    /**
     * Flag if client is subscribed and take mails about rate updates
     * @return true if client is subscribed, else return false
     */
    @Override
    public boolean isSubscribed() {
        return isSubscribed;
    }

    /**
     * Getter for client id
     * @return id of client
     */
    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Getter for client firstname
     * @return firstname of client
     */
    @Override
    public String getFirstname() {
        return firstname;
    }

    /**
     * Getter for client lastname
     * @return lastname of client
     */
    @Override
    public String getLastname() {
        return lastname;
    }

    /**
     * Getter for client email
     * @return email of client, null possible
     */
    @Override
    public Optional<Address> getAddress() {
        return address;
    }

    /**
     * Getter for client address
     * @return address of client, null possible
     */
    @Override
    public Optional<Email> getEmail() {
        return email;
    }

    /**
     * Getter for client passport
     * @return passport of client, null possible
     */
    @Override
    public Optional<Passport> getPassport() {
        return passport;
    }

    /**
     * Add new account to client
     * @param account that was wanted to add to client
     * @return id of this account
     */
    @Override
    public UUID AddAccount(IAccount account) {
        accounts.add(account);
        return account.getId();
    }

    /**
     * Initialize address if client have not got one, else change address to a new one
     * @param newAddress new address of client
     */
    @Override
    public void InitOrChangeAddress(Address newAddress) {
        if (newAddress == null) throw new NullPointerException();

        address = Optional.of(newAddress);
        SetVerified();
    }

    /**
     * Initialize email if client have not got one, else change email to a new one
     * @param newEmail new email of client
     */
    @Override
    public void InitOrChangeEmail(Email newEmail) {
        if (newEmail == null) throw new NullPointerException();

        email = Optional.of(newEmail);
        SetSubscribed();
    }

    /**
     * Initialize passport if client have not got one, else change passport to a new one
     * @param newPassport new passport of client
     */
    @Override
    public void InitOrChangePassport(Passport newPassport) {
        if (newPassport == null) throw new NullPointerException();

        passport = Optional.of(newPassport);
        SetVerified();
    }

    /**
     * Change firstname to a new one
     * @param newFirstname new firstname of client
     */
    public void ChangeFirstname(String newFirstname) {
        if (newFirstname == null) throw new NullPointerException();

        firstname = newFirstname;
    }

    /**
     * Change lastname to a new one
     * @param newLastname new lastname of client
     */
    public void ChangeLastname(String newLastname) {
        if (newLastname == null) throw new NullPointerException();

        lastname = newLastname;
    }

    private void SetSubscribed() {
        isSubscribed = email.isPresent();
    }

    private void SetVerified() {
        isVerified = !(address.isEmpty() || passport.isEmpty());
    }

    /**
     * Builder class with all parameters for client
     */
    public static class ClientBuilder implements IFirstnameBuilder, ILastnameBuilder, IClientBuilder {
        private String builderFirstname;
        private String builderLastname;
        private Optional<Address> builderAddress = Optional.empty();
        private Optional<Passport> builderPassport = Optional.empty();
        private Optional<Email> builderEmail = Optional.empty();

        /** {@inheritDoc} */
        @Override
        public ILastnameBuilder WithFirstname(String firstname) {
            if (firstname.isEmpty()) throw new NullPointerException();

            builderFirstname = firstname;
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public IClientBuilder WithLastname(String lastname) {
            if (lastname.isEmpty()) throw new NullPointerException();

            builderLastname = lastname;
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public IClientBuilder WithAddress(Address address) {
            builderAddress = Optional.ofNullable(address);
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public IClientBuilder WithPassport(Passport passport) {
            builderPassport = Optional.ofNullable(passport);
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public IClientBuilder WithEmail(Email email) {
            builderEmail = Optional.ofNullable(email);
            return this;
        }

        /** {@inheritDoc} */
        @Override
        public Client Build() {
            return new Client(builderFirstname, builderLastname, builderAddress, builderPassport, builderEmail);
        }
    }
}
