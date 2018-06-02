/**
 *
 */
package creative.user.validator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    // Logger for this class
    private static final Log log = LogFactory.getLog(UserValidator.class);

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
            log.debug("validateRegistrationRequest(): No First Name");
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getLastName())) {
            log.debug("validateRegistrationRequest(): No Last Name");
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getNickname())) {
            log.debug("validateRegistrationRequest(): No Nickname");
            requestValid = false;
            // } else if (!validateStringField(pRegistrationRequest.getMobile())) {
            // requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getEmail())) {
            log.debug("validateRegistrationRequest(): No Email");
            requestValid = false;
        } else if (!validateStringField(pRegistrationRequest.getPassword())) {
            log.debug("validateRegistrationRequest(): No Password");
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
        } else if (!validateStringField(pUpdateUserRequest.getMobile())) {
            requestValid = false;
        }

        return requestValid;
    }

}
