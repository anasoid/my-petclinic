package org.anasoid.petclinic.service;

import jakarta.persistence.criteria.JoinType;
import org.anasoid.petclinic.domain.*; // for static metamodels
import org.anasoid.petclinic.domain.Visit;
import org.anasoid.petclinic.repository.VisitRepository;
import org.anasoid.petclinic.service.criteria.VisitCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Visit} entities in the database.
 * The main input is a {@link VisitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Visit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VisitQueryService extends QueryService<Visit> {

    private static final Logger log = LoggerFactory.getLogger(VisitQueryService.class);

    private final VisitRepository visitRepository;

    public VisitQueryService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /**
     * Return a {@link Page} of {@link Visit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Visit> findByCriteria(VisitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Visit> specification = createSpecification(criteria);
        return visitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VisitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Visit> specification = createSpecification(criteria);
        return visitRepository.count(specification);
    }

    /**
     * Function to convert {@link VisitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Visit> createSpecification(VisitCriteria criteria) {
        Specification<Visit> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Visit_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Visit_.date));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Visit_.description));
            }
            if (criteria.getPetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPetId(), root -> root.join(Visit_.pet, JoinType.LEFT).get(Pet_.id))
                );
            }
        }
        return specification;
    }
}
