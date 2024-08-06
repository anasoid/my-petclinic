package org.anasoid.petclinic.service.impl;

import java.util.Optional;
import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.repository.VetRepository;
import org.anasoid.petclinic.service.VetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.anasoid.petclinic.domain.Vet}.
 */
@Service
@Transactional
public class VetServiceImpl implements VetService {

    private static final Logger log = LoggerFactory.getLogger(VetServiceImpl.class);

    private final VetRepository vetRepository;

    public VetServiceImpl(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    @Override
    public Vet save(Vet vet) {
        log.debug("Request to save Vet : {}", vet);
        return vetRepository.save(vet);
    }

    @Override
    public Vet update(Vet vet) {
        log.debug("Request to update Vet : {}", vet);
        return vetRepository.save(vet);
    }

    @Override
    public Optional<Vet> partialUpdate(Vet vet) {
        log.debug("Request to partially update Vet : {}", vet);

        return vetRepository
            .findById(vet.getId())
            .map(existingVet -> {
                if (vet.getFirstName() != null) {
                    existingVet.setFirstName(vet.getFirstName());
                }
                if (vet.getLastName() != null) {
                    existingVet.setLastName(vet.getLastName());
                }

                return existingVet;
            })
            .map(vetRepository::save);
    }

    public Page<Vet> findAllWithEagerRelationships(Pageable pageable) {
        return vetRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vet> findOne(Long id) {
        log.debug("Request to get Vet : {}", id);
        return vetRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vet : {}", id);
        vetRepository.deleteById(id);
    }
}
