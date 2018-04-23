/**
 *
 */
package creative.user.service.impl;

import java.util.Calendar;

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
import creative.user.service.UserService;
import creative.user.validator.UserValidator;

/**
 * @author vihung
 *
 */
@Service
public class UserServiceImpl implements UserService {

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

    @Override
    public boolean checkEmailExists(final String pEmail) {
        if (null == findUserCredentialsByEmail(pEmail)) return false;
        else
            return true;
    }

    @Override
    public boolean checkNicknameExists(final String pNickname) {
        if (null == findUserByNickname(pNickname)) return false;
        else
            return true;
    }

    @Override
    public AccessToken createAccessToken(final User mUser, final String mClientAddress, final String mClientHostName) {
        final AccessToken accessToken = new AccessToken(mUser.getId());
        final Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.DAY_OF_MONTH, 7);

        accessToken.setExpiryTimestamp(expiry.getTimeInMillis());

        accessToken.setClientAddress(mClientAddress);
        accessToken.setClientHostname(mClientHostName);
        getAccessTokenRepository().save(accessToken);
        return accessToken;
    }

    @Override
    public User findUserByNickname(final String pNickname) {
        return getUserRepository().findByNickname(pNickname);
    }

    @Override
    public UserCredentials findUserCredentialsByEmail(final String pEmail) {
        return getUserCredentialsRepository().findByEmail(pEmail);
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
    public AccessToken save(final AccessToken pAccessToken) {
        return getAccessTokenRepository().save(pAccessToken);
    }

    @Override
    public User save(final User pUser) {
        return getUserRepository().save(pUser);
    }

    @Override
    public UserCredentials save(final UserCredentials pCreds) {
        return getUserCredentialsRepository().save(pCreds);
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
    public boolean validateLoginRequest(final LoginRequest pLoginRequest) {
        return getUserValidator().validateLoginRequest(pLoginRequest);
    }

    @Override
    public boolean validateRegistrationRequest(final RegistrationRequest pRegistrationRequest) {
        return getUserValidator().validateRegistrationRequest(pRegistrationRequest);
    }

    @Override
    public boolean validateUpdateUserRequest(final UpdateUserRequest pUpdateUserRequest) {
        return getUserValidator().validateUpdateUserRequest(pUpdateUserRequest);
    }

}
