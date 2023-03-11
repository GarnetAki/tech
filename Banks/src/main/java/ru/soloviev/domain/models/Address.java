package ru.soloviev.domain.models;

import ru.soloviev.domain.exceptions.ClientException;

/**
 * Model of address, contain city, street, house and number of flat, their getters and validator
 */
public class Address {
    private final String city;
    private final String street;
    private final String house;

    private final String flat;

    /**
     * Constructor for address
     * @param address string in format "Word Word number number", where "Word" - word, starts with uppercase letter
     *                and contains only lowercase letter after it, "number" - is a digit number
     * @throws ClientException if format of address is incorrect
     */
    public Address(String address) throws ClientException {
        ValidateAddress(address);
        String[] substr = address.split(" ", 4);
        city = substr[0];
        street = substr[1];
        house = substr[2];
        flat = substr[3];
    }

    /**
     * Getter for city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter for street
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Getter for number of house
     * @return number of house
     */
    public String getHouse() {
        return house;
    }

    /**
     * Getter for number of flat
     * @return number of flat
     */
    public String getFlat() {
        return flat;
    }

    /**
     * Format address into string
     * @return string of address
     */
    public String ToString() {
        return city + " " + street + " " + house + " " + flat;
    }

    private static void ValidateAddress(String full) throws ClientException {
        if (full == null) throw new NullPointerException();

        String[] subs = full.split(" ");
        if (subs.length != 4) throw ClientException.CreateInvalidAddress();

        if (!(subs[0].matches("^[A-Z][a-z]*$") && subs[1].matches("^[A-Z][a-z]*$") && subs[2].matches("^[0-9]*$") && subs[3].matches("^[0-9]*$")))
            throw ClientException.CreateInvalidAddress();
    }
}
