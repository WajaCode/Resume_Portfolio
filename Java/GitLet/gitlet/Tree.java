package gitlet;
import java.io.Serializable;
import java.util.TreeMap;


/** @author Wajahat Khan  */

public class Tree implements Serializable {

    /** TreeMap containing the add files. */
    protected TreeMap<String, Blob> addedFileTreeMap;
    /** TreeMap containing the removed files. */
    protected TreeMap<String, Blob> removedFileTreeMap;
    /** TreeMap containing the commits. */
    protected TreeMap<String, Branch> commitBranchTreeMap;
    /** Storing the branch values in gitletBranch. */
    protected Branch gitletBranch;

    /** TreeObject that makes new TreeMap.
     * addedFileTreeMap
     * removedFileTreeMap
     * commitBranchTreeMap
     * */
    public Tree() {
        addedFileTreeMap = new TreeMap<>();
        removedFileTreeMap = new TreeMap<>();
        commitBranchTreeMap = new TreeMap<>();
    }

    /**
     * gets the currentBranch within the branch.
     * @param branch */
    public void getterCurrentBranchMethod(Branch branch) {
        gitletBranch = branch;
    }

}
