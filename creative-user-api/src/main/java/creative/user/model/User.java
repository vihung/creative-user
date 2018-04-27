package creative.user.model;

import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@Entity
@DynamoDBTable(tableName = "User")
public class User {
    public User() {
        super();
    }

    public User(final String pId) {
        super();
        id = pId;
    }

    private String id;
    private String firstName;
    private String lastName;
    private String nickname;
    private String mobile;

    /**
     * @return the id
     */
    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    /**
     * @param pId
     *            the id to set
     */
    public void setId(final String pId) {
        id = pId;
    }

    /**
     * @return the firstName
     */
    @DynamoDBAttribute(attributeName = "firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param pFirstName
     *            the firstName to set
     */
    public void setFirstName(final String pFirstName) {
        firstName = pFirstName;
    }

    /**
     * @return the lastName
     */
    @DynamoDBAttribute(attributeName = "lastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * @param pLastName
     *            the lastName to set
     */
    public void setLastName(final String pLastName) {
        lastName = pLastName;
    }

    /**
     * @return the nickname
     */
    @DynamoDBAttribute(attributeName = "nickname")
    public String getNickname() {
        return nickname;
    }

    /**
     * @param pNickname
     *            the nickname to set
     */
    public void setNickname(final String pNickname) {
        nickname = pNickname;
    }

    /**
     * @return the mobile
     */
    @DynamoDBAttribute(attributeName = "mobile")
    public String getMobile() {
        return mobile;
    }

    /**
     * @param pMobile
     *            the mobile to set
     */
    public void setMobile(final String pMobile) {
        mobile = pMobile;
    }

    @Override
    public int hashCode() {
        if (StringUtils.isEmpty(id))
            return HashCodeBuilder.reflectionHashCode(17, 37, this);
        else
            return id.hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}