package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.domain.Owner;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.Owner}.
 */
public interface OwnerService {
    /**
     * Save a owner.
     *
     * @param owner the entity to save.
     * @return the persisted entity.
     */
    Owner save(Owner owner);

    /**
     * Updates a owner.
     *
     * @param owner the entity to update.
     * @return the persisted entity.
     */
    Owner update(Owner owner);

    /**
     * Partially updates a owner.
     *
     * @param owner the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Owner> partialUpdate(Owner owner);

    /**
     * Get the "id" owner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Owner> findOne(Long id);

    /**
     * Delete the "id" owner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
