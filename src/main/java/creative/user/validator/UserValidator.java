/**
 *
 */
package creative.user.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import creative.user.model.LoginRequest;
import creative.user.model.RegistrationRequest;
import creative.user.model.UpdateUserRequest;

/**
 * @author vihung
 *
 */
@Service
public class UserValidator {

    /**
     *
     */
    public UserValidator() {
        super();
    }

    public final boolean validateLoginRequest(final LoginRequest pLoginRequest) {
        boolean requestValid = true;

        if (!validateStringField(pLoginRequest.getEmail())) {
            requestValid = false;
        } else if (!validateStringField(pLoginRequest.getPassword())) {
            requestValid = false;
        }

        return requestValid;
    }

    public final boolean validateRegistrationRequest(final RegistrationRequest pRegistrationRequest) {
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

        return requestValid;
    }

    private boolean validateStringField(final String pStringValue) {

        if (StringUtils.isEmpty(pStringValue)) return false;
        else if (pStringValue.length() > 40) return false;
        return true;
    }

    public final boolean validateUpdateUserRequest(final UpdateUserRequest pUpdateUserRequest) {
        boolean requestValid = true;

        if (!validateStringField(pUpdateUserRequest.getFirstName())) {
            requestValid = false;
        } else if (!validateStringField(pUpdateUserRequest.getLastName())) {
            requestValid = false;
        } else if (!validateStringField(pUpdateUserRequest.getNickname())) {
            requestValid = false;
        }

        return requestValid;
    }

}
