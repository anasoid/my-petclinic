package org.anasoid.petclinic.service.mapper;

import org.anasoid.petclinic.domain.Owner;
import org.anasoid.petclinic.service.dto.OwnerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Owner} and its DTO {@link OwnerDTO}.
 */
@Mapper(componentModel = "spring")
public interface OwnerMapper extends EntityMapper<OwnerDTO, Owner> {}
