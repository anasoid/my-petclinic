package org.anasoid.petclinic.service.impl;

import java.util.Optional;
import org.anasoid.petclinic.domain.Pet;
import org.anasoid.petclinic.repository.PetRepository;
import org.anasoid.petclinic.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.anasoid.petclinic.domain.Pet}.
 */
@Service
@Transactional
public class PetServiceImpl implements PetService {

    private static final Logger log = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet save(Pet pet) {
        log.debug("Request to save Pet : {}", pet);
        return petRepository.save(pet);
    }

    @Override
    public Pet update(Pet pet) {
        log.debug("Request to update Pet : {}", pet);
        return petRepository.save(pet);
    }

    @Override
    public Optional<Pet> partialUpdate(Pet pet) {
        log.debug("Request to partially update Pet : {}", pet);

        return petRepository
            .findById(pet.getId())
            .map(existingPet -> {
                if (pet.getName() != null) {
                    existingPet.setName(pet.getName());
                }
                if (pet.getBirthDate() != null) {
                    existingPet.setBirthDate(pet.getBirthDate());
                }

                return existingPet;
            })
            .map(petRepository::save);
    }

    public Page<Pet> findAllWithEagerRelationships(Pageable pageable) {
        return petRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> findOne(Long id) {
        log.debug("Request to get Pet : {}", id);
        return petRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pet : {}", id);
        petRepository.deleteById(id);
    }
}
