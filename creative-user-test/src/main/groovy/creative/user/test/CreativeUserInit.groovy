/**
 *
 */
package creative.user.test

import creative.user.data.DynamoBulkImportCommandRunner
import creative.user.ddl.DynamoDDLCommandRunner

/**
 * @author vihung
 *
 */
class CreativeUserInit {
    public static void setUp() {
        def ddlCommandRunner = new DynamoDDLCommandRunner();
        ddlCommandRunner.cleanAll();
        ddlCommandRunner.createAll();

        def dataCommandRunner = new DynamoBulkImportCommandRunner();
        dataCommandRunner.importAll();
    }

    public static void  tearDown() {
        def dataCommandRunner = new DynamoBulkImportCommandRunner();
        dataCommandRunner.cleanAll();

        def ddlCommandRunner = new DynamoDDLCommandRunner();
        ddlCommandRunner.cleanAll();
    }
}