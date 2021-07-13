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

/**
 * This API is to start the registration for a user by collecting information and store
 * it in session until the account is verified
 */
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

    /**
     * Collect user's info and store them in session.
     * Send a verification email with a code to the email address provided
     *
     * @param request, contains the register info( email, name, and password) to register a user
     */
    @PostMapping("/submit")
    public Object register(@RequestBody RegisterRequest request, HttpServletRequest httpServletRequest) throws Exception {
        validateUserNotAuthenticated();
        validateRequestParams(request);

        var person = createPersonEntity(request);

        session.putAttribute(Constants.Registration.PENDING_CREATION, person);

        Totp generatedTotp = totpService.generate(request.getEmail());

        session.putAttribute(Constants.Registration.TOTP, generatedTotp);

        // This is just a workaround, codes shouldn't be printed
        // But due to some security issues with sendGrid credentials, I had to disable the email-sending part
        System.out.println(generatedTotp.getCode());

//        Map<String, String> context = new HashMap<>();
//        context.put("code", Integer.toString(generatedTotp.getCode()));
//        communicationService.communicate(request.getEmail(), context, Constants.EMAIL_TEMPLATES.ACCOUNT_VERIFICATION);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Validate request params aren't null.
     * Validate input fields against injection attacks.
     * And validate the email address
     */
    private void validateRequestParams(RegisterRequest registerRequest) throws Exception {
        validateRequestParamsNotNull(registerRequest);
        inputValidator.validateInput(registerRequest.getName());
        validateEmail(registerRequest.getEmail());
    }

    /**
     * Validate request params aren't null
     */
    private void validateRequestParamsNotNull(RegisterRequest registerRequest) throws EmptyFieldException {
        validateParamNotNull(registerRequest.getEmail(), "Email");
        validateParamNotNull(registerRequest.getName(), "Name");
        validateParamNotNull(registerRequest.getPassword(), "Password");
    }

    /**
     * Make sure email was not used before, and it has correct format
     * */
    private void validateEmail(String email) throws Exception {
        Person person = people.findByEmail(email);
        if (person != null) {
            throw new AlreadyExistsException("User with email " + email + " already exists!"); // return to sign in page; account does not exist
        }

        boolean isValidEmail = emailValidator.isValid(email);
        if (!isValidEmail)
            throw new IllegalArgumentException("Email address is invalid: " + email);
    }

    /**
     * Use the data received, to create a person entity
     * */
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
