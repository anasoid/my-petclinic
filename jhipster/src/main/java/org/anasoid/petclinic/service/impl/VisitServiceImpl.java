package org.anasoid.petclinic.service.impl;

import java.util.Optional;
import org.anasoid.petclinic.domain.Visit;
import org.anasoid.petclinic.repository.VisitRepository;
import org.anasoid.petclinic.service.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.anasoid.petclinic.domain.Visit}.
 */
@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    private static final Logger log = LoggerFactory.getLogger(VisitServiceImpl.class);

    private final VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    public Visit save(Visit visit) {
        log.debug("Request to save Visit : {}", visit);
        return visitRepository.save(visit);
    }

    @Override
    public Visit update(Visit visit) {
        log.debug("Request to update Visit : {}", visit);
        return visitRepository.save(visit);
    }

    @Override
    public Optional<Visit> partialUpdate(Visit visit) {
        log.debug("Request to partially update Visit : {}", visit);

        return visitRepository
            .findById(visit.getId())
            .map(existingVisit -> {
                if (visit.getDate() != null) {
                    existingVisit.setDate(visit.getDate());
                }
                if (visit.getDescription() != null) {
                    existingVisit.setDescription(visit.getDescription());
                }

                return existingVisit;
            })
            .map(visitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Visit> findOne(Long id) {
        log.debug("Request to get Visit : {}", id);
        return visitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Visit : {}", id);
        visitRepository.deleteById(id);
    }
}
