package org.anasoid.petclinic.service.mapper;

import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.service.api.dto.VetDto;
import org.anasoid.petclinic.service.api.dto.VetFieldsDto;
import org.mapstruct.Mapper;

import java.util.Collection;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper(uses = SpecialtyMapper.class, componentModel = "spring")
public interface VetMapper {
    Vet toVet(VetDto vetDto);

    Vet toVet(VetFieldsDto vetFieldsDto);

    VetDto toVetDto(Vet vet);

    Collection<VetDto> toVetDtos(Collection<Vet> vets);
}
