package org.anasoid.petclinic.service.impl;

import java.util.Optional;
import org.anasoid.petclinic.domain.Specialty;
import org.anasoid.petclinic.repository.SpecialtyRepository;
import org.anasoid.petclinic.service.SpecialtyService;
import org.anasoid.petclinic.service.dto.SpecialtyDTO;
import org.anasoid.petclinic.service.mapper.SpecialtyMapper;
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

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    @Override
    public SpecialtyDTO save(SpecialtyDTO specialtyDTO) {
        log.debug("Request to save Specialty : {}", specialtyDTO);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);
        specialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(specialty);
    }

    @Override
    public SpecialtyDTO update(SpecialtyDTO specialtyDTO) {
        log.debug("Request to update Specialty : {}", specialtyDTO);
        Specialty specialty = specialtyMapper.toEntity(specialtyDTO);
        specialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(specialty);
    }

    @Override
    public Optional<SpecialtyDTO> partialUpdate(SpecialtyDTO specialtyDTO) {
        log.debug("Request to partially update Specialty : {}", specialtyDTO);

        return specialtyRepository
            .findById(specialtyDTO.getId())
            .map(existingSpecialty -> {
                specialtyMapper.partialUpdate(existingSpecialty, specialtyDTO);

                return existingSpecialty;
            })
            .map(specialtyRepository::save)
            .map(specialtyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialtyDTO> findOne(Long id) {
        log.debug("Request to get Specialty : {}", id);
        return specialtyRepository.findById(id).map(specialtyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
