package org.anasoid.petclinic.service.mapper;

import org.anasoid.petclinic.domain.Pet;
import org.anasoid.petclinic.domain.Visit;
import org.anasoid.petclinic.service.dto.PetDTO;
import org.anasoid.petclinic.service.dto.VisitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Visit} and its DTO {@link VisitDTO}.
 */
@Mapper(componentModel = "spring")
public interface VisitMapper extends EntityMapper<VisitDTO, Visit> {
    @Mapping(target = "pets", source = "pets", qualifiedByName = "petId")
    VisitDTO toDto(Visit s);

    @Named("petId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PetDTO toDtoPetId(Pet pet);
}
