package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.service.dto.SpecialtyDTO;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.Specialty}.
 */
public interface SpecialtyService {
    /**
     * Save a specialty.
     *
     * @param specialtyDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialtyDTO save(SpecialtyDTO specialtyDTO);

    /**
     * Updates a specialty.
     *
     * @param specialtyDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialtyDTO update(SpecialtyDTO specialtyDTO);

    /**
     * Partially updates a specialty.
     *
     * @param specialtyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialtyDTO> partialUpdate(SpecialtyDTO specialtyDTO);

    /**
     * Get the "id" specialty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialtyDTO> findOne(Long id);

    /**
     * Delete the "id" specialty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
