package ru.soloviev.domain.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that imitate email - it have address and list of input mails
 */
public class Email {
    private final List<String> mails;

    private final String address;

    /**
     * Constructor for email
     * @param address address of email
     */
    public Email(String address) {
        mails = new ArrayList<>();
        this.address = address;
    }

    /**
     * Getter for address
     * @return address of email
     */
    public String getAddress() {
        return address;
    }

    /**
     * Getter for list of mails
     * @return unmodifiable list of mails
     */
    public List<String> getMails() {
        return Collections.unmodifiableList(mails);
    }

    /**
     * Add mail into list of mails
     * @param message text inside of mail
     */
    public void SendMail(String message) {
        mails.add(message);
    }
}
