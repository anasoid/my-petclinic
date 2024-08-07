package org.anasoid.petclinic.web.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.anasoid.petclinic.domain.Owner;
import org.anasoid.petclinic.service.mapper.OwnerMapper;
import org.anasoid.petclinic.service.mapper.PetMapper;
import org.anasoid.petclinic.service.mapper.VisitMapper;
import org.anasoid.petclinic.service.ClinicService;
import org.anasoid.petclinic.service.api.dto.OwnerDto;
import org.anasoid.petclinic.service.api.dto.PetDto;
import org.anasoid.petclinic.service.api.dto.PetTypeDto;
import org.anasoid.petclinic.service.api.dto.VisitDto;
import org.anasoid.petclinic.web.api.OwnersApiController;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
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
 *
 */
@SpringBootTest
@WebAppConfiguration
class OwnerRestControllerTest extends AbstractRestControllerTest {
    @Autowired
    Environment env;
    @Autowired
    private OwnersApiController ownerRestController;

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private PetMapper petMapper;

    @Autowired
    private VisitMapper visitMapper;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<OwnerDto> owners;

    private List<PetDto> pets;

    private List<VisitDto> visits;

    @BeforeEach
    void initOwners() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestController)
            .setControllerAdvice(new ExceptionTranslator(env))
            .build();
        owners = new ArrayList<>();

        OwnerDto ownerWithPet = new OwnerDto();
        owners.add(ownerWithPet.id(1).firstName("George").lastName("Franklin").address("110 W. Liberty St.").city("Madison").telephone("6085551023").addPetsItem(getTestPetWithIdAndName(ownerWithPet, 1, "Rosy")));
        OwnerDto owner = new OwnerDto();
        owners.add(owner.id(2).firstName("Betty").lastName("Davis").address("638 Cardinal Ave.").city("Sun Prairie").telephone("6085551749"));
        owner = new OwnerDto();
        owners.add(owner.id(3).firstName("Eduardo").lastName("Rodriquez").address("2693 Commerce St.").city("McFarland").telephone("6085558763"));
        owner = new OwnerDto();
        owners.add(owner.id(4).firstName("Harold").lastName("Davis").address("563 Friendly St.").city("Windsor").telephone("6085553198"));

        PetTypeDto petType = new PetTypeDto();
        petType.id(2)
            .name("dog");

        pets = new ArrayList<>();
        PetDto pet = new PetDto();
        pets.add(pet.id(3)
            .name("Rosy")
            .birthDate(LocalDate.now())
            .type(petType));

        pet = new PetDto();
        pets.add(pet.id(4)
            .name("Jewel")
            .birthDate(LocalDate.now())
            .type(petType));

        visits = new ArrayList<>();
        VisitDto visit = new VisitDto();
        visit.setId(2);
        visit.setPetId(pet.getId());
        visit.setDate(LocalDate.now());
        visit.setDescription("rabies shot");
        visits.add(visit);

        visit = new VisitDto();
        visit.setId(3);
        visit.setPetId(pet.getId());
        visit.setDate(LocalDate.now());
        visit.setDescription("neutered");
        visits.add(visit);
    }

    private PetDto getTestPetWithIdAndName(final OwnerDto owner, final int id, final String name) {
        PetTypeDto petType = new PetTypeDto();
        PetDto pet = new PetDto();
        pet.id(id).name(name).birthDate(LocalDate.now()).type(petType.id(2).name("dog")).addVisitsItem(getTestVisitForPet(pet, 1));
        return pet;
    }

    private VisitDto getTestVisitForPet(final PetDto pet, final int id) {
        VisitDto visit = new VisitDto();
        return visit.id(id).date(LocalDate.now()).description("test" + id);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(1)).willReturn(ownerMapper.toOwner(owners.get(0)));
        this.mockMvc.perform(get(getBasePath("/owners/1"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("George"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerNotFound() throws Exception {
        given(this.clinicService.findOwnerById(2)).willReturn(null);
        this.mockMvc.perform(get(getBasePath("/owners/2"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnersListSuccess() throws Exception {
        owners.remove(0);
        owners.remove(1);
        given(this.clinicService.findOwnerByLastName("Davis")).willReturn(ownerMapper.toOwners(owners));
        this.mockMvc.perform(get(getBasePath("/owners?lastName=Davis"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnersListNotFound() throws Exception {
        owners.clear();
        given(this.clinicService.findOwnerByLastName("0")).willReturn(ownerMapper.toOwners(owners));
        this.mockMvc.perform(get(getBasePath("/owners/?lastName=0"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllOwnersSuccess() throws Exception {
        owners.remove(0);
        owners.remove(1);
        given(this.clinicService.findAllOwners()).willReturn(ownerMapper.toOwners(owners));
        this.mockMvc.perform(get(getBasePath("/owners/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetAllOwnersNotFound() throws Exception {
        owners.clear();
        given(this.clinicService.findAllOwners()).willReturn(ownerMapper.toOwners(owners));
        this.mockMvc.perform(get(getBasePath("/owners/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateOwnerSuccess() throws Exception {
        OwnerDto newOwnerDto = owners.get(0);
        newOwnerDto.setId(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
        this.mockMvc.perform(post(getBasePath("/owners/"))
                .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateOwnerError() throws Exception {
        OwnerDto newOwnerDto = owners.get(0);
        newOwnerDto.setId(null);
        newOwnerDto.setFirstName(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
        this.mockMvc.perform(post(getBasePath("/owners/"))
                .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(1)).willReturn(ownerMapper.toOwner(owners.get(0)));
        int ownerId = owners.get(0).getId();
        OwnerDto updatedOwnerDto = new OwnerDto();
        // body.id = ownerId which is used in url path
        updatedOwnerDto.setId(ownerId);
        updatedOwnerDto.setFirstName("GeorgeI");
        updatedOwnerDto.setLastName("Franklin");
        updatedOwnerDto.setAddress("110 W. Liberty St.");
        updatedOwnerDto.setCity("Madison");
        updatedOwnerDto.setTelephone("6085551023");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String newOwnerAsJSON = mapper.writeValueAsString(updatedOwnerDto);
        this.mockMvc.perform(put(getBasePath("/owners/" + ownerId))
                .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get(getBasePath("/owners/" + ownerId))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(ownerId))
            .andExpect(jsonPath("$.firstName").value("GeorgeI"));

    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateOwnerSuccessNoBodyId() throws Exception {
        given(this.clinicService.findOwnerById(1)).willReturn(ownerMapper.toOwner(owners.get(0)));
        int ownerId = owners.get(0).getId();
        OwnerDto updatedOwnerDto = new OwnerDto();
        updatedOwnerDto.setFirstName("GeorgeI");
        updatedOwnerDto.setLastName("Franklin");
        updatedOwnerDto.setAddress("110 W. Liberty St.");
        updatedOwnerDto.setCity("Madison");

        updatedOwnerDto.setTelephone("6085551023");
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(updatedOwnerDto);
        this.mockMvc.perform(put(getBasePath("/owners/" + ownerId))
                .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get(getBasePath("/owners/" + ownerId))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(ownerId))
            .andExpect(jsonPath("$.firstName").value("GeorgeI"));

    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testUpdateOwnerError() throws Exception {
        OwnerDto newOwnerDto = owners.get(0);
        newOwnerDto.setFirstName("");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
        this.mockMvc.perform(put(getBasePath("/owners/1"))
                .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeleteOwnerSuccess() throws Exception {
        OwnerDto newOwnerDto = owners.get(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
        final Owner owner = ownerMapper.toOwner(owners.get(0));
        given(this.clinicService.findOwnerById(1)).willReturn(owner);
        this.mockMvc.perform(delete(getBasePath("/owners/1"))
                .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testDeleteOwnerError() throws Exception {
        OwnerDto newOwnerDto = owners.get(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String newOwnerAsJSON = mapper.writeValueAsString(newOwnerDto);
        given(this.clinicService.findOwnerById(999)).willReturn(null);
        this.mockMvc.perform(delete(getBasePath("/owners/999"))
                .content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreatePetSuccess() throws Exception {
        PetDto newPet = pets.get(0);
        newPet.setId(999);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String newPetAsJSON = mapper.writeValueAsString(newPet);
        System.err.println("--> newPetAsJSON=" + newPetAsJSON);
        this.mockMvc.perform(post(getBasePath("/owners/1/pets/"))
                .content(newPetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreatePetError() throws Exception {
        PetDto newPet = pets.get(0);
        newPet.setId(null);
        newPet.setName(null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        String newPetAsJSON = mapper.writeValueAsString(newPet);
        this.mockMvc.perform(post(getBasePath("/owners/1/pets/"))
                .content(newPetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testCreateVisitSuccess() throws Exception {
        VisitDto newVisit = visits.get(0);
        newVisit.setId(999);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String newVisitAsJSON = mapper.writeValueAsString(visitMapper.toVisit(newVisit));
        System.out.println("newVisitAsJSON " + newVisitAsJSON);
        this.mockMvc.perform(post(getBasePath("/owners/1/pets/1/visits"))
                .content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnerPetSuccess() throws Exception {
        owners.remove(0);
        owners.remove(1);
        given(this.clinicService.findAllOwners()).willReturn(ownerMapper.toOwners(owners));
        var owner = ownerMapper.toOwner(owners.get(0));
        given(this.clinicService.findOwnerById(2)).willReturn(owner);
        var pet = petMapper.toPet(pets.get(0));
        pet.setOwner(owner);
        given(this.clinicService.findPetById(1)).willReturn(pet);
        this.mockMvc.perform(get(getBasePath("/owners/2/pets/1"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    void testGetOwnersPetsNotFound() throws Exception {
        owners.clear();
        given(this.clinicService.findAllOwners()).willReturn(ownerMapper.toOwners(owners));
        this.mockMvc.perform(get(getBasePath("/owners/1/pets/1"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


}
