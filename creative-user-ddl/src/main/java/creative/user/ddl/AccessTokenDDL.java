/**
 *
 */
package creative.user.ddl;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

/**
 * @author Vihung Marathe
 *
 */
public class AccessTokenDDL extends DynamoDDLCommand {
    private static final String KEY_NAME = "tokenValue";

    private static final String KEY_TYPE = "S";

    // Logger for this class
    private static final Log log = LogFactory.getLog(AccessTokenDDL.class);

    private static final String TABLE_NAME = "AccessToken";

    public AccessTokenDDL() {
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

        final ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName(KEY_NAME).withAttributeType(KEY_TYPE));

        final CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(TABLE_NAME).withKeySchema(tableKeySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(10L).withWriteCapacityUnits(5L));

        client.createTable(createTableRequest);

        log.debug("run(): Done");
    }

    @Override
    public final String getName() {
        return TABLE_NAME;
    }

}
