package org.anasoid.petclinic.web.rest;

import static org.anasoid.petclinic.domain.SpecialtyAsserts.*;
import static org.anasoid.petclinic.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.anasoid.petclinic.IntegrationTest;
import org.anasoid.petclinic.domain.Specialty;
import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.repository.SpecialtyRepository;
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
 * Integration tests for the {@link SpecialtyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialtyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    private Specialty insertedSpecialty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createEntity(EntityManager em) {
        Specialty specialty = new Specialty().name(DEFAULT_NAME);
        return specialty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createUpdatedEntity(EntityManager em) {
        Specialty specialty = new Specialty().name(UPDATED_NAME);
        return specialty;
    }

    @BeforeEach
    public void initTest() {
        specialty = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSpecialty != null) {
            specialtyRepository.delete(insertedSpecialty);
            insertedSpecialty = null;
        }
    }

    @Test
    @Transactional
    void createSpecialty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Specialty
        var returnedSpecialty = om.readValue(
            restSpecialtyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialty)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Specialty.class
        );

        // Validate the Specialty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSpecialtyUpdatableFieldsEquals(returnedSpecialty, getPersistedSpecialty(returnedSpecialty));

        insertedSpecialty = returnedSpecialty;
    }

    @Test
    @Transactional
    void createSpecialtyWithExistingId() throws Exception {
        // Create the Specialty with an existing ID
        specialty.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialty)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialty.setName(null);

        // Create the Specialty, which fails.

        restSpecialtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialty)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialties() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSpecialty() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL_ID, specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getSpecialtiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        Long id = specialty.getId();

        defaultSpecialtyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSpecialtyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSpecialtyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSpecialtiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name equals to
        defaultSpecialtyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpecialtiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name in
        defaultSpecialtyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpecialtiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name is not null
        defaultSpecialtyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSpecialtiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name contains
        defaultSpecialtyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSpecialtiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList where name does not contain
        defaultSpecialtyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSpecialtiesByVetIsEqualToSomething() throws Exception {
        Vet vet;
        if (TestUtil.findAll(em, Vet.class).isEmpty()) {
            specialtyRepository.saveAndFlush(specialty);
            vet = VetResourceIT.createEntity(em);
        } else {
            vet = TestUtil.findAll(em, Vet.class).get(0);
        }
        em.persist(vet);
        em.flush();
        specialty.addVet(vet);
        specialtyRepository.saveAndFlush(specialty);
        Long vetId = vet.getId();
        // Get all the specialtyList where vet equals to vetId
        defaultSpecialtyShouldBeFound("vetId.equals=" + vetId);

        // Get all the specialtyList where vet equals to (vetId + 1)
        defaultSpecialtyShouldNotBeFound("vetId.equals=" + (vetId + 1));
    }

    private void defaultSpecialtyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSpecialtyShouldBeFound(shouldBeFound);
        defaultSpecialtyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSpecialtyShouldBeFound(String filter) throws Exception {
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSpecialtyShouldNotBeFound(String filter) throws Exception {
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialty() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialty
        Specialty updatedSpecialty = specialtyRepository.findById(specialty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialty are not directly saved in db
        em.detach(updatedSpecialty);
        updatedSpecialty.name(UPDATED_NAME);

        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpecialty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSpecialty))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpecialtyToMatchAllProperties(updatedSpecialty);
    }

    @Test
    @Transactional
    void putNonExistingSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialty.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialtyWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialty using partial update
        Specialty partialUpdatedSpecialty = new Specialty();
        partialUpdatedSpecialty.setId(specialty.getId());

        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialty))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialtyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpecialty, specialty),
            getPersistedSpecialty(specialty)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpecialtyWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialty using partial update
        Specialty partialUpdatedSpecialty = new Specialty();
        partialUpdatedSpecialty.setId(specialty.getId());

        partialUpdatedSpecialty.name(UPDATED_NAME);

        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialty))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialtyUpdatableFieldsEquals(partialUpdatedSpecialty, getPersistedSpecialty(partialUpdatedSpecialty));
    }

    @Test
    @Transactional
    void patchNonExistingSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(specialty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialty() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the specialty
        restSpecialtyMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return specialtyRepository.count();
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

    protected Specialty getPersistedSpecialty(Specialty specialty) {
        return specialtyRepository.findById(specialty.getId()).orElseThrow();
    }

    protected void assertPersistedSpecialtyToMatchAllProperties(Specialty expectedSpecialty) {
        assertSpecialtyAllPropertiesEquals(expectedSpecialty, getPersistedSpecialty(expectedSpecialty));
    }

    protected void assertPersistedSpecialtyToMatchUpdatableProperties(Specialty expectedSpecialty) {
        assertSpecialtyAllUpdatablePropertiesEquals(expectedSpecialty, getPersistedSpecialty(expectedSpecialty));
    }
}
