package gitlet;
import java.io.Serializable;

/** @author Wajahat Khan */
public class Branch implements Serializable {


    /** */
    protected String commitBranchName;
    /** */
    protected Commit branchCommit;


    public Branch(String branchName, Commit commit) {
        commitBranchName = branchName;
        branchCommit = commit;
    }


    public void getterOfBranchToCommit(Commit commit) {
        branchCommit = commit;
    }



}
