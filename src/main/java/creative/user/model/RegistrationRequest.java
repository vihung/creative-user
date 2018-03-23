package creative.user.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RegistrationRequest {

    public RegistrationRequest() {
        super();
    }

    private String mFirstName;
    private String mLastName;
    private String mNickname;
    private String mMobile;
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
     * @return the firstName
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * @param pFirstName
     *            the firstName to set
     */
    public void setFirstName(final String pFirstName) {
        mFirstName = pFirstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * @param pLastName
     *            the lastName to set
     */
    public void setLastName(final String pLastName) {
        mLastName = pLastName;
    }

    /**
     * @return the nickame
     */
    public String getNickname() {
        return mNickname;
    }

    /**
     * @param pNickame
     *            the nickame to set
     */
    public void setNickname(final String pNickame) {
        mNickname = pNickame;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mMobile;
    }

    /**
     * @param pMobile
     *            the mobile to set
     */
    public void setMobile(final String pMobile) {
        mMobile = pMobile;
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
