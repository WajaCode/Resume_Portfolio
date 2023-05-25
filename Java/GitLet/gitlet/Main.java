package gitlet;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Wajahat Khan
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {

        if (args.length == 0) {
            System.out.println("Must have at least one argument");
            System.exit(-1);
        } else {
            AdminObj adminObj = new AdminObj();
            System.exit(adminObj.processCommand(args));
        }
    }
}


