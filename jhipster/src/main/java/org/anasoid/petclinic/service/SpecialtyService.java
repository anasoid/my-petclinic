package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.domain.Specialty;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.Specialty}.
 */
public interface SpecialtyService {
    /**
     * Save a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    Specialty save(Specialty specialty);

    /**
     * Updates a specialty.
     *
     * @param specialty the entity to update.
     * @return the persisted entity.
     */
    Specialty update(Specialty specialty);

    /**
     * Partially updates a specialty.
     *
     * @param specialty the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Specialty> partialUpdate(Specialty specialty);

    /**
     * Get the "id" specialty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Specialty> findOne(Long id);

    /**
     * Delete the "id" specialty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
