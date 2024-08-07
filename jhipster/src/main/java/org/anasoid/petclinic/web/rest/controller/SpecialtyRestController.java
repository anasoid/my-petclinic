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

import org.anasoid.petclinic.domain.Specialty;
import org.anasoid.petclinic.service.ClinicService;
import org.anasoid.petclinic.service.api.dto.SpecialtyDto;
import org.anasoid.petclinic.service.mapper.SpecialtyMapper;
import org.anasoid.petclinic.web.api.SpecialtiesApiDelegate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecialtyRestController implements SpecialtiesApiDelegate {

    private final ClinicService clinicService;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyRestController(ClinicService clinicService, SpecialtyMapper specialtyMapper) {
        this.clinicService = clinicService;
        this.specialtyMapper = specialtyMapper;
    }


    @Override
    public ResponseEntity<List<SpecialtyDto>> listSpecialties() {
        List<SpecialtyDto> specialties = new ArrayList<>();
        specialties.addAll(specialtyMapper.toSpecialtyDtos(this.clinicService.findAllSpecialties()));
        if (specialties.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<SpecialtyDto> getSpecialty(Integer specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<SpecialtyDto> addSpecialty(SpecialtyDto specialtyDto) {
        HttpHeaders headers = new HttpHeaders();
        Specialty specialty = specialtyMapper.toSpecialty(specialtyDto);
        this.clinicService.saveSpecialty(specialty);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/specialties/{id}").buildAndExpand(specialty.getId()).toUri());
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(specialty), headers, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<SpecialtyDto> updateSpecialty(Integer specialtyId, SpecialtyDto specialtyDto) {
        Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
        if (currentSpecialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentSpecialty.setName(specialtyDto.getName());
        this.clinicService.saveSpecialty(currentSpecialty);
        return new ResponseEntity<>(specialtyMapper.toSpecialtyDto(currentSpecialty), HttpStatus.NO_CONTENT);
    }


    @Override
    public ResponseEntity<SpecialtyDto> deleteSpecialty(Integer specialtyId) {
        Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteSpecialty(specialty);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
