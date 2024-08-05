package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.service.dto.OwnerDTO;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.Owner}.
 */
public interface OwnerService {
    /**
     * Save a owner.
     *
     * @param ownerDTO the entity to save.
     * @return the persisted entity.
     */
    OwnerDTO save(OwnerDTO ownerDTO);

    /**
     * Updates a owner.
     *
     * @param ownerDTO the entity to update.
     * @return the persisted entity.
     */
    OwnerDTO update(OwnerDTO ownerDTO);

    /**
     * Partially updates a owner.
     *
     * @param ownerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OwnerDTO> partialUpdate(OwnerDTO ownerDTO);

    /**
     * Get the "id" owner.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OwnerDTO> findOne(Long id);

    /**
     * Delete the "id" owner.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
