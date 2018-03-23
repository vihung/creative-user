package creative.user.model;

import javax.persistence.Entity;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@Entity
@DynamoDBTable(tableName = "UserCredentials")
public class UserCredentials {
    public UserCredentials() {
        super();
    }


    private String mId;
    private String mEmail;
    private String mHashedPassword;
    // many-to-one
    private String mUserId;

    /**
     * @return the email
     */
    @DynamoDBAttribute
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
    @DynamoDBAttribute
    public String getHashedPassword() {
        return mHashedPassword;
    }

    /**
     * @param pPassword
     *            the password to set
     */
    public void setPassword(final String pPassword) {
        mHashedPassword = DigestUtils.sha256Hex(pPassword);
    }
    /**
     * @param pHashedPassword
     *            the password to set
     */
    public void setHashedPassword(final String pHashedPassword) {
        mHashedPassword = pHashedPassword;
    }

    /**
     * @return the user
     */
    @DynamoDBAttribute
    public String getUserId() {
        return mUserId;
    }

    /**
     * @param pUserId
     *            the user to set
     */
    public void setUserId(final String pUserId) {
        mUserId = pUserId;
    }

    /**
     * @return the id
     */
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return mId;
    }

    /**
     * @param pId
     *            the id to set
     */
    public void setId(final String pId) {
        mId = pId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(17, 37, this);
    }
}
