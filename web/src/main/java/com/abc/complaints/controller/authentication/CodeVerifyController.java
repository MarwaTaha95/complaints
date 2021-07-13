package com.abc.complaints.controller.authentication;

import com.abc.complaints.Constants;
import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.Session;
import com.abc.complaints.entity.Totp;
import com.abc.complaints.exception.entity.EmptyFieldException;
import com.abc.complaints.exception.entity.EntityNotFoundException;
import com.abc.complaints.repository.PersonRepository;
import com.abc.complaints.rest.request.VerifyCodeRequest;
import com.abc.complaints.rest.response.RegisterResponse;
import com.abc.complaints.service.SessionService;
import com.abc.complaints.service.TotpService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

/**
 * This API is for verifying user's account registration, using a one-time code.
 * If the code is valid, user will be registered successfully
 * */
@RestController
@RequestMapping("/api/v1/rest/verify")
public class CodeVerifyController extends AbstractAuthenticationController {
    private final Session session;
    private final PersonRepository people;
    private final TotpService totpService;

    public CodeVerifyController(Session session, SessionService sessions, PersonRepository people, TotpService totpService) {
        super(session, sessions);
        this.session = session;
        this.people = people;
        this.totpService = totpService;
    }

    /**
     * @param request, contains the code value to be validated
     *
     * @return registerResponse containing user's main info
     * */
    @PostMapping
    public Object verify(@RequestBody VerifyCodeRequest request, HttpServletRequest httpServletRequest) throws Exception {
        // Validate user can authenticate
        validateUserNotAuthenticated();
        validateRequestParamsNotNull(request);

        if (!validateCode(request.getCode())) {
            throw new AccessDeniedException("Code is not valid");
        }

        // Get pending user from session
        var person = getPendingPerson();

        // Create and authenticate user
        people.save(person);
        authenticateUser(person);

        clearSession();

        // TODO: add welcome email
//        Map<String, String> context = new HashMap<>();
//        communicationService.communicate(person.getEmail(), context, Constants.EMAIL_TEMPLATES.WELCOME);

        return generateResponse(person);
    }

    /**
     * Validate code is not null
     * */
    private void validateRequestParamsNotNull(VerifyCodeRequest request) throws EmptyFieldException {
        validateParamNotNull(request.getCode(), "Code");
    }

    /**
     * Clean any leftover data from session
     * */
    private void clearSession() {
        session.removeAttribute(Constants.Registration.TOTP);
        session.removeAttribute(Constants.Registration.PENDING_CREATION);
    }

    /**
     * Get the pending user from the registration step
     *
     * @throws EntityNotFoundException if you try to verify a code without registering first
     * */
    private Person getPendingPerson() throws EntityNotFoundException {
        Person person = session.getAttribute(Constants.Registration.PENDING_CREATION);
        if (person == null) {
            throw new EntityNotFoundException("couldn't find any person entity pending creation");
        }

        return person;
    }

    /**
     * @param code to be validated.
     * @return true if the code is valid
     *
     * */
    private boolean validateCode(String code) {
        Totp generatedTotp = session.getAttribute(Constants.Registration.TOTP);
        if (generatedTotp == null) {
            return false;
        }

        return totpService.validateCode(generatedTotp, code);
    }

    /**
     * Generate response to be returned to frontend
     * */
    private RegisterResponse generateResponse(Person person) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setEmail(person.getEmail());
        registerResponse.setName(person.getName());
        registerResponse.setRole(person.getRoleType().name());
        return registerResponse;
    }
}
