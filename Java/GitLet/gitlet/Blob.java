package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {

    /** */
    protected String blobFile;
    /** */
    protected byte[] blobByte;
    /** */
    protected String blobContentFile;

    public Blob(String fileName) {
        blobFile = fileName;
    }
    public void addFile(File fileName) {
        blobByte = Utils.readContents(fileName);
        blobContentFile = Utils.readContentsAsString(fileName);
    }


}

