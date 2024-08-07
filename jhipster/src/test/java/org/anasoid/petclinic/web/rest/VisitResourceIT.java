package org.anasoid.petclinic.web.rest;

import static org.anasoid.petclinic.domain.VisitAsserts.*;
import static org.anasoid.petclinic.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.anasoid.petclinic.IntegrationTest;
import org.anasoid.petclinic.domain.Pet;
import org.anasoid.petclinic.domain.Visit;
import org.anasoid.petclinic.repository.VisitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VisitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisitResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/visits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisitMockMvc;

    private Visit visit;

    private Visit insertedVisit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visit createEntity(EntityManager em) {
        Visit visit = new Visit().date(DEFAULT_DATE).description(DEFAULT_DESCRIPTION);
        return visit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visit createUpdatedEntity(EntityManager em) {
        Visit visit = new Visit().date(UPDATED_DATE).description(UPDATED_DESCRIPTION);
        return visit;
    }

    @BeforeEach
    public void initTest() {
        visit = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVisit != null) {
            visitRepository.delete(insertedVisit);
            insertedVisit = null;
        }
    }

    @Test
    @Transactional
    void createVisit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Visit
        var returnedVisit = om.readValue(
            restVisitMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Visit.class
        );

        // Validate the Visit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVisitUpdatableFieldsEquals(returnedVisit, getPersistedVisit(returnedVisit));

        insertedVisit = returnedVisit;
    }

    @Test
    @Transactional
    void createVisitWithExistingId() throws Exception {
        // Create the Visit with an existing ID
        visit.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visit)))
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        visit.setDate(null);

        // Create the Visit, which fails.

        restVisitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visit)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVisits() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList
        restVisitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visit.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getVisit() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get the visit
        restVisitMockMvc
            .perform(get(ENTITY_API_URL_ID, visit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(visit.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getVisitsByIdFiltering() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        Long id = visit.getId();

        defaultVisitFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVisitFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVisitFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVisitsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where date equals to
        defaultVisitFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllVisitsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where date in
        defaultVisitFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllVisitsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where date is not null
        defaultVisitFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where date is greater than or equal to
        defaultVisitFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllVisitsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where date is less than or equal to
        defaultVisitFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllVisitsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where date is less than
        defaultVisitFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllVisitsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where date is greater than
        defaultVisitFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllVisitsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where description equals to
        defaultVisitFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVisitsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where description in
        defaultVisitFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVisitsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where description is not null
        defaultVisitFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllVisitsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where description contains
        defaultVisitFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVisitsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList where description does not contain
        defaultVisitFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllVisitsByPetIsEqualToSomething() throws Exception {
        Pet pet;
        if (TestUtil.findAll(em, Pet.class).isEmpty()) {
            visitRepository.saveAndFlush(visit);
            pet = PetResourceIT.createEntity(em);
        } else {
            pet = TestUtil.findAll(em, Pet.class).get(0);
        }
        em.persist(pet);
        em.flush();
        visit.setPet(pet);
        visitRepository.saveAndFlush(visit);
        Long petId = pet.getId();
        // Get all the visitList where pet equals to petId
        defaultVisitShouldBeFound("petId.equals=" + petId);

        // Get all the visitList where pet equals to (petId + 1)
        defaultVisitShouldNotBeFound("petId.equals=" + (petId + 1));
    }

    private void defaultVisitFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVisitShouldBeFound(shouldBeFound);
        defaultVisitShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVisitShouldBeFound(String filter) throws Exception {
        restVisitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visit.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restVisitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVisitShouldNotBeFound(String filter) throws Exception {
        restVisitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVisitMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVisit() throws Exception {
        // Get the visit
        restVisitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVisit() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the visit
        Visit updatedVisit = visitRepository.findById(visit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVisit are not directly saved in db
        em.detach(updatedVisit);
        updatedVisit.date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

        restVisitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVisit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVisit))
            )
            .andExpect(status().isOk());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVisitToMatchAllProperties(updatedVisit);
    }

    @Test
    @Transactional
    void putNonExistingVisit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitMockMvc
            .perform(put(ENTITY_API_URL_ID, visit.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visit)))
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVisit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(visit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVisit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(visit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVisitWithPatch() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the visit using partial update
        Visit partialUpdatedVisit = new Visit();
        partialUpdatedVisit.setId(visit.getId());

        partialUpdatedVisit.date(UPDATED_DATE);

        restVisitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVisit))
            )
            .andExpect(status().isOk());

        // Validate the Visit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVisitUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVisit, visit), getPersistedVisit(visit));
    }

    @Test
    @Transactional
    void fullUpdateVisitWithPatch() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the visit using partial update
        Visit partialUpdatedVisit = new Visit();
        partialUpdatedVisit.setId(visit.getId());

        partialUpdatedVisit.date(UPDATED_DATE).description(UPDATED_DESCRIPTION);

        restVisitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVisit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVisit))
            )
            .andExpect(status().isOk());

        // Validate the Visit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVisitUpdatableFieldsEquals(partialUpdatedVisit, getPersistedVisit(partialUpdatedVisit));
    }

    @Test
    @Transactional
    void patchNonExistingVisit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVisitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, visit.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(visit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVisit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(visit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVisit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        visit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVisitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(visit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Visit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVisit() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the visit
        restVisitMockMvc
            .perform(delete(ENTITY_API_URL_ID, visit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return visitRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Visit getPersistedVisit(Visit visit) {
        return visitRepository.findById(visit.getId()).orElseThrow();
    }

    protected void assertPersistedVisitToMatchAllProperties(Visit expectedVisit) {
        assertVisitAllPropertiesEquals(expectedVisit, getPersistedVisit(expectedVisit));
    }

    protected void assertPersistedVisitToMatchUpdatableProperties(Visit expectedVisit) {
        assertVisitAllUpdatablePropertiesEquals(expectedVisit, getPersistedVisit(expectedVisit));
    }
}
