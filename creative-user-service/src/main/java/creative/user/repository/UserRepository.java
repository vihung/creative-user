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
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import creative.user.model.User;

@Repository
public class UserRepository {
    // Logger for this class
    private static final Log log = LogFactory.getLog(UserRepository.class);

    @Autowired
    private AmazonDynamoDB client;

    public UserRepository() {
        super();
    }

    public int count() {
        // return mapper.count(User.class, null);
        return 0;
    }

    public void delete(final Iterable<? extends User> pUsers) {
        for (final User user : pUsers) {
            delete(user);
        }

    }

    public void delete(final String pUserId) {
        final User user = findOne(pUserId);
        delete(user);

    }

    public void delete(final User pUser) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.delete(pUser);

    }

    public boolean exists(final String pUserId) {
        // TODO Auto-generated method stub
        return true;
    }

    public List<Map<String, AttributeValue>> findAll() {
        final ScanRequest scanRequest = new ScanRequest().withTableName("User");
        final ScanResult result = client.scan(scanRequest);
        return result.getItems();

    }

    public Iterable<User> findAll(final Iterable<String> pIds) {
        final List<User> users = new ArrayList<User>();
        for (final String userId : pIds) {
            users.add(findOne(userId));
        }
        return users;
    }

    public User findByNickname(final String pNickname) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        log.debug("findByNickname(): Invoked. pNickname=" + pNickname);
        final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":nickname", new AttributeValue().withS(pNickname));

        // // TODO: replace with query instead of scan
        // final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("nickname = :nickname")
        // .withExpressionAttributeValues(eav);
        //
        // log.debug("findByNickname(): scanExpression=" + scanExpression);
        // List<User> users = mapper.scan(User.class, scanExpression);

        final DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>().withIndexName("NicknameIndex")
                .withKeyConditionExpression("nickname = :nickname").withExpressionAttributeValues(eav).withConsistentRead(false);

        log.debug("findByNickname(): queryExpression=" + queryExpression);
        final List<User> users = mapper.query(User.class, queryExpression);

        log.debug("findByNickname(): Found " + users.size() + " matching users for nickname '" + pNickname + "'");

        User user;
        if (users.isEmpty()) {
            user = null;
        } else {
            user = users.get(0);
        }

        log.debug("findByNickname(): Returning user=" + user);
        return user;
    }

    public User findOne(final String pId) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        log.debug("findOne(): Invoked");
        final User user = mapper.load(User.class, pId);
        log.debug("findOne(): Returning user=" + user);
        return user;
    }

    public <S extends User> Iterable<S> save(final Iterable<S> pUsers) {
        final List<User> ids = new ArrayList<User>();

        for (final User user : pUsers) {
            ids.add(save(user));
        }
        return null;
    }

    public <S extends User> S save(final S pUser) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(pUser);
        return pUser;
    }

}
