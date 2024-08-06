package org.anasoid.petclinic.mapper;

import org.anasoid.petclinic.domain.Owner;
import org.anasoid.petclinic.service.api.dto.OwnerDto;
import org.anasoid.petclinic.service.api.dto.OwnerFieldsDto;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * Maps Owner & OwnerDto using Mapstruct
 */
@Mapper(uses = PetMapper.class, componentModel = "spring")
public interface OwnerMapper {

    OwnerDto toOwnerDto(Owner owner);

    Owner toOwner(OwnerDto ownerDto);

    Owner toOwner(OwnerFieldsDto ownerDto);

    List<OwnerDto> toOwnerDtoCollection(Collection<Owner> ownerCollection);

    Collection<Owner> toOwners(Collection<OwnerDto> ownerDtos);
}
