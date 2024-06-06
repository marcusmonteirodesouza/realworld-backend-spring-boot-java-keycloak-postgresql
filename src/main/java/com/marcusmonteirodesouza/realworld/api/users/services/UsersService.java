package com.marcusmonteirodesouza.realworld.api.users.services;

import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.models.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.validator.routines.EmailValidator;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private Keycloak keycloakAdminInstance;

    @Value("${keycloak.admin-client-id}")
    private String keycloakAdminClientId;

    @Value("${keycloak.admin-password}")
    private String keycloakAdminPassword;

    @Value("${keycloak.admin-username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.external-client-id}")
    private String keycloakExternalClientId;

    @Value("${keycloak.external-client-secret}")
    private String keycloakExternalClientSecret;

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @PostConstruct
    public void initKeycloak() {
        keycloakAdminInstance =
                KeycloakBuilder.builder()
                        .serverUrl(keycloakServerUrl)
                        .realm("master")
                        .clientId(keycloakAdminClientId)
                        .grantType("password")
                        .username(keycloakAdminUsername)
                        .password(keycloakAdminPassword)
                        .build();
    }

    @PreDestroy
    public void closeKeycloak() {
        keycloakAdminInstance.close();
    }

    public User createUser(String username, String email, String password)
            throws AlreadyExistsException {
        validateUsername(username);

        validateEmail(email);

        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentation.setEmail(email);
        userRepresentation.setEnabled(true);

        var createUserResponse = usersResource.create(userRepresentation);

        var userId = CreatedResponseUtil.getCreatedId(createUserResponse);

        var passwordCredentialRepresentation = new CredentialRepresentation();

        passwordCredentialRepresentation.setTemporary(false);
        passwordCredentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        passwordCredentialRepresentation.setValue(password);

        var userResource = usersResource.get(userId);

        userResource.resetPassword(passwordCredentialRepresentation);

        return new User(
                userRepresentation.getEmail(), userRepresentation.getUsername(), null, null);
    }

    public String getToken(String username, String password) {
        var keycloakInstance =
                Keycloak.getInstance(
                        keycloakServerUrl,
                        keycloakRealm,
                        username,
                        password,
                        keycloakExternalClientId,
                        keycloakExternalClientSecret);

        try {
            return keycloakAdminInstance.tokenManager().grantToken().getToken();
        } finally {
            keycloakInstance.close();
        }
    }

    private void validateUsername(String username) throws AlreadyExistsException {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var usersByUsername = usersResource.searchByUsername(username, true);
        if (!usersByUsername.isEmpty()) {
            throw new AlreadyExistsException("Username '" + username + "' is taken");
        }
    }

    private void validateEmail(String email) throws AlreadyExistsException {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }

        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var usersByEmail = usersResource.searchByEmail(email, true);
        if (!usersByEmail.isEmpty()) {
            throw new AlreadyExistsException("Email '" + email + "' is taken");
        }
    }
}
