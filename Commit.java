import java.io.*;
import java.nio.file.Files;
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
    private String commit1;


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
        File prev2CommitFile = new File("objects" + File.separator + commit1);
        if (!prev2CommitFile.exists()) {
            List<String> lines = Files.readAllLines(previousCommitFile.toPath());

            if (lines.size() > 1) {
                int nextCommitIndex = ordinalIndexOf(lines, ":", 3);
                if (nextCommitIndex != -1) {
                    lines.set(nextCommitIndex - 2, shaToAdd);
                } else {
                    lines.add(1, shaToAdd);
                }

                Files.write(previousCommitFile.toPath(), lines);
            }
            return;
        }
        if (previousCommitFile.exists()) {
            List<String> lines = Files.readAllLines(previousCommitFile.toPath());

            if (lines.size() > 1) {
                int nextCommitIndex = ordinalIndexOf(lines, ":", 3);
                if (nextCommitIndex != -1) {
                    lines.set(nextCommitIndex - 1, shaToAdd);
                } else {
                    lines.add(2, shaToAdd);
                }

                Files.write(previousCommitFile.toPath(), lines);
            }
        }
    }


    private int ordinalIndexOf(List<String> list, String str, int n) {
        int pos = -1;
        do {
            pos = list.get(pos + 1).indexOf(str, pos + 1);
        } while (n-- > 0 && pos != -1);
        return pos;
    }

    

    public Commit(String author, String summary) throws NoSuchAlgorithmException, IOException {

        File file = new File("tree");
        if (!file.exists()){
            Tree tree = new Tree();
            tree.init();
        }
        
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
    
    commit1 = hash;

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

    public String getCommitSHA1() throws NoSuchAlgorithmException, IOException {
        String fileContent = Blob.readFile("commit");
        return Blob.SHA1(fileContent);
    }

}
