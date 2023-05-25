package gitlet;
import java.io.File;
import java.util.TreeMap;

/** @author Wajahat Khan*/
public class AdminObj {
    /** to exit the switch. */
    private int returnStatus = 0;
    /** the stagingObj accessor. */
    protected StagingObj theStagingObj;
    /** the commitObj accessor. */
    private CommitObj theCommitObj;

    public void processCommand(String... args) {
        switch (args[0]) {
        case "init":
            if (validateNumArgs(1, args)) {
                initCommand();
            }
            break;
        case "add":
            if (validateNumArgs(2, args)) {
                 addCommand(args[1]);
            }
            break;
        case "commit":
            if (validateNumArgs(2, args)) {
                commitCommand(args[1]);
            }
            break;
        case "rm":
            if (validateNumArgs(2, args)) {
                 rmCommand(args[1]);
            }
            break;
        case "log":
            if (validateNumArgs(1, args)) {
                logCommand();
            }
            break;
        case "global-log":
            if (validateNumArgs(1, args)) {
                 theCommitObj.globalCommand();
            }
            break;
        case "find":
            if (validateNumArgs(1, args)) {
                 theCommitObj.findCommand(args[1]);
            }
            break;
        case "status":
            if (validateNumArgs(1, args)) {
                statusCommand();
            }
            break;
        case "checkout":
            if (args.length == 2) {
                checkoutCommandBranchName(args[1]);
            } else if (args.length == 3) {
                checkoutCommandFileName(args[2]);
            } else {
                checkoutCommand(args[1], args[3]);
            }
            break;
        default:
            System.out.println("No command found");
        }
    }
    /** Making the CWD. */
    protected String CWD = System.getProperty("user.dir");
    /** Making of the gitletDirectory. */
    protected File GITLET_DIR = Utils.join(CWD, ".gitlet");
    protected File commitDirectory = Utils.join(CWD, ".gitlet/commits");
    protected File branchDirectory = Utils.join(CWD, ".gitlet/branches");
    protected String theCurrentBranch;
    protected Branch gitletBranch;
    protected Tree gitletTree;
    protected Commit gitletCommit;
    protected TreeMap<String, Blob> untrackedTree;

    AdminObj() {
        try {
            gitletTree = Utils.readObject(
                    Utils.join(GITLET_DIR, "stage"), Tree.class);
            gitletBranch = gitletTree.gitletBranch;
        } catch (IllegalArgumentException e) {
            gitletTree = new Tree();
        }
        theStagingObj = new StagingObj();
        theCommitObj = new CommitObj();
        untrackedTree = new TreeMap<>();
    }

    public void stagingTree() {
        Utils.writeObject(Utils.join(GITLET_DIR, "stage"), gitletTree);
    }

    Tree getGitletTree() {
        return gitletTree;
    }


    String getCWD() {
        return CWD;
    }

    public int initCommand() {
        if (GITLET_DIR.exists()) {
            System.out.println("A gitlet version-control system"
                    + " already exists in the current directory.");
        } else {
            makeGitlet();
        }
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }
    }
    public void makeGitlet() {
        GITLET_DIR.mkdir();
        new File(".gitlet/commits").mkdir();
        new File(".gitlet/branches").mkdir();
        Commit theGitLetCommit = new Commit("initial commit",
                new TreeMap<>(), null);
        String theShaKey = theGitLetCommit.getShaKey();
        gitletCommit = theGitLetCommit;
        Utils.writeObject(Utils.join(commitDirectory, theShaKey), theGitLetCommit);
        Branch theBranch = new Branch("master", theGitLetCommit);
        Utils.writeObject(Utils.join(branchDirectory, theBranch.commitBranchName), theBranch);
        theCurrentBranch = theBranch.commitBranchName;
        gitletTree.getterCurrentBranchMethod(theBranch);
        untrackedTree = new TreeMap<>();
        stagingTree();
    }


    public int addCommand(String filename) {
        theStagingObj.addCommand(this, filename);
        return 0;
    }

    public int rmCommand(String filename) {
        theStagingObj.rmCommand(this, filename);
        return 0;
    }

    public void commitCommand(String commitMessage) {
        theCommitObj.commitCommand(this, commitMessage);
    }

    public int logCommand() {
        return theCommitObj.logCommand(this);
    }


    public void checkoutCommandFileName(String filename) {
        theCommitObj.checkoutCommandFileName(this, filename);
    }

    public void checkoutCommandBranchName(String branchName) {
    }

    public void checkoutCommand(String commitId, String filename) {
        theCommitObj.checkoutCommand(this, commitId, filename);
    }

    public int statusCommand() {
        return -1;
    }

    public static boolean validateNumArgs(int n, String... args) {
        if (args.length == n) {
            return true;
        }
        throw new RuntimeException(
                "Invalid number of arguments");
    }
}

