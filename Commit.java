import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
    String tree = null;
    String prevCommit = null;
    String nextCommit = null;
    String author;
    String date;
    String summary;

    StringBuilder toPrint;
    static String pathToWorkSpace = "C:\\Users\\danie\\OneDrive\\Desktop\\Topics Repos\\BlobandIndexRonanUpdated";

    // constructor for any commit after the first ever commit
    public Commit(String shaOfPrevCommit, String author, String summary) throws Exception {
        createTree();
        this.prevCommit = shaOfPrevCommit;
        if (nextCommit == null)
            nextCommit = "";

        this.author = author;
        this.date = getDate();
        this.summary = summary;

        toPrint.append(this.tree + "\n");
        toPrint.append(this.prevCommit + "\n");
        toPrint.append(this.nextCommit + "\n");
        toPrint.append(this.author + "\n");
        toPrint.append(this.date + "\n");
        toPrint.append(this.summary);
    }

    // constructor for the first commit with no parent or next
    public Commit(String author, String summary) throws Exception {
        createTree();
        if (prevCommit == null)
            prevCommit = "";
        if (nextCommit == null)
            nextCommit = "";

        this.author = author;
        this.date = getDate();
        this.summary = summary;

        toPrint.append(this.tree + "\n");
        toPrint.append(this.prevCommit + "\n");
        toPrint.append(this.nextCommit + "\n");
        toPrint.append(this.author + "\n");
        toPrint.append(this.date + "\n");
        toPrint.append(this.summary);

        // prevCommit needs to be updated to the sha of this current commit so that next
        // commits can access this value
        this.prevCommit = generateSha1();
        save();
    }

    public void save() throws Exception {
        // Create the commit file in the 'objects' folder
        Path commitPath = Paths.get(pathToWorkSpace + "\\objects", generateSha1());
        Files.write(commitPath, toPrint.toString().getBytes());
    }

    public String generateSha1() throws Exception {
        StringBuilder forSHA = new StringBuilder("");
        // add all the file contents except for the next commit
        forSHA.append(this.tree + "\n");
        forSHA.append(this.prevCommit + "\n");
        forSHA.append(this.author + "\n");
        forSHA.append(this.date + "\n");
        forSHA.append(this.summary);
        return Blob.generateSHA(forSHA.toString());
    }

    // code taken from javatpoint.com
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    // create a tree and generate a base sha1 for an empty file
    public void createTree() throws Exception {
        Tree tree = new Tree();
        tree.generateBlob();
        this.tree = tree.getSha1();
    }
}
