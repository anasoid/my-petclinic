package org.anasoid.petclinic.service.mapper;

import org.anasoid.petclinic.domain.PetType;
import org.anasoid.petclinic.service.dto.PetTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PetType} and its DTO {@link PetTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface PetTypeMapper extends EntityMapper<PetTypeDTO, PetType> {}
