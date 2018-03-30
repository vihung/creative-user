package creative.user.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class RegistrationRequest {

    private String mEmail;

    private String mFirstName;
    private String mLastName;
    private String mMobile;
    private String mNickname;
    private String mPassword;

    public RegistrationRequest() {
        super();
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mMobile;
    }

    /**
     * @return the nickame
     */
    public String getNickname() {
        return mNickname;
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
     * @param pFirstName
     *            the firstName to set
     */
    public void setFirstName(final String pFirstName) {
        mFirstName = pFirstName;
    }

    /**
     * @param pLastName
     *            the lastName to set
     */
    public void setLastName(final String pLastName) {
        mLastName = pLastName;
    }

    /**
     * @param pMobile
     *            the mobile to set
     */
    public void setMobile(final String pMobile) {
        mMobile = pMobile;
    }

    /**
     * @param pNickame
     *            the nickame to set
     */
    public void setNickname(final String pNickame) {
        mNickname = pNickame;
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
