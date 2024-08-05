package org.anasoid.petclinic.service.mapper;

import org.anasoid.petclinic.domain.Person;
import org.anasoid.petclinic.service.dto.PersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {}
