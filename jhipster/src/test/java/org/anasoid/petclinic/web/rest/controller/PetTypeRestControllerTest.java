package org.anasoid.petclinic.web.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.service.ClinicService;
import org.anasoid.petclinic.service.mapper.PetTypeMapper;
import org.anasoid.petclinic.web.api.PettypesApiController;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class PetTypeRestControllerTest extends AbstractRestControllerTest {
    @Autowired
    Environment env;


    @Autowired
    private PettypesApiController petTypeRestController;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<PetType> petTypes;

    @BeforeEach
    void initPetTypes(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(petTypeRestController)
            .setControllerAdvice(new ExceptionTranslator(env))
            .build();
        petTypes = new ArrayList<>();

        PetType petType = new PetType();
        petType.setId(1l);
        petType.setName("cat");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(2l);
        petType.setName("dog");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(3l);
        petType.setName("lizard");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(4l);
        petType.setName("snake");
        petTypes.add(petType);
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetPetTypeSuccessAsOwnerAdmin() throws Exception {
        given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
        this.mockMvc.perform(get(getBasePath("/pettypes/1"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetPetTypeSuccessAsVetAdmin() throws Exception {
        given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
        this.mockMvc.perform(get(getBasePath("/pettypes/1"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetPetTypeNotFound() throws Exception {
        given(this.clinicService.findPetTypeById(999)).willReturn(null);
        this.mockMvc.perform(get(getBasePath("/pettypes/999"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    void testGetAllPetTypesSuccessAsOwnerAdmin() throws Exception {
        petTypes.remove(0);
        petTypes.remove(1);
        given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get(getBasePath("/pettypes/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].name").value("dog"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].name").value("snake"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllPetTypesSuccessAsVetAdmin() throws Exception {
        petTypes.remove(0);
        petTypes.remove(1);
        given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get(getBasePath("/pettypes/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].name").value("dog"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].name").value("snake"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testGetAllPetTypesNotFound() throws Exception {
        petTypes.clear();
        given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get(getBasePath("/pettypes/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreatePetTypeSuccess() throws Exception {
        PetType newPetType = petTypes.get(0);
        newPetType.setId(null);
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeFieldsDto(newPetType));
        this.mockMvc.perform(post(getBasePath("/pettypes/"))
                .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testCreatePetTypeError() throws Exception {
        PetType newPetType = petTypes.get(0);
        newPetType.setId(null);
        newPetType.setName(null);
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
        this.mockMvc.perform(post(getBasePath("/pettypes/"))
                .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdatePetTypeSuccess() throws Exception {
        given(this.clinicService.findPetTypeById(2)).willReturn(petTypes.get(1));
        PetType newPetType = petTypes.get(1);
        newPetType.setName("dog I");
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
        this.mockMvc.perform(put(getBasePath("/pettypes/2"))
                .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get(getBasePath("/pettypes/2"))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("dog I"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testUpdatePetTypeError() throws Exception {
        PetType newPetType = petTypes.get(0);
        newPetType.setName("");
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
        this.mockMvc.perform(put(getBasePath("/pettypes/1"))
                .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeletePetTypeSuccess() throws Exception {
        PetType newPetType = petTypes.get(0);
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
        given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
        this.mockMvc.perform(delete(getBasePath("/pettypes/1"))
                .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    void testDeletePetTypeError() throws Exception {
        PetType newPetType = petTypes.get(0);
        ObjectMapper mapper = new ObjectMapper();
        String newPetTypeAsJSON = mapper.writeValueAsString(petTypeMapper.toPetTypeDto(newPetType));
        given(this.clinicService.findPetTypeById(999)).willReturn(null);
        this.mockMvc.perform(delete(getBasePath("/pettypes/999"))
                .content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }

}
