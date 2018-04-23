/**
 *
 */
package creative.user.ddl;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

/**
 * @author Vihung Marathe
 *
 */
public class DynamoDBCreate {
    // Logger for this class
    private static final Log log = LogFactory.getLog(DynamoDBCreate.class);

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final DynamoDBCreate create = new DynamoDBCreate();
        try {
            create.run();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

    }

    private final DynamoDB dynamoDB;

    public DynamoDBCreate() {
        final AmazonDynamoDB client = new AmazonDynamoDBClient(new BasicAWSCredentials("key", "key2"));
        client.setEndpoint("http://localhost:8000/");
        dynamoDB = new DynamoDB(client);
    }

    private void createTable(final String pTableName, final String pKeyName, final String pKeyType) throws InterruptedException {
        final ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName(pKeyName).withKeyType(KeyType.HASH));
        final ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName(pKeyName).withAttributeType(pKeyType));
        final CreateTableRequest request = new CreateTableRequest().withTableName(pTableName).withKeySchema(keySchema)
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(5L));
        request.setAttributeDefinitions(attributeDefinitions);

        final Table tableToDelete = dynamoDB.getTable(pTableName);
        if (tableToDelete != null) {
            try {
                tableToDelete.delete();
                tableToDelete.waitForDelete();
                log.debug("createTable(): Deleted table '" + pTableName + "'");
            } catch (final Exception e) {
                log.error("Error deleting '" + pTableName + "'", e);
            }
        }

        final Table table = dynamoDB.createTable(request);
        log.debug("createTable(): Table '" + pTableName + "' created. Waiting for activation...");
        table.waitForActive();
    }

    public void run() throws InterruptedException {
        log.debug("run(): Invoked");
        createTable("User", "id", "S");
        createTable("UserCredentials", "id", "S");
        createTable("AccessToken", "tokenValue", "S");
        log.debug("run(): Done");
    }

}
