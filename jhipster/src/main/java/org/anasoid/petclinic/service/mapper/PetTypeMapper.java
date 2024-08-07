package org.anasoid.petclinic.service.mapper;

import org.mapstruct.Mapper;
import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.service.api.dto.PetTypeDto;
import org.anasoid.petclinic.service.api.dto.PetTypeFieldsDto;

import java.util.Collection;
import java.util.List;

/**
 * Map PetType & PetTypeDto using mapstruct
 */
@Mapper(componentModel = "spring")
public interface PetTypeMapper {

    PetType toPetType(PetTypeDto petTypeDto);

    PetType toPetType(PetTypeFieldsDto petTypeFieldsDto);

    PetTypeDto toPetTypeDto(PetType petType);
    PetTypeFieldsDto toPetTypeFieldsDto(PetType petType);

    List<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);
}
