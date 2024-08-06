package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.domain.PetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.PetType}.
 */
public interface PetTypeService {
    /**
     * Save a petType.
     *
     * @param petType the entity to save.
     * @return the persisted entity.
     */
    PetType save(PetType petType);

    /**
     * Updates a petType.
     *
     * @param petType the entity to update.
     * @return the persisted entity.
     */
    PetType update(PetType petType);

    /**
     * Partially updates a petType.
     *
     * @param petType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PetType> partialUpdate(PetType petType);

    /**
     * Get all the petTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PetType> findAll(Pageable pageable);

    /**
     * Get the "id" petType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PetType> findOne(Long id);

    /**
     * Delete the "id" petType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
