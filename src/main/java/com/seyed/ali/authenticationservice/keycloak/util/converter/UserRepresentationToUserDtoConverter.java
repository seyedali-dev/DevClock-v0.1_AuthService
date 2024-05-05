package com.seyed.ali.authenticationservice.keycloak.util.converter;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRepresentationToUserDtoConverter implements Converter<UserRepresentation, UserDTO> {

    @Override
    public UserDTO convert(UserRepresentation source) {
        return new UserDTO(
                source.getId(),
                source.getFirstName(),
                source.getLastName(),
                source.getEmail(),
                source.getUsername(),
                null
        );
    }

}
