package org.anasoid.petclinic.mapper;

import org.mapstruct.Mapper;
import org.anasoid.petclinic.domain.Specialty;
import org.anasoid.petclinic.service.api.dto.SpecialtyDto;

import java.util.Collection;

/**
 * Map Specialty & SpecialtyDto using mapstruct
 */
@Mapper(componentModel = "spring")
public interface SpecialtyMapper {
    Specialty toSpecialty(SpecialtyDto specialtyDto);

    SpecialtyDto toSpecialtyDto(Specialty specialty);

    Collection<SpecialtyDto> toSpecialtyDtos(Collection<Specialty> specialties);

    Collection<Specialty> toSpecialtys(Collection<SpecialtyDto> specialties);

}
