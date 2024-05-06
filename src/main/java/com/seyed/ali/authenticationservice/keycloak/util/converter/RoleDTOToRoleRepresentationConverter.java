package com.seyed.ali.authenticationservice.keycloak.util.converter;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleDTOToRoleRepresentationConverter implements Converter<RoleDTO, RoleRepresentation> {

    @Override
    public RoleRepresentation convert(RoleDTO source) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName(source.name());
        roleRepresentation.setDescription(source.description());
        return roleRepresentation;
    }

}
