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
import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.service.ClinicService;
import org.anasoid.petclinic.service.api.dto.VetDto;
import org.anasoid.petclinic.service.mapper.SpecialtyMapper;
import org.anasoid.petclinic.service.mapper.VetMapper;
import org.anasoid.petclinic.web.api.VetsApiDelegate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VetRestController implements VetsApiDelegate {

    private final ClinicService clinicService;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    public VetRestController(ClinicService clinicService, VetMapper vetMapper, SpecialtyMapper specialtyMapper) {
        this.clinicService = clinicService;
        this.vetMapper = vetMapper;
        this.specialtyMapper = specialtyMapper;
    }

    @Override
    public ResponseEntity<List<VetDto>> listVets() {
        List<VetDto> vets = new ArrayList<>();
        vets.addAll(vetMapper.toVetDtos(this.clinicService.findAllVets()));
        if (vets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<VetDto> getVet(Integer vetId) {
        Vet vet = this.clinicService.findVetById(vetId);
        if (vet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vetMapper.toVetDto(vet), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VetDto> addVet(VetDto vetDto) {
        HttpHeaders headers = new HttpHeaders();
        Vet vet = vetMapper.toVet(vetDto);
        if (!CollectionUtils.isEmpty(vet.getSpecialties())) {
            List<Specialty> vetSpecialities = this.clinicService.findSpecialtiesByNameIn(vet.getSpecialties().stream().map(Specialty::getName).collect(Collectors.toSet()));
            vet.setSpecialties(vetSpecialities == null ? null : new HashSet<>(vetSpecialities));
        }
        this.clinicService.saveVet(vet);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/vets/{id}").buildAndExpand(vet.getId()).toUri());
        return new ResponseEntity<>(vetMapper.toVetDto(vet), headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<VetDto> updateVet(Integer vetId, VetDto vetDto) {
        Vet currentVet = this.clinicService.findVetById(vetId);
        if (currentVet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentVet.setFirstName(vetDto.getFirstName());
        currentVet.setLastName(vetDto.getLastName());
        currentVet.getSpecialties().clear();
        for (Specialty spec : specialtyMapper.toSpecialtys(vetDto.getSpecialties())) {
            currentVet.getSpecialties().add(clinicService.findSpecialtyById(spec.getId()));
        }
        if (!CollectionUtils.isEmpty(currentVet.getSpecialties())) {
            List<Specialty> vetSpecialities = this.clinicService.findSpecialtiesByNameIn(currentVet.getSpecialties().stream().map(Specialty::getName).collect(Collectors.toSet()));
            currentVet.setSpecialties(vetSpecialities == null ? null : new HashSet<>(vetSpecialities));

        }
        this.clinicService.saveVet(currentVet);
        return new ResponseEntity<>(vetMapper.toVetDto(currentVet), HttpStatus.NO_CONTENT);
    }


    @Override
    public ResponseEntity<VetDto> deleteVet(Integer vetId) {
        Vet vet = this.clinicService.findVetById(vetId);
        if (vet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteVet(vet);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
