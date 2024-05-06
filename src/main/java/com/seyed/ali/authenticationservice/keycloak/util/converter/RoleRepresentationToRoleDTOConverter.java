package com.seyed.ali.authenticationservice.keycloak.util.converter;

import com.seyed.ali.authenticationservice.keycloak.model.dto.RoleDTO;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleRepresentationToRoleDTOConverter implements Converter<RoleRepresentation, RoleDTO> {

    @Override
    public RoleDTO convert(RoleRepresentation source) {
        return new RoleDTO(source.getName(), source.getDescription());
    }

}
