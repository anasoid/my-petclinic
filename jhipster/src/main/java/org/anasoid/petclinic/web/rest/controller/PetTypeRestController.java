package org.anasoid.petclinic.web.rest.controller;/*
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

import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.service.ClinicService;
import org.anasoid.petclinic.service.api.dto.PetTypeDto;
import org.anasoid.petclinic.service.api.dto.PetTypeFieldsDto;
import org.anasoid.petclinic.service.mapper.PetTypeMapper;
import org.anasoid.petclinic.web.api.PettypesApiDelegate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
public class PetTypeRestController implements PettypesApiDelegate {


    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;


    public PetTypeRestController(ClinicService clinicService, PetTypeMapper petTypeMapper) {
        this.clinicService = clinicService;
        this.petTypeMapper = petTypeMapper;
    }


    @Override
    public ResponseEntity<List<PetTypeDto>> listPetTypes() {
        List<PetType> petTypes = new ArrayList<>(this.clinicService.findAllPetTypes());
        if (petTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petTypeMapper.toPetTypeDtos(petTypes), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<PetTypeDto> getPetType(Integer petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<PetTypeDto> addPetType(PetTypeFieldsDto petTypeFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        final PetType type = petTypeMapper.toPetType(petTypeFieldsDto);
        this.clinicService.savePetType(type);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pettypes/{id}").buildAndExpand(type.getId()).toUri());
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(type), headers, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<PetTypeDto> updatePetType(Integer petTypeId, PetTypeDto petTypeDto) {
        PetType currentPetType = this.clinicService.findPetTypeById(petTypeId);
        if (currentPetType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPetType.setName(petTypeDto.getName());
        this.clinicService.savePetType(currentPetType);
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(currentPetType), HttpStatus.NO_CONTENT);
    }


    @Override
    public ResponseEntity<PetTypeDto> deletePetType(Integer petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deletePetType(petType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
