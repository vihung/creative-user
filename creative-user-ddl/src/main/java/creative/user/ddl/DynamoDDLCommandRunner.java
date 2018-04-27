/**
 *
 */
package creative.user.ddl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vihung
 *
 */
public class DynamoDDLCommandRunner {

    private final static List<DynamoDDLCommand> getCommands() {
        final List<DynamoDDLCommand> commands = new ArrayList<DynamoDDLCommand>();
        commands.add(new UserDDL());
        commands.add(new UserCredentialsDDL());
        commands.add(new AccessTokenDDL());
        return commands;
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        for (final DynamoDDLCommand command : getCommands()) {
            boolean success;
            success = command.clean();
            System.out.println("main(): " + command.getName() + " Cleaned " + (success ? "successfully" : "unsuccessfully"));
            success = command.run();
            System.out.println("main(): " + command.getName() + " Run " + (success ? "successfully" : "unsuccessfully"));
        }
    }

}
