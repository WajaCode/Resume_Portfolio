package gitlet;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

/** @author Wajahat Khan . */
public class CommitObj {
    CommitObj() {
    }
    public void commitCommand(AdminObj theAdmindObj, String commitMessage) {
        if (commitMessage.equals("") || commitMessage.equals(" ")) {
            System.out.println("Please enter a commit message.");
            return;
        }
        TreeMap<String, Blob> gitletBlobMap = new TreeMap<>();
        gitletBlobMap.putAll(
                theAdmindObj.gitletTree.addedFileTreeMap);
        Commit theGitletCommit = new Commit(commitMessage,
                gitletBlobMap, theAdmindObj.gitletBranch.branchCommit.shaKey);
        String commitKey = theGitletCommit.getShaKey();
        theGitletCommit.commitTreeRemoved.putAll(
                theAdmindObj.gitletTree.removedFileTreeMap);
        theAdmindObj.gitletCommit = theGitletCommit;
        theAdmindObj.gitletTree.gitletBranch.
                getterOfBranchToCommit(theGitletCommit);
        theAdmindObj.theStagingObj.clearStagingArea(theAdmindObj.gitletTree);
        theAdmindObj.stagingTree();
        Utils.writeObject(Utils.join(theAdmindObj.commitDirectory,
                commitKey), theGitletCommit);
        Utils.writeObject(Utils.join(theAdmindObj.branchDirectory,
                theAdmindObj.gitletTree.gitletBranch.commitBranchName),
                theAdmindObj.gitletTree.gitletBranch);
    }
    public int logCommand(AdminObj theAdmindObj) {
        Commit theCommit = theAdmindObj.gitletBranch.branchCommit;
        while (theCommit != null) {
            System.out.println("===");
            System.out.println("commit " + theCommit.shaKey);
            System.out.println("Date: " + theCommit.commitDate);
            System.out.println(theCommit.commitMessage);
            System.out.println();
            String previousCommit = theCommit.previousShaKey;
            if (previousCommit == null) {
                break;
            }
            theCommit = Utils.readObject(Utils.join
                    (theAdmindObj.commitDirectory, previousCommit),
                    Commit.class);
        }
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }
    }
    public int globalCommand() {
        System.out.println("Does the same thing as Log "
                + "but only displays commits???/");
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }

    }
    public int findCommand(String message) {
        System.out.println("Found no commit with that message");
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }
    }
    public void checkoutCommandFileName(AdminObj theAdminObj, String filename) {
        Blob tempBlob;
        Commit theCommit = theAdminObj.gitletBranch.branchCommit;
        TreeMap<String, Blob> theBlobTreeFiles = theCommit.commitTreeBlob;
        for (String theShaKey : theBlobTreeFiles.keySet()) {
            if (filename.equals(theShaKey)) {
                File fileObj = new File(filename);
                String contents =
                        theBlobTreeFiles.get(theShaKey).blobContentFile;
                Utils.writeContents(fileObj, contents);
                tempBlob = new Blob(fileObj.getName());
                tempBlob.addFile(fileObj);
                break;
            }
        }
        theAdminObj.stagingTree();
    }




    public int checkoutCommand(AdminObj theAdminObj,
                               String commitId, String fileName) {
        String theFileName = "";
        List<String> fileNamesInDirectory =
                Utils.plainFilenamesIn(theAdminObj.commitDirectory);
        if (fileNamesInDirectory == null) {
            System.out.println("No commit with that id exists.");
            return -1;
        } else {
            for (String filesName : fileNamesInDirectory) {
                if (filesName.startsWith(commitId)) {
                    theFileName = filesName;
                    break;
                }
            }
        }
        Commit theCommit = Utils.readObject(Utils.join
                (theAdminObj.commitDirectory, theFileName), Commit.class);
        TreeMap<String, Blob> theBlobFiles = theCommit.commitTreeBlob;
        for (String theShaKey : theBlobFiles.keySet()) {
            if (fileName.equals(theShaKey)) {
                File fileObj = new File(fileName);
                String contents = theBlobFiles.get(theShaKey).blobContentFile;
                Utils.writeContents(fileObj, contents);
                new Blob(fileObj.getName()).addFile(fileObj);
                break;
            }
        }
        theAdminObj.stagingTree();
        return 0;
    }
    /**
     * Creates a new Branch with a givenName(branchName).
     * points it at the current head Node.
     * @param branchName
     * @return
     */
    public int branchCommand(String branchName) {
        System.out.println("A Branch with that name already exists");
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Deletes the Branch with the given name.
     * This means delete the pointer associated with the branch,
     * it does not mean to delete all commits
     * that were created under the branch, or anything like that.
     * @param branchName
     * @return
     */
    public int rmBranchCommand(String branchName) {
        System.out.println("A Branch with that name does not exist.");
        System.out.println("Cannot remove the current branch.");
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * check out all the files tracked by the given commit.
     * Removes tracked files that are not present in the commit.
     * Moves the current branch's head to the commit Node.
     * Checkout is an arbitary commit that also changes the current branch head.
     * @param commitId
     * @return commitID
     */
    public int resetCommand(String commitId) {
        System.out.println("No commit with that id exits.");
        System.out.println("There is an untracked file in the way, "
                + "delete it, or add and commit it first.");
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Merges files from given branch into the current branch.
     * @param commitBranchName
     * @return value
     */
    public int mergeCommand(String commitBranchName) {
        System.out.println("Merging parent A and parent B together into one");
        if (1 > 2) {
            return 0;
        } else {
            return -1;
        }

    }

}

