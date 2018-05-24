/**
 *
 */
package creative.user.data.bulk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;

import creative.user.data.DynamoBulkImportCommand;

/**
 * @author vihung
 *
 */
public class UserCredentialsData extends DynamoBulkImportCommand {

    private static final String DATA_FILE_NAME = "UserData.csv";

    /**
     * @see creative.user.data.DynamoBulkImportCommand#doClean()
     */
    @Override
    protected void doClean() {

        try {
            final Reader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/UserData.csv")));
            final CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                    .withHeader("Id", "First Name", "Last Name", "Email", "Password").withIgnoreHeaderCase().withTrim());

            final DynamoDB dynamoDB = new DynamoDB(client);
            final Table userCredsTable = dynamoDB.getTable("UserCredentials");

            final List<CSVRecord> csvRecords = csvParser.getRecords();

            for (final CSVRecord record : csvRecords) {
                final String id = record.get("id");
                userCredsTable.deleteItem(new PrimaryKey("id", id));
                System.out.println("Deleted user credentials with id " + id);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    /**
     * @see creative.user.data.DynamoBulkImportCommand#doRun()
     */
    @Override
    protected void doRun() {

        CSVParser csvParser = null;
        try {
            final Reader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/" + DATA_FILE_NAME)));
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withHeader("Id", "First Name", "Last Name", "Email", "Password")
                    .withIgnoreHeaderCase().withTrim());

            final DynamoDB dynamoDB = new DynamoDB(client);
            final Table userTable = dynamoDB.getTable("User");
            final Table userCredsTable = dynamoDB.getTable("UserCredentials");

            final List<CSVRecord> csvRecords = csvParser.getRecords();

            for (final CSVRecord record : csvRecords) {
                Item userCredsItem = new Item();
                final String id = record.get("Id");
                userCredsItem = userCredsItem.withPrimaryKey("id", id);
                userCredsItem = userCredsItem.with("userId", id);

                final String email = record.get("Email");
                userCredsItem = userCredsItem.with("email", email);

                final String password = record.get("Password");
                final String hashedPassword = DigestUtils.sha256Hex(password);
                userCredsItem = userCredsItem.with("hashedPassword", hashedPassword);

                userCredsTable.putItem(userCredsItem);
                System.out.println(userCredsItem.toJSON());

            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (csvParser != null) {
                if (!csvParser.isClosed()) {
                    try {
                        csvParser.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @see creative.user.data.DynamoBulkImportCommand#getName()
     */
    @Override
    public String getName() {
        return "User Data";
    }

}
