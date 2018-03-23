package creative.user.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import creative.user.model.AccessToken;

@Repository
public class AccessTokenRepository {
    // Logger for this class
    private static final Log log = LogFactory.getLog(AccessTokenRepository.class);

    @Autowired
    private AmazonDynamoDB client;

    public AccessTokenRepository() {
        super();
    }

    public int count() {
        // return mapper.count(AccessToken.class, null);
        return 0;
    }

    public void delete(final String pAccessTokenId) {
        final AccessToken accessToken = findOne(pAccessTokenId);
        delete(accessToken);

    }

    public void delete(final AccessToken pAccessToken) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.delete(pAccessToken);

    }

    public void delete(final Iterable<? extends AccessToken> pAccessTokens) {
        for (final AccessToken accessToken : pAccessTokens) {
            delete(accessToken);
        }

    }

    public boolean exists(final String pAccessTokenId) {
        // TODO Auto-generated method stub
        return true;
    }

    public List<Map<String, AttributeValue>> findAll() {
        final ScanRequest scanRequest = new ScanRequest().withTableName("AccessToken");
        final ScanResult result = client.scan(scanRequest);
        return result.getItems();

    }

    public Iterable<AccessToken> findAll(final Iterable<String> pIds) {
        final List<AccessToken> accessTokens = new ArrayList<AccessToken>();
        for (final String accessTokenId : pIds) {
            accessTokens.add(findOne(accessTokenId));
        }
        return accessTokens;
    }

    public AccessToken findOne(final String pId) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        log.debug("findOne(): Invoked");
        final AccessToken accessToken = mapper.load(AccessToken.class, pId);
        log.debug("findOne(): Returning accessToken=" + accessToken);
        return accessToken;
    }

    public <S extends AccessToken> S save(final S pAccessToken) {
        final DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(pAccessToken);
        return pAccessToken;
    }

    public <S extends AccessToken> Iterable<S> save(final Iterable<S> pAccessTokens) {
        final List<AccessToken> ids = new ArrayList<AccessToken>();

        for (final AccessToken accessToken : pAccessTokens) {
            ids.add(save(accessToken));
        }
        return null;
    }
}
