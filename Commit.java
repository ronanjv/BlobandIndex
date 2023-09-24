import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
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
        save();

        // add updated commit contents in a stringbuilder
        File fileToPrevCommit = new File(pathToWorkSpace + "\\objects\\" + shaOfPrevCommit);
        StringBuilder tempSB = new StringBuilder("");
        BufferedReader br = new BufferedReader(new FileReader(fileToPrevCommit));
        for (int i = 0; i < 5; i++) {
            if (i != 2)
                tempSB.append(br.readLine());
            else
                br.readLine();
            tempSB.append(generateSha1());
        }
        br.close();

        // update the second most recently created commits "next" value
        PrintWriter pw = new PrintWriter(shaOfPrevCommit);
        pw.print(tempSB.toString());
        pw.close();
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
