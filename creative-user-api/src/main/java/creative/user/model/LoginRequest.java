package creative.user.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LoginRequest {
    private String mEmail;
    private String mPassword;

    public LoginRequest() {
        super();
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return mPassword;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
    }

    /**
     * @param pEmail
     *            the email to set
     */
    public void setEmail(final String pEmail) {
        mEmail = pEmail;
    }

    /**
     * @param pPassword
     *            the password to set
     */
    public void setPassword(final String pPassword) {
        mPassword = pPassword;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
