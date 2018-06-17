/**
 *
 */
package creative.user.ddl;

import java.util.ArrayList;
import java.util.List;

import creative.user.ddl.table.AccessTokenDDL;
import creative.user.ddl.table.UserCredentialsDDL;
import creative.user.ddl.table.UserDDL;

/**
 * @author vihung
 *
 */
public class DynamoDDLCommandRunner {

  public static void cleanAll() {
    for (final DynamoDDLCommand command : getCommands()) {
      boolean success;
      success = command.clean();
      System.out.println(command.getName() + " Cleaned " + (success ? "successfully" : "unsuccessfully"));
    }
  }

  public static void createAll() {
    for (final DynamoDDLCommand command : getCommands()) {
      boolean success;
      success = command.run();
      System.out.println(command.getName() + " Created " + (success ? "successfully" : "unsuccessfully"));
    }
  }

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
    cleanAll();
    createAll();
  }

}
