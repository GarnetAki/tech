package ru.soloviev.domain.entities;

import ru.soloviev.domain.models.Address;
import ru.soloviev.domain.models.Email;
import ru.soloviev.domain.models.Passport;

/**
 * Builder interface for client with information that is not necessary
 */
public interface IClientBuilder {
    /**
     * Builder part for address
     * @param address parameter that needed to verification
     * @return same builder
     */
    IClientBuilder WithAddress(Address address);

    /**
     * Builder part for passport
     * @param passport parameter that needed to verification
     * @return same builder
     */
    IClientBuilder WithPassport(Passport passport);

    /**
     * Builder part for email
     * @param email parameter that needed to subscribe
     * @return same builder
     */
    IClientBuilder WithEmail(Email email);

    /**
     * Finish of build
     * @return client with some parameters
     */
    Client Build();
}
