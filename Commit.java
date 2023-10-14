import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Commit {
    private String treeSHA1;
    private String prevSHA;
    private String author;
    private String date;
    private String summary;


    public Commit(String parentCommitSHA1, String author, String summary) throws NoSuchAlgorithmException, IOException {


        String fileContent = Blob.readFile("tree");
        String hash = Blob.SHA1(fileContent);
        this.prevSHA = parentCommitSHA1;
        this.treeSHA1 = hash;
        this.author = author;
        this.summary = summary;
        this.date = getDate();

        File commitFile = new File("commit");
    if (!commitFile.exists()) {
        commitFile.createNewFile();
    }

    PrintWriter pw = new PrintWriter(new FileWriter(commitFile));
    pw.println( treeSHA1);
    pw.println(prevSHA);
    pw.println(author);
    pw.println(date);
    pw.print(summary);
    pw.close();

    String content = Blob.readFile("commit");
    String comHash = Blob.SHA1(content);
    
    createBlob();
    updatePrevious(prevSHA,comHash);
    updateHeadFile();

    }

    private void updatePrevious(String previousSha, String shaToAdd) throws IOException {
        File previousCommitFile = new File("objects" + File.separator + previousSha);
        if (previousCommitFile.exists()) {
            List<String> lines = Files.readAllLines(previousCommitFile.toPath());

            if (lines.size() > 1) {
                lines.set(1, shaToAdd);
            } else {
                lines.add(1, shaToAdd);
            }

            Files.write(previousCommitFile.toPath(), lines);
        }
    }

    

    public Commit(String author, String summary) throws NoSuchAlgorithmException, IOException {
        String fileContent = Blob.readFile("tree");
        String hash = Blob.SHA1(fileContent);
        this.treeSHA1 = hash;
        this.author = author;
        this.summary = summary;
        this.date = getDate();

        File commitFile = new File("commit");
    if (!commitFile.exists()) {
        commitFile.createNewFile();
    }

    PrintWriter pw = new PrintWriter(new FileWriter(commitFile));
    pw.println( treeSHA1);
    pw.println(author);
    pw.println(date);
    pw.print(summary);
    pw.close();

    createBlob();
    createHeadFile();
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(new Date());
    }

    public void createBlob() throws IOException, NoSuchAlgorithmException{
        String fileContent = Blob.readFile("commit");
        String hash = Blob.SHA1(fileContent);

        String blobFileName = "objects" + File.separator + hash;
        Blob.writeFile(blobFileName, fileContent);
    }

    public void createHeadFile() throws NoSuchAlgorithmException, IOException{
        String fileContent = Blob.readFile("commit");
        String hash = Blob.SHA1(fileContent);
        Blob.writeFile("head", hash);


    }

    public void updateHeadFile() throws IOException, NoSuchAlgorithmException {
        String fileContent = Blob.readFile("commit");
        String hash = Blob.SHA1(fileContent);
    
        File headFile = new File("head");
        if (headFile.exists()) {
            headFile.delete();
        }
        
    
        try (PrintWriter writer = new PrintWriter(new FileWriter("head"))) {
            writer.print(hash);
        }
    }

    


}
