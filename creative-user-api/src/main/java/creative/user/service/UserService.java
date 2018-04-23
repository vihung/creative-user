package creative.user.service;

import creative.user.model.AccessToken;
import creative.user.model.LoginRequest;
import creative.user.model.RegistrationRequest;
import creative.user.model.UpdateUserRequest;
import creative.user.model.User;
import creative.user.model.UserCredentials;

public interface UserService {

    boolean checkEmailExists(String pEmail);

    boolean checkNicknameExists(String pNickname);

    AccessToken createAccessToken(User pUser, String pClientAddress, String pClientHostName);

    User findUserByNickname(String pNickname);

    UserCredentials findUserCredentialsByEmail(String pEmail);

    UserCredentials findUserCredentialsByUserId(String pUserId);

    User getUser(String pUserId);

    AccessToken save(AccessToken pAccessToken);

    User save(User pUser);

    UserCredentials save(UserCredentials pCreds);

    boolean validateLoginRequest(LoginRequest pLoginRequest);

    boolean validateRegistrationRequest(RegistrationRequest pRegistrationRequest);

    boolean validateUpdateUserRequest(UpdateUserRequest pUpdateUserRequest);

}
