package creative.user.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class LoginRequest {

    public LoginRequest() {
        super();
    }

    private String mEmail;
    private String mPassword;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
    }
    /**
     * @return the email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * @param pEmail
     *            the email to set
     */
    public void setEmail(final String pEmail) {
        mEmail = pEmail;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * @param pPassword
     *            the password to set
     */
    public void setPassword(final String pPassword) {
        mPassword = pPassword;
    }

}
