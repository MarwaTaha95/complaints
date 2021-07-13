package com.abc.complaints.controller.authentication;

import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.RoleType;
import com.abc.complaints.entity.Session;
import com.abc.complaints.exception.entity.EmptyFieldException;
import com.abc.complaints.exception.entity.EntityNotFoundException;
import com.abc.complaints.repository.PersonRepository;
import com.abc.complaints.rest.request.LoginRequest;
import com.abc.complaints.rest.response.LoginResponse;
import com.abc.complaints.service.SessionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

/**
 * This API is for authenticating a user, with email and password.
 */
@RestController
@RequestMapping("/api/v1/rest/login")
public class LoginController extends AbstractAuthenticationController {
    private final PersonRepository people;
    private final PasswordEncoder passwordEncoder;

    public LoginController(Session session, SessionService sessions, PersonRepository people, PasswordEncoder passwordEncoder) {
        super(session, sessions);
        this.people = people;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param request, contains the login info( email and password) to authenticate a user
     * @return loginResponse containing user's main info
     */
    @PostMapping
    public Object login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) throws Exception {
        validateUserNotAuthenticated();
        validateRequestParamsNotNull(request);

        var person = getPersonOfCredentials(request);
        authenticateUser(person);
        LoginResponse response = new LoginResponse();
        response.setName(person.getName());
        response.setEmail(person.getEmail());
        response.setAdmin(person.getRoleType().equals(RoleType.ADMIN));

        return response;
    }

    /**
     * Validate request params aren't null
     * */
    private void validateRequestParamsNotNull(LoginRequest loginRequest) throws EmptyFieldException {
        validateParamNotNull(loginRequest.getEmail(), "Email");
        validateParamNotNull(loginRequest.getPassword(), "Password");
    }

    /**
     * Get from db, the user with the credentials provided
     *
     * @throws EntityNotFoundException if there is no user with provided email
     * @throws AccessDeniedException if the password provided doesn't match the stored password
     * */
    private Person getPersonOfCredentials(LoginRequest request) throws Exception {
        Person person = people.findByEmail(request.getEmail());
        if (person == null) {
            throw new EntityNotFoundException("Cannot find user with email: " + request.getEmail());
        }

        boolean isPasswordCorrect = passwordEncoder.matches(request.getPassword(), person.getPassword());
        if (!isPasswordCorrect) {
            throw new AccessDeniedException("Authentication failed, wrong password");
        }
        return person;
    }
}
