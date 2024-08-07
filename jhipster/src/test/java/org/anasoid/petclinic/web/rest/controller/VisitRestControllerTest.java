package org.anasoid.petclinic.web.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.anasoid.petclinic.domain.Owner;
import org.anasoid.petclinic.domain.Pet;
import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.domain.Visit;
import org.anasoid.petclinic.service.ClinicService;
import org.anasoid.petclinic.service.mapper.VisitMapper;
import org.anasoid.petclinic.web.api.VisitsApiController;
import org.anasoid.petclinic.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @author : anasoid
 * Date :   8/7/24
 */
@SpringBootTest
@WebAppConfiguration
class VisitRestControllerTest extends AbstractRestControllerTest {
    @Autowired
    Environment env;


    @Autowired
    private VisitsApiController visitRestController;

    @MockBean
    private ClinicService clinicService;

    @Autowired
    private VisitMapper visitMapper;

    private MockMvc mockMvc;

    private List<Visit> visits;

    @BeforeEach
    void initVisits() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(visitRestController)
            .setControllerAdvice(new ExceptionTranslator(env))
            .build();

        visits = new ArrayList<>();

        Owner owner = new Owner();
        owner.setId(1l);
        owner.setFirstName("Eduardo");
        owner.setLastName("Rodriquez");
        owner.setAddress("2693 Commerce St.");
        owner.setCity("McFarland");
        owner.setTelephone("6085558763");

        PetType petType = new PetType();
        petType.setId(2l);
        petType.setName("dog");

        Pet pet = new Pet();
        pet.setId(8l);
        pet.setName("Rosy");
        pet.setBirthDate(LocalDate.now());
        pet.setOwner(owner);
        pet.setType(petType);


        Visit visit = new Visit();
        visit.setId(2l);
        visit.setPet(pet);
        visit.setDate(LocalDate.now());
        visit.setDescription("rabies shot");
        visits.add(visit);

        visit = new Visit();
        visit.setId(3l);
        visit.setPet(pet);
        visit.setDate(LocalDate.now());
        visit.setDescription("neutered");
        visits.add(visit);


    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetVisitSuccess() throws Exception {
        given(this.clinicService.findVisitById(2)).willReturn(visits.get(0));
        this.mockMvc.perform(get(getBasePath("/visits/2"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.description").value("rabies shot"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetVisitNotFound() throws Exception {
        given(this.clinicService.findVisitById(999)).willReturn(null);
        this.mockMvc.perform(get(getBasePath("/visits/999"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllVisitsSuccess() throws Exception {
        given(this.clinicService.findAllVisits()).willReturn(visits);
        this.mockMvc.perform(get(getBasePath("/visits/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].description").value("rabies shot"))
            .andExpect(jsonPath("$.[1].id").value(3))
            .andExpect(jsonPath("$.[1].description").value("neutered"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllVisitsNotFound() throws Exception {
        visits.clear();
        given(this.clinicService.findAllVisits()).willReturn(visits);
        this.mockMvc.perform(get(getBasePath("/visits/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateVisitSuccess() throws Exception {
        Visit newVisit = visits.get(0);
        newVisit.setId(999l);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
        System.out.println("newVisitAsJSON " + newVisitAsJSON);
        this.mockMvc.perform(post(getBasePath("/visits/"))
                .content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateVisitError() throws Exception {
        Visit newVisit = visits.get(0);
        newVisit.setId(null);
        newVisit.setDescription(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
        this.mockMvc.perform(post(getBasePath("/visits/"))
                .content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateVisitSuccess() throws Exception {
        given(this.clinicService.findVisitById(2)).willReturn(visits.get(0));
        Visit newVisit = visits.get(0);
        newVisit.setDescription("rabies shot test");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
        this.mockMvc.perform(put(getBasePath("/visits/2"))
                .content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get(getBasePath("/visits/2"))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.description").value("rabies shot test"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateVisitError() throws Exception {
        Visit newVisit = visits.get(0);
        newVisit.setDescription(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
        this.mockMvc.perform(put(getBasePath("/visits/2"))
                .content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeleteVisitSuccess() throws Exception {
        Visit newVisit = visits.get(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
        given(this.clinicService.findVisitById(2)).willReturn(visits.get(0));
        this.mockMvc.perform(delete(getBasePath("/visits/2"))
                .content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeleteVisitError() throws Exception {
        Visit newVisit = visits.get(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisitDto(newVisit));
        given(this.clinicService.findVisitById(999)).willReturn(null);
        this.mockMvc.perform(delete(getBasePath("/visits/999"))
                .content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }
}
