package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.domain.Visit;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.Visit}.
 */
public interface VisitService {
    /**
     * Save a visit.
     *
     * @param visit the entity to save.
     * @return the persisted entity.
     */
    Visit save(Visit visit);

    /**
     * Updates a visit.
     *
     * @param visit the entity to update.
     * @return the persisted entity.
     */
    Visit update(Visit visit);

    /**
     * Partially updates a visit.
     *
     * @param visit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Visit> partialUpdate(Visit visit);

    /**
     * Get the "id" visit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Visit> findOne(Long id);

    /**
     * Delete the "id" visit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
