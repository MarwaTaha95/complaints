package com.abc.complaints.controller.authentication;

import com.abc.complaints.Constants;
import com.abc.complaints.entity.Person;
import com.abc.complaints.entity.RoleType;
import com.abc.complaints.entity.Session;
import com.abc.complaints.entity.Totp;
import com.abc.complaints.exception.entity.AlreadyExistsException;
import com.abc.complaints.exception.entity.EmptyFieldException;
import com.abc.complaints.repository.PersonRepository;
import com.abc.complaints.rest.request.RegisterRequest;
import com.abc.complaints.service.CommunicationService;
import com.abc.complaints.service.SessionService;
import com.abc.complaints.service.TotpService;
import com.abc.complaints.validator.EmailValidator;
import com.abc.complaints.validator.InputValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rest/register")
public class RegistrationController extends AbstractAuthenticationController {
    private final Session session;
    private final PersonRepository people;
    private final TotpService totpService;
    private final CommunicationService communicationService;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final InputValidator inputValidator;

    public RegistrationController(Session session, SessionService sessions, PersonRepository people, TotpService totpService, CommunicationService communicationService, PasswordEncoder passwordEncoder, EmailValidator emailValidator, InputValidator inputValidator) {
        super(session, sessions);
        this.session = session;
        this.people = people;
        this.totpService = totpService;
        this.communicationService = communicationService;
        this.passwordEncoder = passwordEncoder;
        this.emailValidator = emailValidator;
        this.inputValidator = inputValidator;
    }

    @PostMapping("/submit")
    public Object register(@RequestBody RegisterRequest request, HttpServletRequest httpServletRequest) throws Exception {
        validateUserNotAuthenticated();
        validateRequestParams(request);
        validateEmail(request.getEmail());

        var person = createPersonEntity(request);

        session.putAttribute(Constants.Registration.PENDING_CREATION, person);

        Totp generatedTotp = totpService.generate(request.getEmail());

        System.out.println(generatedTotp.getCode());
        session.putAttribute(Constants.Registration.TOTP, generatedTotp);

        Map<String, String> context = new HashMap<>();
        context.put("code", Integer.toString(generatedTotp.getCode()));
//        communicationService.communicate(request.getEmail(), context, Constants.EMAIL_TEMPLATES.ACCOUNT_VERIFICATION);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void validateRequestParams(RegisterRequest registerRequest) throws EmptyFieldException {
        validateRequestParamsNotNull(registerRequest);
        inputValidator.validateInput(registerRequest.getName());
    }

    private void validateRequestParamsNotNull(RegisterRequest registerRequest) throws EmptyFieldException {
        validateParamNotNull(registerRequest.getEmail(), "Email");
        validateParamNotNull(registerRequest.getName(), "Name");
        validateParamNotNull(registerRequest.getPassword(), "Password");
    }

    private void validateEmail(String email) throws Exception {
        Person person = people.findByEmail(email);
        if (person != null) {
            throw new AlreadyExistsException("User with email " + email + " already exists!"); // return to sign in page; account does not exist
        }

        boolean isValidEmail = emailValidator.isValid(email);
        if (!isValidEmail)
            throw new IllegalArgumentException("Email address is invalid: " + email);
    }

    private Person createPersonEntity(RegisterRequest registerRequest) {
        Person person = new Person();
        person.setName(registerRequest.getName());
        person.setEmail(registerRequest.getEmail());

        if (registerRequest.isAdmin())
            person.setRoleType(RoleType.ADMIN);

        var encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        person.setPassword(encodedPassword);

        return person;
    }
}
