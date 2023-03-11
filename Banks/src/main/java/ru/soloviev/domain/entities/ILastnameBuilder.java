package ru.soloviev.domain.entities;

/**
 * Builder interface for client with necessary lastname
 */
public interface ILastnameBuilder {
    /**
     * Builder part for lastname
     * @param lastname necessary parameter for client
     * @return next builder
     */
    IClientBuilder WithLastname(String lastname);
}
