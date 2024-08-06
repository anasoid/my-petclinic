package org.anasoid.petclinic.web.delegate;/*
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
 * Date :   8/6/24
 */

import org.anasoid.petclinic.domain.Owner;
import org.anasoid.petclinic.mapper.OwnerMapper;
import org.anasoid.petclinic.repository.OwnerRepository;
import org.anasoid.petclinic.service.OwnerQueryService;
import org.anasoid.petclinic.service.OwnerService;
import org.anasoid.petclinic.service.api.dto.*;
import org.anasoid.petclinic.service.criteria.OwnerCriteria;
import org.anasoid.petclinic.web.api.OwnersApiDelegate;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tech.jhipster.service.filter.StringFilter;

import java.util.Collection;
import java.util.List;

@Component
public class DefaultOwnersApiDelegate implements OwnersApiDelegate {

    private final OwnerService ownerService;

    private final OwnerRepository ownerRepository;

    private final OwnerQueryService ownerQueryService;

    private final OwnerMapper ownerMapper;

    public DefaultOwnersApiDelegate(OwnerService ownerService, OwnerRepository ownerRepository, OwnerQueryService ownerQueryService, OwnerMapper ownerMapper) {
        this.ownerService = ownerService;
        this.ownerRepository = ownerRepository;
        this.ownerQueryService = ownerQueryService;
        this.ownerMapper = ownerMapper;
    }

    @Override
    public ResponseEntity<OwnerDto> addOwner(OwnerFieldsDto ownerFieldsDto) {
        return OwnersApiDelegate.super.addOwner(ownerFieldsDto);
    }

    @Override
    public ResponseEntity<PetDto> addPetToOwner(Integer ownerId, PetFieldsDto petFieldsDto) {
        return OwnersApiDelegate.super.addPetToOwner(ownerId, petFieldsDto);
    }

    @Override
    public ResponseEntity<VisitDto> addVisitToOwner(Integer ownerId, Integer petId, VisitFieldsDto visitFieldsDto) {
        return OwnersApiDelegate.super.addVisitToOwner(ownerId, petId, visitFieldsDto);
    }

    @Override
    public ResponseEntity<OwnerDto> deleteOwner(Integer ownerId) {
        return OwnersApiDelegate.super.deleteOwner(ownerId);
    }

    @Override
    public ResponseEntity<OwnerDto> getOwner(Integer ownerId) {
        return OwnersApiDelegate.super.getOwner(ownerId);
    }

    @Override
    public ResponseEntity<PetDto> getOwnersPet(Integer ownerId, Integer petId) {
        return OwnersApiDelegate.super.getOwnersPet(ownerId, petId);
    }

    @Override
    public ResponseEntity<List<OwnerDto>> listOwners(String lastName) {
        Collection<Owner> owners;


        if (lastName != null) {
            OwnerCriteria ownerCriteria = new OwnerCriteria();
            ownerCriteria.setLastName(new StringFilter().setContains(lastName));
            owners = this.ownerQueryService.findByCriteria(ownerCriteria, Pageable.unpaged()).getContent();
        } else {
            owners = this.ownerRepository.findAll();
        }
        if (owners.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ownerMapper.toOwnerDtoCollection(owners), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OwnerDto> updateOwner(Integer ownerId, OwnerFieldsDto ownerFieldsDto) {
        return OwnersApiDelegate.super.updateOwner(ownerId, ownerFieldsDto);
    }

    @Override
    public ResponseEntity<Void> updateOwnersPet(Integer ownerId, Integer petId, PetFieldsDto petFieldsDto) {
        return OwnersApiDelegate.super.updateOwnersPet(ownerId, petId, petFieldsDto);
    }
}
