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

    @PostMapping
    public Object verify(@RequestBody VerifyCodeRequest request, HttpServletRequest httpServletRequest) throws Exception {
        validateUserNotAuthenticated();
        validateRequestParamsNotNull(request);

        if (!validateCode(request.getCode())) {
            throw new AccessDeniedException("Code is not valid");
        }

        var person = getPendingPerson();

        people.save(person);
        authenticateUser(person);

        clearSession();

        // TODO: add welcome email
//        Map<String, String> context = new HashMap<>();
//        communicationService.communicate(person.getEmail(), context, Constants.EMAIL_TEMPLATES.WELCOME);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setEmail(person.getEmail());
        registerResponse.setName(person.getName());
        registerResponse.setRole(person.getRoleType().name());
        return registerResponse;
    }

    private void validateRequestParamsNotNull(VerifyCodeRequest request) throws EmptyFieldException {
        validateParamNotNull(request.getCode(), "Code");
    }

    private void clearSession() {
        session.removeAttribute(Constants.Registration.TOTP);
        session.removeAttribute(Constants.Registration.PENDING_CREATION);
    }

    private Person getPendingPerson() throws EntityNotFoundException {
        Person person = session.getAttribute(Constants.Registration.PENDING_CREATION);
        if (person == null) {
            throw new EntityNotFoundException("couldn't find any person entity pending creation");
        }

        return person;
    }

    private boolean validateCode(String code) {
        Totp generatedTotp = session.getAttribute(Constants.Registration.TOTP);
        if (generatedTotp == null) {
            return false;
        }

        return totpService.validateCode(generatedTotp, code);
    }
}
