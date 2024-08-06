package org.anasoid.petclinic.mapper;

import org.mapstruct.Mapper;
import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.service.api.dto.VetDto;
import org.anasoid.petclinic.service.api.dto.VetFieldsDto;

import java.util.Collection;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper(uses = SpecialtyMapper.class)
public interface VetMapper {
    Vet toVet(VetDto vetDto);

    Vet toVet(VetFieldsDto vetFieldsDto);

    VetDto toVetDto(Vet vet);

    Collection<VetDto> toVetDtos(Collection<Vet> vets);
}
