package org.anasoid.petclinic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.anasoid.petclinic.domain.Visit;
import org.anasoid.petclinic.service.api.dto.VisitDto;
import org.anasoid.petclinic.service.api.dto.VisitFieldsDto;

import java.util.Collection;

/**
 * Map Visit & VisitDto using mapstruct
 */
@Mapper(uses = PetMapper.class)
public interface VisitMapper {
    Visit toVisit(VisitDto visitDto);

    Visit toVisit(VisitFieldsDto visitFieldsDto);

    @Mapping(source = "pet.id", target = "petId")
    VisitDto toVisitDto(Visit visit);

    Collection<VisitDto> toVisitsDto(Collection<Visit> visits);

}
