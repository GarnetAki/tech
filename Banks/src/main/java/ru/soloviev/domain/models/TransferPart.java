package ru.soloviev.domain.models;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Record that contains bank and account ids, sum of operation and flag if it was deposit or withdraw
 * @param bankId id of bank that contains account
 * @param accountId account that used in operation
 * @param sum amount of money of operation
 * @param isInput true if operation was deposit, else false
 */
public record TransferPart(UUID bankId, UUID accountId, BigDecimal sum, boolean isInput) {
}