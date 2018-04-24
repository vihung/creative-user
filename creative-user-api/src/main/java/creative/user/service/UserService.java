package creative.user.service;

import creative.user.model.AccessToken;
import creative.user.model.LoginRequest;
import creative.user.model.RegistrationRequest;
import creative.user.model.UpdateUserRequest;
import creative.user.model.User;
import creative.user.model.UserCredentials;

public interface UserService {
    /**
     * Create an {@link AccessToken} for the given user at the given client host and address.
     *
     * @param pUser
     *            the user
     * @param pClientAddress
     *            the client IP address
     * @param pClientHostName
     *            the client hostname
     * @return the {@link AccessToken}
     */
    public AccessToken createAccessToken(User pUser, String pClientAddress, String pClientHostName);

    /**
     * Find the {@link UserCredentials} for a given user, by user id.
     *
     * @param pUserId
     *            the user id
     * @return {@link UserCredentials, or <code>null</code> if not found
     */
    public UserCredentials findUserCredentialsByUserId(String pUserId);

    public User getUser(String pUserId);

    public User login(LoginRequest pLoginRequest) throws InvalidLoginRequestException, UnknownEmailException, IncorrectPasswordException;

    public void logout(AccessToken pAccessToken);

    public User registerUser(RegistrationRequest pRegistrationRequest)
            throws InvalidRegistrationRequestException, DuplicateEmailException, DuplicateNicknameException;

    public User updateCurrentUser(User pUser, UpdateUserRequest pUpdateUserRequest) throws InvalidUpdateUserRequestException;
}
