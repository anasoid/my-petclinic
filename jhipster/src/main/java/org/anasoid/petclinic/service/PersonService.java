package org.anasoid.petclinic.service;

import java.util.Optional;
import org.anasoid.petclinic.domain.Person;

/**
 * Service Interface for managing {@link org.anasoid.petclinic.domain.Person}.
 */
public interface PersonService {
    /**
     * Save a person.
     *
     * @param person the entity to save.
     * @return the persisted entity.
     */
    Person save(Person person);

    /**
     * Updates a person.
     *
     * @param person the entity to update.
     * @return the persisted entity.
     */
    Person update(Person person);

    /**
     * Partially updates a person.
     *
     * @param person the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Person> partialUpdate(Person person);

    /**
     * Get the "id" person.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Person> findOne(Long id);

    /**
     * Delete the "id" person.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
