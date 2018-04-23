package creative.user.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class UpdateUserRequest {

    private String mFirstName;
    private String mLastName;
    private String mMobile;
    private String mNickname;

    public UpdateUserRequest() {
        super();
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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
