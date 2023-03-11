package ru.soloviev.domain.interfaces;

import ru.soloviev.domain.exceptions.RateException;

import java.time.Duration;

/**
 * Interface for virtual clock that contains getter for virtual time, functions
 * for add and delete accounts that subscribed on this clock, function for skipping day
 * and function for notify all subscribed accounts about new day
 */
public interface IClock {
    /**
     * Getter for virtual time
     * @return virtual time
     */
    Duration getVirtualDate();

    /**
     * Subscribe account to this clock
     * @param account that was subscribed
     */
    void Attach(IAccount account);

    /**
     * Unsubscribe account of this clock
     * @param account that was unsubscribed
     */
    void Detach(IAccount account);

    /**
     * Notify all accounts about new date
     */
    void Notify() throws RateException;

    /**
     * Skip day
     */
    void NextDay() throws RateException;
}
