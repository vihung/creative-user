package creative.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import creative.user.model.UpdateUserRequest;
import creative.user.model.User;
import creative.user.model.UserCredentials;
import creative.user.service.UserService;
import creative.user.validator.UserValidator;

@RestController
@RequestMapping(value = "/api")
public class UserController {
    private static final Log log = LogFactory.getLog(UserController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest mRequest;

    @Autowired
    private HttpServletResponse mResponse;

    @Autowired
    private UserService mUserService;

    @Autowired
    private UserValidator mUserValidator;

    public UserController() {
        super();
    }

    private boolean checkPasswordCorrect(final LoginRequest pLoginRequest) {
        log.debug("checkPasswordCorrect(): Invoked. pLoginRequest=" + pLoginRequest);
        final UserCredentials creds = getUserService().findUserCredentialsByEmail(pLoginRequest.getEmail());
        final String actualHashedPassword = creds.getHashedPassword();
        log.debug("checkPasswordCorrect(): actualHashedPassword=" + actualHashedPassword);
        final String candidateHashedPassword = DigestUtils.sha256Hex(pLoginRequest.getPassword());
        log.debug("checkPasswordCorrect(): candidateHashedPassword=" + candidateHashedPassword);
        return candidateHashedPassword.equals(actualHashedPassword);

    }

    @RequestMapping(value = "/user/current", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getCurrentUser() {
        final User user = (User) mRequest.getAttribute("currentUser");
        if (user == null) {
            final String messageKey = "ERR_NO_CURRENT_USER";
            final String messageValue = messageSource.getMessage(messageKey, null, mRequest.getLocale());
            final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
            return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/current/credentials", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getCurrentUserCredentials() {
        final User user = (User) mRequest.getAttribute("currentUser");
        if (user == null) {
            final String messageKey = "ERR_NO_CURRENT_USER";
            final String messageValue = messageSource.getMessage(messageKey, null, mRequest.getLocale());
            final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
            return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);

        } else {
            final String userId = user.getId();
            final UserCredentials creds = getUserService().findUserCredentialsByUserId(userId);
            return new ResponseEntity(creds, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getUserById(@PathVariable(name = "userId", required = true) final String pUserId) {
        log.debug("getUserById(): pUserId=" + pUserId);
        final User currentUser = (User) mRequest.getAttribute("currentUser");
        log.debug("getUserById(): currentUser.id=" + currentUser.getId());
        if (currentUser != null) if (!currentUser.getId().equals(pUserId)) {
            final String messageKey = "ERR_USER_ACCESS_FORBIDDEN";
            final String messageValue = messageSource.getMessage(messageKey, new Object[] { pUserId }, mRequest.getLocale());
            final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
            return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
        }

        final User user = getUserService().getUser(pUserId);
        if (user == null) {
            final String messageKey = "ERR_USER_NOT_FOUND";
            final String messageValue = messageSource.getMessage(messageKey, new Object[] { pUserId }, mRequest.getLocale());
            final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
            return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity(user, HttpStatus.OK);

    }

    public UserService getUserService() {
        return mUserService;
    }

    public UserValidator getUserValidator() {
        return mUserValidator;
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
        final boolean requestValid = getUserValidator().validateLoginRequest(pLoginRequest);
        if (!requestValid) {
            final String messageKey = "ERR_LOGIN_REQUEST_INVALID";
            final String messageValue = messageSource.getMessage(messageKey, new Object[] {}, mRequest.getLocale());
            final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            final boolean emailExists = getUserService().checkEmailExists(pLoginRequest.getEmail());
            if (!emailExists) {
                log.debug("login(): Email address unknown");
                final String messageKey = "ERR_EMAIL_UNKNOWN";
                final String messageValue = messageSource.getMessage(messageKey, new Object[] {}, mRequest.getLocale());
                final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
                return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            else if (!checkPasswordCorrect(pLoginRequest)) {
                log.debug("login(): Password incorrect");
                final String messageKey = "ERR_PASSWORD_INCORRECT";
                final String messageValue = messageSource.getMessage(messageKey, new Object[] {}, mRequest.getLocale());
                final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
                return new ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED);
            } else {

                try {
                    final UserCredentials creds = getUserService().findUserCredentialsByEmail(pLoginRequest.getEmail());
                    log.debug("login(): creds=" + creds);
                    final String userId = creds.getUserId();
                    final User user = getUserService().getUser(userId);
                    log.debug("login(): user=" + user);

                    final String clientAddress = mRequest.getRemoteAddr();
                    log.debug("login(): clientAddress=" + clientAddress);

                    final String clientHostName = mRequest.getRemoteHost();
                    log.debug("login(): clientHostName=" + clientHostName);

                    // Create Access Token
                    final AccessToken accessToken = getUserService().createAccessToken(user, clientAddress, clientHostName);

                    final String tokenValue = accessToken.getTokenValue();

                    final Cookie cookie = new Cookie("ACCESS_TOKEN", tokenValue);
                    // 7 days in seconds
                    final int maxAge = 7 * 24 * 60 * 60;
                    cookie.setMaxAge(maxAge);
                    cookie.setPath("/");
                    mResponse.addCookie(cookie);

                    return new ResponseEntity(user, HttpStatus.CREATED);
                } catch (final Exception e) {
                    log.error("Error logging in", e);
                    final String messageKey = "ERR_LOGIN";
                    final String messageValue = messageSource.getMessage(messageKey, new Object[] {}, mRequest.getLocale());
                    final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
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
        getUserService().save(accessToken);

        // get cookie
        // set maxage to 0
        final Cookie cookie = new Cookie("ACCESS_TOKEN", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        mResponse.addCookie(cookie);
        return new ResponseEntity(HttpStatus.OK);

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
        final boolean requestValid = getUserValidator().validateRegistrationRequest(pRegistrationRequest);
        if (!requestValid) {
            final String messageKey = "ERR_REGISTRATION_REQUEST_INVALID";
            final String messageValue = messageSource.getMessage(messageKey, new Object[] {}, mRequest.getLocale());
            final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            final String email = pRegistrationRequest.getEmail();
            final boolean emailExists = getUserService().checkEmailExists(email);
            if (emailExists) {
                final String messageKey = "ERR_EMAIL_EXISTS";
                final String messageValue = messageSource.getMessage(messageKey, new Object[] { email }, mRequest.getLocale());
                final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
                return new ResponseEntity(errorResponse, HttpStatus.CONFLICT);
            }
            final String nickname = pRegistrationRequest.getNickname();
            final boolean nicknameExists = getUserService().checkNicknameExists(nickname);
            if (nicknameExists) {
                final String messageKey = "ERR_NICKNAME_EXISTS";
                final String messageValue = messageSource.getMessage(messageKey, new Object[] { nickname }, mRequest.getLocale());
                final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
                return new ResponseEntity(errorResponse, HttpStatus.CONFLICT);
            } else {
                try {
                    User user = new User();
                    user.setFirstName(pRegistrationRequest.getFirstName());
                    user.setLastName(pRegistrationRequest.getLastName());
                    user.setNickname(nickname);
                    user.setMobile(pRegistrationRequest.getMobile());
                    log.debug("registerUser(): user=" + user);

                    user = getUserService().save(user);
                    log.debug("registerUser(): user=" + user);

                    UserCredentials creds = new UserCredentials();
                    creds.setEmail(email);
                    creds.setPassword(pRegistrationRequest.getPassword());
                    creds.setUserId(user.getId());
                    log.debug("registerUser(): creds=" + creds);

                    creds = getUserService().save(creds);
                    log.debug("registerUser(): creds=" + creds);

                    final String clientAddress = mRequest.getRemoteAddr();
                    log.debug("login(): clientAddress=" + clientAddress);

                    final String clientHostName = mRequest.getRemoteHost();
                    log.debug("login(): clientHostName=" + clientHostName);

                    final AccessToken accessToken = getUserService().createAccessToken(user, clientAddress, clientHostName);

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
                    final String messageKey = "ERR_REGISTRATION";
                    final String messageValue = messageSource.getMessage(messageKey, new Object[] {}, mRequest.getLocale());
                    final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
                    return new ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

                }
            }
        }
    }

    public void setUserService(final UserService pUserService) {
        mUserService = pUserService;
    }

    public void setUserValidator(final UserValidator pUserValidator) {
        mUserValidator = pUserValidator;
    }

    @RequestMapping(value = "/user/current", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateCurrentUser(@RequestBody final UpdateUserRequest pUpdateUserRequest) {
        final boolean requestValid = getUserValidator().validateUpdateUserRequest(pUpdateUserRequest);
        if (!requestValid) {
            final String messageKey = "ERR_UPDATE_USER_REQUEST_INVALID";
            final String messageValue = messageSource.getMessage(messageKey, new Object[] {}, mRequest.getLocale());
            final ErrorResponse errorResponse = new ErrorResponse(messageKey, messageValue);
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        } else {
            User user = (User) mRequest.getAttribute("currentUser");
            user.setFirstName(pUpdateUserRequest.getFirstName());
            user.setLastName(pUpdateUserRequest.getLastName());
            user.setNickname(pUpdateUserRequest.getNickname());
            user = getUserService().save(user);
            return new ResponseEntity(user, HttpStatus.OK);
        }
    }

}
