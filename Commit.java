import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
    String prevCommit = null;
    String nextCommit = null;

    public Commit(String shaOfParentCommit, String author, String summary) {

    }

    public Commit(String author, String summary) {

    }

    public String generateSha1() {

    }

    // inspired by javatpoint.com
    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public void createTree() throws Exception {
        Tree tree = new Tree();
        tree.generateBlob();
        tree.getSha1();
    }
}
