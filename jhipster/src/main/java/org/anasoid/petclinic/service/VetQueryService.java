package org.anasoid.petclinic.service;

import jakarta.persistence.criteria.JoinType;
import org.anasoid.petclinic.domain.*; // for static metamodels
import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.repository.VetRepository;
import org.anasoid.petclinic.service.criteria.VetCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Vet} entities in the database.
 * The main input is a {@link VetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Vet} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VetQueryService extends QueryService<Vet> {

    private static final Logger log = LoggerFactory.getLogger(VetQueryService.class);

    private final VetRepository vetRepository;

    public VetQueryService(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    /**
     * Return a {@link Page} of {@link Vet} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Vet> findByCriteria(VetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vet> specification = createSpecification(criteria);
        return vetRepository.fetchBagRelationships(vetRepository.findAll(specification, page));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Vet> specification = createSpecification(criteria);
        return vetRepository.count(specification);
    }

    /**
     * Function to convert {@link VetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vet> createSpecification(VetCriteria criteria) {
        Specification<Vet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Vet_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Vet_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Vet_.lastName));
            }
            if (criteria.getSpecialtiesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSpecialtiesId(), root -> root.join(Vet_.specialties, JoinType.LEFT).get(Specialty_.id))
                );
            }
        }
        return specification;
    }
}
