package org.anasoid.petclinic.service.impl;

import java.util.Optional;
import org.anasoid.petclinic.domain.Specialty;
import org.anasoid.petclinic.repository.SpecialtyRepository;
import org.anasoid.petclinic.service.SpecialtyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.anasoid.petclinic.domain.Specialty}.
 */
@Service
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {

    private static final Logger log = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public Specialty save(Specialty specialty) {
        log.debug("Request to save Specialty : {}", specialty);
        return specialtyRepository.save(specialty);
    }

    @Override
    public Specialty update(Specialty specialty) {
        log.debug("Request to update Specialty : {}", specialty);
        return specialtyRepository.save(specialty);
    }

    @Override
    public Optional<Specialty> partialUpdate(Specialty specialty) {
        log.debug("Request to partially update Specialty : {}", specialty);

        return specialtyRepository
            .findById(specialty.getId())
            .map(existingSpecialty -> {
                if (specialty.getName() != null) {
                    existingSpecialty.setName(specialty.getName());
                }

                return existingSpecialty;
            })
            .map(specialtyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Specialty> findOne(Long id) {
        log.debug("Request to get Specialty : {}", id);
        return specialtyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
