/**
 *
 */
package creative.user.ddl.table;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

import creative.user.ddl.DynamoDDLCommand;

/**
 * @author Vihung Marathe
 *
 */
public class UserCredentialsDDL extends DynamoDDLCommand {
    private static final String KEY_NAME = "id";

    private static final String KEY_TYPE = "S";

    // Logger for this class
    private static final Log log = LogFactory.getLog(UserCredentialsDDL.class);

    private static final String TABLE_NAME = "UserCredentials";

    public UserCredentialsDDL() {
        super();
    }

    @Override
    public void doClean() {
        log.debug("clean(): Invoked");

        final DeleteTableRequest deleteTableRequest = new DeleteTableRequest(TABLE_NAME);
        client.deleteTable(deleteTableRequest);
        log.debug("clean(): Done");
    }

    @Override
    public void doRun() {
        log.debug("run(): Invoked");

        final ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
        tableKeySchema.add(new KeySchemaElement().withAttributeName(KEY_NAME).withKeyType(KeyType.HASH));

        final ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
        indexKeySchema.add(new KeySchemaElement().withAttributeName("email").withKeyType(KeyType.HASH));

        final ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName(KEY_NAME).withAttributeType(KEY_TYPE));
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("email").withAttributeType("S"));

        final GlobalSecondaryIndex emailIndex = new GlobalSecondaryIndex().withIndexName("EmailIndex")
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(1L))
                .withProjection(new Projection().withProjectionType(ProjectionType.ALL)).withKeySchema(indexKeySchema);

        final CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(TABLE_NAME).withKeySchema(tableKeySchema)
                .withGlobalSecondaryIndexes(emailIndex).withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(5L));

        client.createTable(createTableRequest);

        log.debug("run(): Done");
    }

    @Override
    public final String getName() {
        return TABLE_NAME;
    }

}
