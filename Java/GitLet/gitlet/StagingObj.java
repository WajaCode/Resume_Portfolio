package gitlet;

import java.io.File;
import java.util.TreeMap;

/** @author Wajahat Khan. */

public class StagingObj {

    public int statusCommand() {
        return -1;
    }


    public int addCommand(AdminObj theAdminObj, String filename) {
        File fileObj = Utils.join
                (theAdminObj.getCWD(), filename);
        if (!fileObj.exists()) {
            System.out.println("File does not exist.");

        }
        Blob theGitletBlob = new Blob(filename);
        theGitletBlob.addFile(fileObj);
        theAdminObj.getGitletTree().addedFileTreeMap.put(filename, theGitletBlob);
        theAdminObj.stagingTree();
        return 0;
    }

    public int rmCommand(AdminObj theAdminObj, String filename) {
        File fileObj = Utils.join
                (theAdminObj.getCWD(), filename);
        if (!fileObj.exists()) {
            System.out.println("File does not exist.");
            return -1;
        }
        Blob theGitletBlob = new Blob(filename);
        theGitletBlob.addFile(fileObj);
        theAdminObj.getGitletTree().removedFileTreeMap.put(filename, theGitletBlob);
        theAdminObj.stagingTree();
        return 0;
    }

    public void clearStagingArea(Tree theGitletTree) {
        theGitletTree.addedFileTreeMap = new TreeMap<>();
        theGitletTree.removedFileTreeMap = new TreeMap<>();
    }







}
