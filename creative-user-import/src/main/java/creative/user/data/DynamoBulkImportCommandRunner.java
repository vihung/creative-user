/**
 *
 */
package creative.user.data;

import java.util.ArrayList;
import java.util.List;

import creative.user.data.bulk.UserCredentialsData;
import creative.user.data.bulk.UserData;

/**
 * @author vihung
 *
 */
public class DynamoBulkImportCommandRunner {

    public static void cleanAll() {
        for (final DynamoBulkImportCommand command : getCommands()) {
            boolean success;
            success = command.clean();
            System.out.println(command.getName() + " Cleaned " + (success ? "successfully" : "unsuccessfully"));
        }
    }

    private final static List<DynamoBulkImportCommand> getCommands() {
        final List<DynamoBulkImportCommand> commands = new ArrayList<DynamoBulkImportCommand>();
        commands.add(new UserData());
        commands.add(new UserCredentialsData());
        return commands;
    }

    public static void importAll() {
        for (final DynamoBulkImportCommand command : getCommands()) {
            boolean success;
            success = command.run();
            System.out.println(command.getName() + " Imported " + (success ? "successfully" : "unsuccessfully"));
        }
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        cleanAll();
        importAll();
    }

}
