package com.marcusmonteirodesouza.realworld.api.users.services;

import com.google.common.base.Optional;
import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.models.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.NotFoundException;
import java.lang.invoke.MethodHandles;
import org.apache.commons.validator.routines.EmailValidator;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final Logger logger =
            LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    private Keycloak keycloakAdminInstance;

    @Value("${keycloak.admin-password}")
    private String keycloakAdminPassword;

    @Value("${keycloak.admin-username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.client-secret}")
    private String keycloakClientSecret;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @PostConstruct
    public void initKeycloak() {
        logger.info(keycloakServerUrl);
        logger.info(keycloakRealm);
        logger.info(keycloakClientId);
        logger.info(keycloakAdminUsername);
        logger.info(keycloakAdminPassword);

        keycloakAdminInstance =
                KeycloakBuilder.builder()
                        .serverUrl(keycloakServerUrl)
                        .realm(keycloakRealm)
                        .clientId(keycloakClientId)
                        .clientSecret(keycloakClientSecret)
                        .grantType(OAuth2Constants.PASSWORD)
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

        logger.info("Creating user. Username: " + username + ", email: " + email);

        var createUserResponse = usersResource.create(userRepresentation);

        logger.info("User '" + username + "' created!");

        var userId = CreatedResponseUtil.getCreatedId(createUserResponse);

        var passwordCredentialRepresentation = new CredentialRepresentation();

        passwordCredentialRepresentation.setTemporary(false);
        passwordCredentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        passwordCredentialRepresentation.setValue(password);

        var userResource = usersResource.get(userId);

        userResource.resetPassword(passwordCredentialRepresentation);

        return new User(
                userRepresentation.getEmail(),
                userRepresentation.getUsername(),
                Optional.absent(),
                Optional.absent());
    }

    public User getUserById(String userId) {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var userRepresentation = usersResource.get(userId).toRepresentation();

        return new User(
                userRepresentation.getEmail(),
                userRepresentation.getUsername(),
                Optional.fromNullable(userRepresentation.firstAttribute("bio")),
                Optional.fromNullable(userRepresentation.firstAttribute("image")));
    }

    public User getUserByEmail(String email) {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var usersByEmail = usersResource.searchByEmail(email, true);

        if (usersByEmail.isEmpty()) {
            throw new NotFoundException("User with email '" + email + "' not found");
        }

        var userRepresentation = usersByEmail.getFirst();

        return new User(
                userRepresentation.getEmail(),
                userRepresentation.getUsername(),
                Optional.fromNullable(userRepresentation.firstAttribute("bio")),
                Optional.fromNullable(userRepresentation.firstAttribute("image")));
    }

    public User getUserByUsername(String username) {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var usersByEmail = usersResource.searchByUsername(username, true);

        if (usersByEmail.isEmpty()) {
            throw new NotFoundException("User with username '" + username + "' not found");
        }

        var userRepresentation = usersByEmail.getFirst();

        return new User(
                userRepresentation.getEmail(),
                userRepresentation.getUsername(),
                Optional.fromNullable(userRepresentation.firstAttribute("bio")),
                Optional.fromNullable(userRepresentation.firstAttribute("image")));
    }

    public String getToken(String username, String password) {
        var keycloakInstance =
                Keycloak.getInstance(
                        keycloakServerUrl,
                        keycloakRealm,
                        username,
                        password,
                        keycloakClientId,
                        keycloakClientSecret);

        try {
            return keycloakInstance.tokenManager().grantToken().getToken();
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
