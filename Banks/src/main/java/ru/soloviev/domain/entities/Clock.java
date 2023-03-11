package ru.soloviev.domain.entities;

import ru.soloviev.domain.exceptions.RateException;
import ru.soloviev.domain.interfaces.IAccount;
import ru.soloviev.domain.interfaces.IClock;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for virtual clock that contains getter for virtual time, functions
 * for add and delete accounts that subscribed on this clock, function for skipping day
 * and function for notify all subscribed accounts about new day
 */
public class Clock implements IClock {
    private final List<IAccount> observers;

    private Duration virtualDate;

    /**
     * Constructor for clock
     */
    public Clock() {
        observers = new ArrayList<>();
        virtualDate = Duration.ZERO;
    }

    /**
     * Getter for virtual time
     * @return virtual time
     */
    @Override
    public Duration getVirtualDate() {
        return virtualDate;
    }

    /**
     * Subscribe account to this clock
     * @param account that was subscribed
     */
    @Override
    public void Attach(IAccount account) {
        observers.add(account);
    }

    /**
     * Unsubscribe account of this clock
     * @param account that was unsubscribed
     */
    @Override
    public void Detach(IAccount account) {
        observers.remove(account);
    }

    /**
     * Notify all accounts about new date
     */
    @Override
    public void Notify() throws RateException {
        for (IAccount observer : observers) {
            observer.Update(this);
        }
    }

    /**
     * Skip day
     */
    @Override
    public void NextDay() throws RateException {
        virtualDate = virtualDate.plus(Duration.ofDays(1));
        Notify();
    }
}
