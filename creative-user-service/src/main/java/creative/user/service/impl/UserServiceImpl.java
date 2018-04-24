/**
 *
 */
package creative.user.service.impl;

import java.util.Calendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import creative.user.model.AccessToken;
import creative.user.model.LoginRequest;
import creative.user.model.RegistrationRequest;
import creative.user.model.UpdateUserRequest;
import creative.user.model.User;
import creative.user.model.UserCredentials;
import creative.user.repository.AccessTokenRepository;
import creative.user.repository.UserCredentialsRepository;
import creative.user.repository.UserRepository;
import creative.user.service.DuplicateEmailException;
import creative.user.service.DuplicateNicknameException;
import creative.user.service.IncorrectPasswordException;
import creative.user.service.InvalidLoginRequestException;
import creative.user.service.InvalidRegistrationRequestException;
import creative.user.service.InvalidUpdateUserRequestException;
import creative.user.service.UnknownEmailException;
import creative.user.service.UserService;
import creative.user.validator.UserValidator;

/**
 * @author vihung
 *
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Log log = LogFactory.getLog(UserService.class);

    @Autowired
    private AccessTokenRepository mAccessTokenRepository;

    @Autowired
    private UserCredentialsRepository mUserCredentialsRepository;

    @Autowired
    private UserRepository mUserRepository;

    @Autowired
    private UserValidator mUserValidator;

    /**
     *
     */
    public UserServiceImpl() {
        super();
    }

    private boolean checkEmailExists(final String pEmail) {
        if (null == getUserCredentialsRepository().findByEmail(pEmail)) return false;
        else
            return true;
    }

    private boolean checkNicknameExists(final String pNickname) {
        if (null == getUserRepository().findByNickname(pNickname)) return false;
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

    @Override
    public AccessToken createAccessToken(final User pUser, final String pClientAddress, final String pClientHostName) {
        final AccessToken accessToken = new AccessToken(pUser.getId());
        final Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.DAY_OF_MONTH, 7);

        accessToken.setExpiryTimestamp(expiry.getTimeInMillis());

        accessToken.setClientAddress(pClientAddress);
        accessToken.setClientHostname(pClientHostName);
        getAccessTokenRepository().save(accessToken);
        return accessToken;
    }

    @Override
    public UserCredentials findUserCredentialsByUserId(final String pUserId) {
        return getUserCredentialsRepository().findByUserId(pUserId);
    }

    /**
     * @return the accessTokenRepository
     */
    public AccessTokenRepository getAccessTokenRepository() {
        return mAccessTokenRepository;
    }

    @Override
    public User getUser(final String pUserId) {
        return getUserRepository().findOne(pUserId);
    }

    public UserCredentialsRepository getUserCredentialsRepository() {
        return mUserCredentialsRepository;
    }

    public UserRepository getUserRepository() {
        return mUserRepository;
    }

    public UserValidator getUserValidator() {
        return mUserValidator;
    }

    @Override
    public User login(final LoginRequest pLoginRequest) throws InvalidLoginRequestException, UnknownEmailException, IncorrectPasswordException {
        final boolean requestValid = getUserValidator().validateLoginRequest(pLoginRequest);
        if (!requestValid) throw new InvalidLoginRequestException();
        final boolean emailExists = checkEmailExists(pLoginRequest.getEmail());
        if (!emailExists) throw new UnknownEmailException();
        else if (!checkPasswordCorrect(pLoginRequest)) throw new IncorrectPasswordException();
        else {
            final UserCredentials creds = getUserCredentialsRepository().findByEmail(pLoginRequest.getEmail());
            log.debug("login(): creds=" + creds);
            final String userId = creds.getUserId();
            final User user = getUser(userId);
            log.debug("login(): user=" + user);
            return user;
        }

    }

    @Override
    public void logout(final AccessToken pAccessToken) {

        pAccessToken.setExpiryTimestamp(System.currentTimeMillis());

        getAccessTokenRepository().save(pAccessToken);
    }

    @Override
    public User registerUser(final RegistrationRequest pRegistrationRequest)
            throws InvalidRegistrationRequestException, DuplicateEmailException, DuplicateNicknameException {
        final boolean requestValid = getUserValidator().validateRegistrationRequest(pRegistrationRequest);
        if (!requestValid) throw new InvalidRegistrationRequestException();

        final String email = pRegistrationRequest.getEmail();
        final boolean emailExists = checkEmailExists(email);
        if (emailExists) throw new DuplicateEmailException();

        final String nickname = pRegistrationRequest.getNickname();
        final boolean nicknameExists = checkNicknameExists(nickname);
        if (nicknameExists) throw new DuplicateNicknameException();

        User user = new User();
        user.setFirstName(pRegistrationRequest.getFirstName());
        user.setLastName(pRegistrationRequest.getLastName());
        user.setNickname(nickname);
        user.setMobile(pRegistrationRequest.getMobile());
        log.debug("registerUser(): user=" + user);
        final User pUser = user;

        user = getUserRepository().save(pUser);
        log.debug("registerUser(): user=" + user);

        UserCredentials creds = new UserCredentials();
        creds.setEmail(email);
        creds.setPassword(pRegistrationRequest.getPassword());
        creds.setUserId(user.getId());
        log.debug("registerUser(): creds=" + creds);
        final UserCredentials pCreds = creds;

        creds = getUserCredentialsRepository().save(pCreds);
        log.debug("registerUser(): creds=" + creds);

        return user;
    }

    /**
     * @param pAccessTokenRepository
     *            the accessTokenRepository to set
     */
    public void setAccessTokenRepository(final AccessTokenRepository pAccessTokenRepository) {
        mAccessTokenRepository = pAccessTokenRepository;
    }

    public void setUserCredentialsRepository(final UserCredentialsRepository pUserCredentialsRepository) {
        mUserCredentialsRepository = pUserCredentialsRepository;
    }

    public void setUserRepository(final UserRepository pUserRepository) {
        mUserRepository = pUserRepository;
    }

    public void setUserValidator(final UserValidator pUserValidator) {
        mUserValidator = pUserValidator;
    }

    @Override
    public User updateCurrentUser(User pUser, final UpdateUserRequest pUpdateUserRequest) throws InvalidUpdateUserRequestException {
        final boolean requestValid = getUserValidator().validateUpdateUserRequest(pUpdateUserRequest);
        if (!requestValid) throw new InvalidUpdateUserRequestException();
        else {
            pUser.setFirstName(pUpdateUserRequest.getFirstName());
            pUser.setLastName(pUpdateUserRequest.getLastName());
            pUser.setNickname(pUpdateUserRequest.getNickname());
            pUser.setMobile(pUpdateUserRequest.getMobile());
            final User pUser1 = pUser;
            pUser = getUserRepository().save(pUser1);
            return pUser;
        }

    }
}