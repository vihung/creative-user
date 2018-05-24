package creative.user.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import creative.user.model.UserCredentials;

@Repository
public class UserCredentialsRepository {
    // Logger for this class
    private static final Log log = LogFactory.getLog(UserCredentialsRepository.class);

    @Autowired
    private AmazonDynamoDB client;

    public UserCredentialsRepository() {
        super();
    }

    public int count() {
        // return mapper.count(UserCredentials.class, null);
        return 0;
    }

    public void delete(final Iterable<? extends UserCredentials> pUserCredentialss) {
        for (final UserCredentials userCredentials : pUserCredentialss) {
            delete(userCredentials);
        }

    }

    public void delete(final String pUserCredentialsId) {
        final UserCredentials userCredentials = findOne(pUserCredentialsId);
        delete(userCredentials);

    }

    public void delete(final UserCredentials pUserCredentials) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.delete(pUserCredentials);

    }

    public boolean exists(final String pUserCredentialsId) {
        // TODO Auto-generated method stub
        return true;
    }

    public List<Map<String, AttributeValue>> findAll() {
        final ScanRequest scanRequest = new ScanRequest().withTableName("UserCredentials");
        final ScanResult result = client.scan(scanRequest);
        return result.getItems();

    }

    public Iterable<UserCredentials> findAll(final Iterable<String> pIds) {
        final List<UserCredentials> userCredentials = new ArrayList<UserCredentials>();
        for (final String userCredentialsId : pIds) {
            userCredentials.add(findOne(userCredentialsId));
        }
        return userCredentials;
    }

    public UserCredentials findByEmail(final String pEmail) {
        log.debug("findByEmail(): Invoked. pEmail=" + pEmail);

        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":email", new AttributeValue().withS(pEmail));

        final DynamoDBQueryExpression<UserCredentials> queryExpression = new DynamoDBQueryExpression<UserCredentials>().withIndexName("EmailIndex")
                .withKeyConditionExpression("email = :email").withExpressionAttributeValues(eav).withConsistentRead(false);

        final List<UserCredentials> userCredentialsList = mapper.query(UserCredentials.class, queryExpression);

        log.debug("findByEmail(): Found " + userCredentialsList.size() + " matching credentials");

        UserCredentials userCredentials;
        if (userCredentialsList.isEmpty()) {
            userCredentials = null;
        } else {
            userCredentials = userCredentialsList.get(0);
        }

        log.debug("findByEmail(): Returning userCredentials=" + userCredentials);
        return userCredentials;
    }

    public UserCredentials findByUserId(final String pUserId) {
        log.debug("findByUserId(): Invoked. pUserId=" + pUserId);

        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":userId", new AttributeValue().withS(pUserId));

        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("userId = :userId").withExpressionAttributeValues(eav);

        log.debug("findByUserId(): scanExpression=" + scanExpression);
        final List<UserCredentials> userCredentialsList = mapper.scan(UserCredentials.class, scanExpression);

        UserCredentials userCredentials;
        if (userCredentialsList.isEmpty()) {
            userCredentials = null;
        } else {
            userCredentials = userCredentialsList.get(0);
        }

        log.debug("findByUserId(): Returning userCredentials=" + userCredentials);
        return userCredentials;
    }

    public UserCredentials findOne(final String pId) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        log.debug("findOne(): Invoked");
        final UserCredentials userCredentials = mapper.load(UserCredentials.class, pId);
        log.debug("findOne(): Returning userCredentials=" + userCredentials);
        return userCredentials;
    }

    public <S extends UserCredentials> Iterable<S> save(final Iterable<S> pUserCredentials) {
        final List<UserCredentials> ids = new ArrayList<UserCredentials>();

        for (final UserCredentials userCredentials : pUserCredentials) {
            ids.add(save(userCredentials));
        }
        return null;
    }

    public <S extends UserCredentials> S save(final S pUserCredentials) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(pUserCredentials);
        return pUserCredentials;
    }

}
