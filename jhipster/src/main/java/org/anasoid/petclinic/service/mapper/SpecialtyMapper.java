package org.anasoid.petclinic.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.anasoid.petclinic.domain.Specialty;
import org.anasoid.petclinic.domain.Vet;
import org.anasoid.petclinic.service.dto.SpecialtyDTO;
import org.anasoid.petclinic.service.dto.VetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {
    @Mapping(target = "vets", source = "vets", qualifiedByName = "vetIdSet")
    SpecialtyDTO toDto(Specialty s);

    @Mapping(target = "vets", ignore = true)
    @Mapping(target = "removeVet", ignore = true)
    Specialty toEntity(SpecialtyDTO specialtyDTO);

    @Named("vetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VetDTO toDtoVetId(Vet vet);

    @Named("vetIdSet")
    default Set<VetDTO> toDtoVetIdSet(Set<Vet> vet) {
        return vet.stream().map(this::toDtoVetId).collect(Collectors.toSet());
    }
}
