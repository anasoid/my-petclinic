package org.anasoid.petclinic.service;

import jakarta.persistence.criteria.JoinType;
import org.anasoid.petclinic.domain.*; // for static metamodels
import org.anasoid.petclinic.domain.Specialty;
import org.anasoid.petclinic.repository.SpecialtyRepository;
import org.anasoid.petclinic.service.criteria.SpecialtyCriteria;
import org.anasoid.petclinic.service.dto.SpecialtyDTO;
import org.anasoid.petclinic.service.mapper.SpecialtyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Specialty} entities in the database.
 * The main input is a {@link SpecialtyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SpecialtyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SpecialtyQueryService extends QueryService<Specialty> {

    private static final Logger log = LoggerFactory.getLogger(SpecialtyQueryService.class);

    private final SpecialtyRepository specialtyRepository;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyQueryService(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyMapper = specialtyMapper;
    }

    /**
     * Return a {@link Page} of {@link SpecialtyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SpecialtyDTO> findByCriteria(SpecialtyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.findAll(specification, page).map(specialtyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SpecialtyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Specialty> specification = createSpecification(criteria);
        return specialtyRepository.count(specification);
    }

    /**
     * Function to convert {@link SpecialtyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Specialty> createSpecification(SpecialtyCriteria criteria) {
        Specification<Specialty> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Specialty_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Specialty_.name));
            }
            if (criteria.getVetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVetId(), root -> root.join(Specialty_.vets, JoinType.LEFT).get(Vet_.id))
                );
            }
        }
        return specification;
    }
}
