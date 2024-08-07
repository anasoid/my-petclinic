package org.anasoid.petclinic.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.anasoid.petclinic.domain.Pet;
import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.service.api.dto.PetDto;
import org.anasoid.petclinic.service.api.dto.PetFieldsDto;
import org.anasoid.petclinic.service.api.dto.PetTypeDto;

import java.util.Collection;

/**
 * Map Pet & PetDto using mapstruct
 */
@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    PetDto toPetDto(Pet pet);

    Collection<PetDto> toPetsDto(Collection<Pet> pets);

    Collection<Pet> toPets(Collection<PetDto> pets);

    Pet toPet(PetDto petDto);

    Pet toPet(PetFieldsDto petFieldsDto);

    PetTypeDto toPetTypeDto(PetType petType);

    PetType toPetType(PetTypeDto petTypeDto);

    Collection<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);
}
