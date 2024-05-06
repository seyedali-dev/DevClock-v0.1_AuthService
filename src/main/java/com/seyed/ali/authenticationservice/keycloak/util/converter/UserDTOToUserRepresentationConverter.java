package com.seyed.ali.authenticationservice.keycloak.util.converter;

import com.seyed.ali.authenticationservice.keycloak.model.dto.UserDTO;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDTOToUserRepresentationConverter implements Converter<UserDTO, UserRepresentation> {

    @Override
    public UserRepresentation convert(UserDTO source) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(source.firstName());
        userRepresentation.setLastName(source.lastName());
        userRepresentation.setUsername(source.username());
        userRepresentation.setEmail(source.email());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRep = new CredentialRepresentation();
        credentialRep.setTemporary(false);
        credentialRep.setValue(source.password());

        List<CredentialRepresentation> credentialRepresentationList = new ArrayList<>();
        credentialRepresentationList.add(credentialRep);

        userRepresentation.setCredentials(credentialRepresentationList);
        return userRepresentation;
    }

}
