package ru.soloviev.domain.entities;

/**
 * Builder interface for client with necessary firstname
 */
public interface IFirstnameBuilder {
    /**
     * Builder part for firstname
     * @param firstname necessary parameter for client
     * @return next builder
     */
    ILastnameBuilder WithFirstname(String firstname);
}
