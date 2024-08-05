package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.service.dto.VisitDTO;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.Visit}.
 */
public interface VisitService {
    /**
     * Save a visit.
     *
     * @param visitDTO the entity to save.
     * @return the persisted entity.
     */
    VisitDTO save(VisitDTO visitDTO);

    /**
     * Updates a visit.
     *
     * @param visitDTO the entity to update.
     * @return the persisted entity.
     */
    VisitDTO update(VisitDTO visitDTO);

    /**
     * Partially updates a visit.
     *
     * @param visitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VisitDTO> partialUpdate(VisitDTO visitDTO);

    /**
     * Get the "id" visit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VisitDTO> findOne(Long id);

    /**
     * Delete the "id" visit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
