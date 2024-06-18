package com.marcusmonteirodesouza.realworld.api.users.services.users;

import com.marcusmonteirodesouza.realworld.api.exceptions.AlreadyExistsException;
import com.marcusmonteirodesouza.realworld.api.users.models.User;
import com.marcusmonteirodesouza.realworld.api.users.services.users.parameterobjects.UserUpdate;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
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
        logger.info("Creating User. Username: " + username + ", email: " + email);

        validateUsername(username);

        validateEmail(email);

        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentation.setEmail(email);
        userRepresentation.setEnabled(true);

        var createUserResponse = usersResource.create(userRepresentation);

        var userId = CreatedResponseUtil.getCreatedId(createUserResponse);

        var passwordCredentialRepresentation = makePasswordCredentialRepresentation(password);

        var userResource = usersResource.get(userId);

        userResource.resetPassword(passwordCredentialRepresentation);

        return new User(userRepresentation);
    }

    public Optional<User> getUserById(String userId) {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var userRepresentation = usersResource.get(userId).toRepresentation();

        return Optional.of(new User(userRepresentation));
    }

    public Optional<User> getUserByEmail(String email) {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var usersByEmail = usersResource.searchByEmail(email, true);

        if (usersByEmail.isEmpty()) {
            return Optional.empty();
        }

        var userRepresentation = usersByEmail.getFirst();

        return Optional.of(new User(userRepresentation));
    }

    public Optional<User> getUserByUsername(String username) {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var usersByEmail = usersResource.searchByUsername(username, true);

        if (usersByEmail.isEmpty()) {
            return Optional.empty();
        }

        var userRepresentation = usersByEmail.getFirst();

        return Optional.of(new User(userRepresentation));
    }

    public List<User> listUsers() {
        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var userRepresentations = usersResource.list();

        return userRepresentations.stream()
                .map(userRepresentation -> new User(userRepresentation))
                .collect(Collectors.toList());
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

    public Optional<User> updateUser(String userId, UserUpdate userUpdate)
            throws AlreadyExistsException {
        logger.info(
                "Updating User: "
                        + userId
                        + ". Username: "
                        + userUpdate.getUsername()
                        + ", Email: "
                        + userUpdate.getEmail()
                        + ", Bio: "
                        + userUpdate.getBio()
                        + ", Image: "
                        + userUpdate.getImage()
                        + ", Updating Password: "
                        + userUpdate.getPassword().isPresent());

        var usersResource = keycloakAdminInstance.realm(keycloakRealm).users();

        var userResource = usersResource.get(userId);

        var userRepresentation = userResource.toRepresentation();

        if (userUpdate.getUsername().isPresent()) {
            var username = userUpdate.getUsername().get();

            if (!username.equals(userRepresentation.getUsername())) {
                validateUsername(username);
                userRepresentation.setUsername(username);
            }
        }

        if (userUpdate.getEmail().isPresent()) {
            var email = userUpdate.getEmail().get();

            if (!email.equals(userRepresentation.getEmail())) {
                validateEmail(email);
                userRepresentation.setEmail(email);
            }
        }

        if (userUpdate.getBio().isPresent()) {
            userRepresentation.singleAttribute("bio", userUpdate.getBio().get());
        }

        if (userUpdate.getImage().isPresent()) {
            var image = userUpdate.getImage().get();
            validateImage(image);
            userRepresentation.singleAttribute("image", image);
        }

        userResource.update(userRepresentation);

        if (userUpdate.getPassword().isPresent()) {
            var passwordCredentialRepresentation =
                    makePasswordCredentialRepresentation(userUpdate.getPassword().get());
            userResource.resetPassword(passwordCredentialRepresentation);
        }

        return Optional.of(
                new User(
                        userRepresentation.getId(),
                        userRepresentation.getEmail(),
                        userRepresentation.getUsername(),
                        Optional.ofNullable(userRepresentation.firstAttribute("bio")),
                        Optional.ofNullable(userRepresentation.firstAttribute("image"))));
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

    private void validateImage(String image) {
        if (!UrlValidator.getInstance().isValid(image)) {
            throw new IllegalArgumentException("Invalid image URL: " + image);
        }
    }

    private CredentialRepresentation makePasswordCredentialRepresentation(String password) {
        var passwordCredentialRepresentation = new CredentialRepresentation();

        passwordCredentialRepresentation.setTemporary(false);
        passwordCredentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        passwordCredentialRepresentation.setValue(password);

        return passwordCredentialRepresentation;
    }
}
