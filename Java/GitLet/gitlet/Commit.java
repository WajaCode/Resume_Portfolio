package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;


/** Commit class, makes the commit for gitlet.
 * @author Wajahat Khan
 * */

public class Commit implements Serializable {

    protected String shaKey;

    protected String commitDate;

    protected String commitMessage;

    protected String previousShaKey;

    protected TreeMap<String, Blob> commitTreeBlob;
    protected TreeMap<String, Blob> commitTreeRemoved;

    /** Making/initializing the commit Tree.
     * @param blobMap
     * @param shaKey1
     * @param stringCommitMessage
     * */
    public Commit(String stringCommitMessage, TreeMap<String, Blob> blobMap, String shaKey1) {
        commitGetter(stringCommitMessage, blobMap, shaKey1);
        Date newDate = new Date();
        SimpleDateFormat sdf =
                new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        commitDate = sdf.format(newDate);
        if (stringCommitMessage.equals("initial commit")) {
            commitDate = "Wed Dec 31 16:00:00 1969 -0800";
        }
        commitTreeRemoved = new TreeMap<>();
    }


    /** Makes the ShaKey into an actual string.
     * @return */
    public String getShaKey() {
        StringBuilder shaString = new StringBuilder();
        for (Blob blob : commitTreeBlob.values()) {
            shaString.append(blob.toString());
        }
        shaKey = shaValueMethod(commitMessage,
                commitDate, shaString.toString());
        return shaKey;
    }

    /** A Getter Function for the commit.
     * @param blobMap
     * @param shaKey1
     * @param stringCommitMessage
     * */
    public void commitGetter(String stringCommitMessage,
                             TreeMap<String, Blob> blobMap, String shaKey1) {
        commitMessage = stringCommitMessage;
        commitTreeBlob = blobMap;
        previousShaKey = shaKey1;
    }

    /** Makes the ShaKey value based of string message,commit Date and message.
     * @param commitDate1
     * @param message
     * @param stringCommitMessage
     * @return
     * */
    public String shaValueMethod(String stringCommitMessage,
                                 String commitDate1, String message) {
        return Utils.sha1(stringCommitMessage + commitDate1 + message);
    }
}

