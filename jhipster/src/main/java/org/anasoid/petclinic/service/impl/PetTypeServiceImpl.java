package org.anasoid.petclinic.service.impl;

import java.util.Optional;
import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.repository.PetTypeRepository;
import org.anasoid.petclinic.service.PetTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.anasoid.petclinic.domain.PetType}.
 */
@Service
@Transactional
public class PetTypeServiceImpl implements PetTypeService {

    private static final Logger log = LoggerFactory.getLogger(PetTypeServiceImpl.class);

    private final PetTypeRepository petTypeRepository;

    public PetTypeServiceImpl(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public PetType save(PetType petType) {
        log.debug("Request to save PetType : {}", petType);
        return petTypeRepository.save(petType);
    }

    @Override
    public PetType update(PetType petType) {
        log.debug("Request to update PetType : {}", petType);
        return petTypeRepository.save(petType);
    }

    @Override
    public Optional<PetType> partialUpdate(PetType petType) {
        log.debug("Request to partially update PetType : {}", petType);

        return petTypeRepository
            .findById(petType.getId())
            .map(existingPetType -> {
                if (petType.getName() != null) {
                    existingPetType.setName(petType.getName());
                }

                return existingPetType;
            })
            .map(petTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetType> findAll(Pageable pageable) {
        log.debug("Request to get all PetTypes");
        return petTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetType> findOne(Long id) {
        log.debug("Request to get PetType : {}", id);
        return petTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PetType : {}", id);
        petTypeRepository.deleteById(id);
    }
}
