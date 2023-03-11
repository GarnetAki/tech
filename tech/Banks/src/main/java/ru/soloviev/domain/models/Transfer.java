package ru.soloviev.domain.models;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Record that contains id and list of transfer parts
 * It can give unmodifiable list of transfer parts
 * @param id id of this transfer
 * @param parts list of simple parts of this transfer
 */
public record Transfer(UUID id, List<TransferPart> parts) {

    /**
     * Getter for list of transfer parts
     * @return unmodifiable list of transfer parts
     */
    @Override
    public List<TransferPart> parts() {
        return Collections.unmodifiableList(parts);
    }
}
