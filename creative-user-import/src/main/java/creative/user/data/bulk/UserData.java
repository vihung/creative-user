/**
 *
 */
package creative.user.data.bulk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

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
public class UserData extends DynamoBulkImportCommand {

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
            final Table userTable = dynamoDB.getTable("User");

            final List<CSVRecord> csvRecords = csvParser.getRecords();

            for (final CSVRecord record : csvRecords) {
                final String id = record.get("id");
                userTable.deleteItem(new PrimaryKey("id", id));
                System.out.println("Deleted user with id " + id);
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

            final List<CSVRecord> csvRecords = csvParser.getRecords();

            for (final CSVRecord record : csvRecords) {
                Item userItem = new Item();
                final String id = record.get("id");
                userItem = userItem.withPrimaryKey("id", id);
                final String firstName = record.get("First Name");
                userItem = userItem.with("firstName", firstName);

                final String lastName = record.get("Last Name");
                userItem = userItem.with("lastName", lastName);

                final String nickname = (firstName.substring(0, 1) + lastName).toLowerCase();
                userItem = userItem.with("nickname", nickname);

                userTable.putItem(userItem);
                System.out.println(userItem.toJSON());
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
