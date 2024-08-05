package org.anasoid.petclinic.service.mapper;

import org.anasoid.petclinic.domain.Owner;
import org.anasoid.petclinic.domain.Pet;
import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.service.dto.OwnerDTO;
import org.anasoid.petclinic.service.dto.PetDTO;
import org.anasoid.petclinic.service.dto.PetTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pet} and its DTO {@link PetDTO}.
 */
@Mapper(componentModel = "spring")
public interface PetMapper extends EntityMapper<PetDTO, Pet> {
    @Mapping(target = "type", source = "type", qualifiedByName = "petTypeName")
    @Mapping(target = "owner", source = "owner", qualifiedByName = "ownerId")
    PetDTO toDto(Pet s);

    @Named("petTypeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PetTypeDTO toDtoPetTypeName(PetType petType);

    @Named("ownerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OwnerDTO toDtoOwnerId(Owner owner);
}
