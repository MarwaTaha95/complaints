package com.abc.complaints.controller.authentication;

import com.abc.complaints.entity.Person;
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

import java.nio.file.AccessDeniedException;

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

    @PostMapping
    public Object login(@RequestBody LoginRequest request) throws Exception {
        validateUserNotAuthenticated();
        validateRequestParamsNotNull(request);

        var person = getPersonOfCredentials(request);
        authenticateUser(person);
        LoginResponse response = new LoginResponse();
        response.setName(person.getName());
        response.setEmail(person.getEmail());

        return response;
    }

    private void validateRequestParamsNotNull(LoginRequest loginRequest) throws EmptyFieldException {
        validateParamNotNull(loginRequest.getEmail(), "Email");
        validateParamNotNull(loginRequest.getPassword(), "Password");
    }

    private Person getPersonOfCredentials(LoginRequest request) throws Exception {
        Person person = people.findByEmail(request.getEmail());
        if (person == null) {
            throw new EntityNotFoundException("Cannot find user with email: " + request.getEmail()); // return to sign up page; account does not exist
        }

        boolean isPasswordCorrect = passwordEncoder.matches(request.getPassword(), person.getPassword());
        if (!isPasswordCorrect) {
            throw new AccessDeniedException("Authentication failed, wrong password"); // return error, wrong password
        }
        return person;
    }
}
