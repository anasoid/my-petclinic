package org.anasoid.petclinic.web.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.service.ClinicService;
import org.anasoid.petclinic.service.mapper.VetMapper;
import org.anasoid.petclinic.web.api.VetsApiController;
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
class VetRestControllerTest extends AbstractRestControllerTest {
    @Autowired
    Environment env;


    @Autowired
    private VetsApiController vetRestController;

    @Autowired
    private VetMapper vetMapper;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<Vet> vets;

    @BeforeEach
    void initVets() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(vetRestController)
            .setControllerAdvice(new ExceptionTranslator(env))
            .build();
        vets = new ArrayList<Vet>();


        Vet vet = new Vet();
        vet.setId(1l);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        vets.add(vet);

        vet = new Vet();
        vet.setId(2l);
        vet.setFirstName("Helen");
        vet.setLastName("Leary");
        vets.add(vet);

        vet = new Vet();
        vet.setId(3l);
        vet.setFirstName("Linda");
        vet.setLastName("Douglas");
        vets.add(vet);
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testGetVetSuccess() throws Exception {
        given(this.clinicService.findVetById(1)).willReturn(vets.get(0));
        this.mockMvc.perform(get(getBasePath("/vets/1"))
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("James"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testGetVetNotFound() throws Exception {
        given(this.clinicService.findVetById(-1)).willReturn(null);
        this.mockMvc.perform(get(getBasePath("/vets/999"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testGetAllVetsSuccess() throws Exception {
        given(this.clinicService.findAllVets()).willReturn(vets);
        this.mockMvc.perform(get(getBasePath("/vets/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[0].firstName").value("James"))
            .andExpect(jsonPath("$.[1].id").value(2))
            .andExpect(jsonPath("$.[1].firstName").value("Helen"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testGetAllVetsNotFound() throws Exception {
        vets.clear();
        given(this.clinicService.findAllVets()).willReturn(vets);
        this.mockMvc.perform(get(getBasePath("/vets/"))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testCreateVetSuccess() throws Exception {
        Vet newVet = vets.get(0);
        newVet.setId(999l);
        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
        this.mockMvc.perform(post(getBasePath("/vets/"))
                .content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testCreateVetError() throws Exception {
        Vet newVet = vets.get(0);
        newVet.setId(null);
        newVet.setFirstName(null);
        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
        this.mockMvc.perform(post(getBasePath("/vets/"))
                .content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testUpdateVetSuccess() throws Exception {
        given(this.clinicService.findVetById(1)).willReturn(vets.get(0));
        Vet newVet = vets.get(0);
        newVet.setFirstName("James");
        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
        this.mockMvc.perform(put(getBasePath("/vets/1"))
                .content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isNoContent());

        this.mockMvc.perform(get(getBasePath("/vets/1"))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("James"));

    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testUpdateVetError() throws Exception {
        Vet newVet = vets.get(0);
        newVet.setFirstName(null);
        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
        this.mockMvc.perform(put(getBasePath("/vets/1"))
                .content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testDeleteVetSuccess() throws Exception {
        Vet newVet = vets.get(0);
        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
        given(this.clinicService.findVetById(1)).willReturn(vets.get(0));
        this.mockMvc.perform(delete(getBasePath("/vets/1"))
                .content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    void testDeleteVetError() throws Exception {
        Vet newVet = vets.get(0);
        ObjectMapper mapper = new ObjectMapper();
        String newVetAsJSON = mapper.writeValueAsString(vetMapper.toVetDto(newVet));
        given(this.clinicService.findVetById(-1)).willReturn(null);
        this.mockMvc.perform(delete(getBasePath("/vets/999"))
                .content(newVetAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
    }
}
