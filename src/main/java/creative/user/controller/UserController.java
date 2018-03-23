package creative.user.controller;

import java.util.Calendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import creative.user.model.AccessToken;
import creative.user.model.ErrorResponse;
import creative.user.model.LoginRequest;
import creative.user.model.RegistrationRequest;
import creative.user.model.User;
import creative.user.model.UserCredentials;
import creative.user.repository.AccessTokenRepository;
import creative.user.repository.UserCredentialsRepository;
import creative.user.repository.UserRepository;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    private static final Log log = LogFactory.getLog(UserController.class);

    @Autowired
    private UserRepository mUserRepository;

    @Autowired
    private UserCredentialsRepository mUserCredentialsRepository;

    @Autowired
    private AccessTokenRepository mAccessTokenRepository;

    @Autowired
    private HttpServletRequest mRequest;

    @Autowired
    private HttpServletResponse mResponse;

    public UserController() {
        super();
    }

    /**
     * Register User. Checks whether user already exists with given email or nickname, and registers otherwise.
     *
     * @param pRegistrationRequest
     *            the registration request (user details + login credentials)
     * @return sets access token cookie (effectively, logging newly registered user in) and returns a 201 CREATED response with user details in the body if
     *         successful. Returns a 400 BAD REQUEST or 409 CONFLICT response with error details if not successful, or a 500 response on any other internal
     *         error
     */
    @RequestMapping(value = "/user/new", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity registerUser(@RequestBody final RegistrationRequest pRegistrationRequest) {
        log.debug("registerUser(): Invoked");
        log.debug("registerUser(): pRegistrationRequest=" + pRegistrationRequest);
        final boolean requestValid = validateRegistrationRequest(pRegistrationRequest);
        if (!requestValid) {
            final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Registration request invalid. Check all fields and try again");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            final boolean emailExists = checkEmailExists(pRegistrationRequest.getEmail());
            final boolean nicknameExists = checkNicknameExists(pRegistrationRequest.getNickname());
            if (emailExists) {
                final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(),
                        "A user with this email address already exists. If you are already registered, then please sign in");
                return new ResponseEntity(errorResponse, HttpStatus.CONFLICT);
            } else if (nicknameExists) {
                final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(),
                        "A user with this nickname already exists. Please change the nickname and try again");
                return new ResponseEntity(errorResponse, HttpStatus.CONFLICT);
            } else {
                try {
                    User user = new User();
                    user.setFirstName(pRegistrationRequest.getFirstName());
                    user.setLastName(pRegistrationRequest.getLastName());
                    user.setNickname(pRegistrationRequest.getNickname());
                    user.setMobile(pRegistrationRequest.getMobile());
                    log.debug("registerUser(): user=" + user);

                    user = getUserRepository().save(user);
                    log.debug("registerUser(): user=" + user);

                    UserCredentials creds = new UserCredentials();
                    creds.setEmail(pRegistrationRequest.getEmail());
                    creds.setPassword(pRegistrationRequest.getPassword());
                    creds.setUserId(user.getId());
                    log.debug("registerUser(): creds=" + creds);

                    creds = getUserCredentialsRepository().save(creds);
                    log.debug("registerUser(): creds=" + creds);

                    final AccessToken accessToken = new AccessToken(user.getId());
                    final Calendar expiry = Calendar.getInstance();
                    log.debug("registerUser(): now=" + expiry);
                    expiry.add(Calendar.DAY_OF_MONTH, 7);
                    log.debug("registerUser(): expiry=" + expiry);

                    accessToken.setExpiryTimestamp(expiry.getTimeInMillis());

                    final String clientAddress = mRequest.getRemoteAddr();
                    log.debug("login(): clientAddress=" + clientAddress);
                    accessToken.setClientAddress(clientAddress);
                    final String clientHostName = mRequest.getRemoteHost();
                    log.debug("login(): clientHostName=" + clientHostName);
                    accessToken.setClientHostname(clientHostName);

                    getAccessTokenRepository().save(accessToken);

                    final String tokenValue = accessToken.getTokenValue();

                    final Cookie cookie = new Cookie("ACCESS_TOKEN", tokenValue);
                    // 7 days in seconds
                    final int maxAge = 7 * 24 * 60 * 60;
                    cookie.setMaxAge(maxAge);
                    cookie.setPath("/");
                    mResponse.addCookie(cookie);

                    return new ResponseEntity(user, HttpStatus.CREATED);
                } catch (final Exception e) {
                    log.error("Error registering user", e);
                    final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
                    return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

                }
            }
        }
    }

    /**
     * Register User. Checks whether user already exists with given email or nickname, and registers otherwise.
     *
     * @param pLoginRequest
     *            the login request (login credentials)
     * @return sets access token cookie (effectively, logging newly registered user in) and returns a 201 CREATED response with user details in the body if
     *         successful. Returns a 400 BAD REQUEST or 409 CONFLICT response with error details if not successful, or a 500 response on any other internal
     *         error
     */
    @RequestMapping(value = "/session/new", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity login(@RequestBody final LoginRequest pLoginRequest) {
        log.debug("login(): Invoked");
        log.debug("login(): pLoginRequest=" + pLoginRequest);
        final boolean requestValid = validateLoginRequest(pLoginRequest);
        if (!requestValid) {
            final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Login request invalid. Check all fields and try again");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            final boolean emailExists = checkEmailExists(pLoginRequest.getEmail());
            if (!emailExists) {
                log.debug("login(): Email address unknown");
                final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Email address unknown");
                return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            else if (!checkPasswordCorrect(pLoginRequest)) {
                log.debug("login(): Password incorrect");
                final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Password incorrect");
                return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
            } else {

                try {
                    // TODO: Load User by UserId
                    final UserCredentials creds = getUserCredentialsRepository().findByEmail(pLoginRequest.getEmail());
                    log.debug("login(): creds=" + creds);
                    final String userId = creds.getUserId();
                    final User user = getUserRepository().findOne(userId);
                    log.debug("login(): user=" + user);
                    // Create Access Token
                    final AccessToken accessToken = new AccessToken(user.getId());
                    final Calendar expiry = Calendar.getInstance();
                    log.debug("registerUser(): now=" + expiry);
                    expiry.add(Calendar.DAY_OF_MONTH, 7);
                    log.debug("registerUser(): expiry=" + expiry);

                    accessToken.setExpiryTimestamp(expiry.getTimeInMillis());

                    final String clientAddress = mRequest.getRemoteAddr();
                    log.debug("login(): clientAddress=" + clientAddress);
                    accessToken.setClientAddress(clientAddress);
                    final String clientHostName = mRequest.getRemoteHost();
                    log.debug("login(): clientHostName=" + clientHostName);
                    accessToken.setClientHostname(clientHostName);
                    log.debug("login(): accessToken=" + accessToken);
                    getAccessTokenRepository().save(accessToken);

                    final String tokenValue = accessToken.getTokenValue();

                    final Cookie cookie = new Cookie("ACCESS_TOKEN", tokenValue);
                    // 7 days in seconds
                    final int maxAge = 7 * 24 * 60 * 60;
                    cookie.setMaxAge(maxAge);
                    cookie.setPath("/");
                    mResponse.addCookie(cookie);

                    return new ResponseEntity(user, HttpStatus.CREATED);
                } catch (final Exception e) {
                    log.error("Error registering user", e);
                    final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
                    return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

                }
            }
        }
    }

    @RequestMapping(value = "/session/current", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity logout() {
        // get access token from cookie
        final AccessToken accessToken = (AccessToken) mRequest.getAttribute("accessToken");
        // load from repository
        // set expiry to now
        accessToken.setExpiryTimestamp(System.currentTimeMillis());
        // save
        getAccessTokenRepository().save(accessToken);

        // get cookie
        // set maxage to 0
        final Cookie cookie = new Cookie("ACCESS_TOKEN", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        mResponse.addCookie(cookie);
        return new ResponseEntity(HttpStatus.OK);

    }

    @RequestMapping(value = "/user/current", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getCurrentUser() {
        final User user = (User) mRequest.getAttribute("currentUser");
        if (user != null)
            return new ResponseEntity(user, HttpStatus.OK);
        else {
            final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "No current user found");
            return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);

        }

    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserById(@PathVariable(name = "userId", required = true) final String pUserId) {
        log.debug("getUserById(): pUserId=" + pUserId);
        final User currentUser = (User) mRequest.getAttribute("currentUser");
        log.debug("getUserById(): currentUser.id=" + currentUser.getId());
        if (currentUser != null)
            if (!currentUser.getId().equals(pUserId)) {
                final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Current user cannot access user with id " + pUserId);
                return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
            }

        final User user = getUserRepository().findOne(pUserId);
        if (user != null)
            return new ResponseEntity(user, HttpStatus.OK);
        else {
            final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "No user found for id " + pUserId);
            return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
        }

    }

    private final boolean checkNicknameExists(final String pNickname) {
        log.debug("checkNicknameExists(): Invoked. pNickname=" + pNickname);
        if (null == getUserRepository().findByNickname(pNickname))
            return false;
        else
            return true;
    }

    private final boolean checkEmailExists(final String pEmail) {
        log.debug("checkEmailExists(): Invoked. pEmail=" + pEmail);
        if (null == getUserCredentialsRepository().findByEmail(pEmail))
            return false;
        else
            return true;
    }

    private boolean checkPasswordCorrect(final LoginRequest pLoginRequest) {
        log.debug("checkPasswordCorrect(): Invoked. pLoginRequest=" + pLoginRequest);
        final UserCredentials creds = getUserCredentialsRepository().findByEmail(pLoginRequest.getEmail());
        final String actualHashedPassword = creds.getHashedPassword();
        log.debug("checkPasswordCorrect(): actualHashedPassword=" + actualHashedPassword);
        final String candidateHashedPassword = DigestUtils.sha256Hex(pLoginRequest.getPassword());
        log.debug("checkPasswordCorrect(): candidateHashedPassword=" + candidateHashedPassword);
        return candidateHashedPassword.equals(actualHashedPassword);

    }

    private final boolean validateLoginRequest(final LoginRequest pLoginRequest) {
        log.debug("validateLoginRequest(): Invoked");
        boolean requestValid = true;

        if (!validateStringField(pLoginRequest.getEmail())) {
            requestValid = false;
        } else if (!validateStringField(pLoginRequest.getPassword())) {
            requestValid = false;
        }

        log.debug("validateLoginRequest(): Returning requestValid=" + requestValid);
        return requestValid;
    }

    private final boolean validateRegistrationRequest(final RegistrationRequest pRegistrationRequest) {
        log.debug("validateRegistrationRequest(): Invoked");
        boolean requestValid = true;

        if (!validateStringField(pRegistrationRequest.getFirstName())) {
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getLastName())) {
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getNickname())) {
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getMobile())) {
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getEmail())) {
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getPassword())) {
            requestValid = false;
        }

        log.debug("validateRegistrationRequest(): Returning requestValid=" + requestValid);
        return requestValid;
    }

    private boolean validateStringField(final String pStringValue) {
        log.debug("validateStringField(): Invoked");
        log.debug("validateStringField(): pStringValue=" + pStringValue);

        if (StringUtils.isEmpty(pStringValue)) {
            log.warn("validateStringField(): Empty");
            return false;

        } else if (pStringValue.length() > 40) {
            log.warn("validateStringField(): Too long");
            return false;
        }
        return true;
    }

    /**
     * @return the userService
     */
    public UserRepository getUserRepository() {
        return mUserRepository;
    }

    /**
     * @param pUserRepository
     *            the userService to set
     */
    public void setUserRepository(final UserRepository pUserRepository) {
        mUserRepository = pUserRepository;
    }

    /**
     * @return the userCredentialsRepository
     */
    public UserCredentialsRepository getUserCredentialsRepository() {
        return mUserCredentialsRepository;
    }

    /**
     * @param pUserCredentialsRepository
     *            the userCredentialsRepository to set
     */
    public void setUserCredentialsRepository(final UserCredentialsRepository pUserCredentialsRepository) {
        mUserCredentialsRepository = pUserCredentialsRepository;
    }

    /**
     * @return the accessTokenRepository
     */
    public AccessTokenRepository getAccessTokenRepository() {
        return mAccessTokenRepository;
    }

    /**
     * @param pAccessTokenRepository
     *            the accessTokenRepository to set
     */
    public void setAccessTokenRepository(final AccessTokenRepository pAccessTokenRepository) {
        mAccessTokenRepository = pAccessTokenRepository;
    }
}
