package org.anasoid.petclinic.service.mapper;

import org.anasoid.petclinic.domain.Role;
import org.anasoid.petclinic.service.dto.RoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {}
