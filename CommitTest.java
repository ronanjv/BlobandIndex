import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTest {

/* 
    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(Paths.get("tree"));

            File objectsFolder = new File("objects");
            if (objectsFolder.exists()) {
                File[] objects = objectsFolder.listFiles();
                if (objects != null) {
                    for (File file : objects) {
                        file.delete();
                    }
                }
                objectsFolder.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/



    @Test
    @DisplayName("One Commit")
    void testCommit() throws NoSuchAlgorithmException, IOException{
        Tree tree = new Tree();
        tree.add("testfile1.txt");
        tree.add("testfile2.txt");
        Commit first = new Commit("ronan","first commit");

        File commit = new File("commit");

        String fileContent = Blob.readFile("tree");
        String hash = Blob.SHA1(fileContent);
        String commitContent = Blob.readFile("commit");

        assertTrue(commitContent.contains(hash));
    }

    @Test
    @DisplayName("two Commits")
    void testCommit2() throws NoSuchAlgorithmException, IOException{
        Tree tree = new Tree();
        tree.add("testfile1.txt");
        tree.add("testfile2.txt");
        Commit first = new Commit("ronan","first commit");
        tree.save();
        

        File commit = new File("commit");

        String fileContent = Blob.readFile("tree");
        String treeHash = Blob.SHA1(fileContent);
        String commit1Content = Blob.readFile("commit");
        String com1Hash = Blob.SHA1(commit1Content);
        Commit second = new Commit("12cd02b39ed05d7a9a06056fa75ee4db7fa6317f","ronan","second commit");
        String commit2Content = Blob.readFile("commit");


        String commitContent1 = Blob.readFile("objects/" + com1Hash);

        //checks for commit 1 having tree
        assertTrue(commit1Content.contains(treeHash));
        //checks for commit 2 having tree
        assertTrue(commit2Content.contains(treeHash));
        //checks for commit 2 having sha of commit 1
        assertTrue(commitContent1.contains("64e5c1a8ce7f1cc9129cdc7063ac4e0c23d8890e"));
        //checks for commit 2 having sha of commit 1
        assertTrue(commit2Content.contains("12cd02b39ed05d7a9a06056fa75ee4db7fa6317f"));
    }




    @Test
    void testCreateBlob() throws NoSuchAlgorithmException, IOException {

        Tree tree = new Tree();
        tree.init();
        Commit commit = new Commit("ronan", "first commit");
        commit.createBlob();


        String commitContent = Blob.readFile("commit");
        String blobContent = Blob.readFile("objects/" + commit.getCommitSHA1());
        assertEquals(commitContent, blobContent);
    }

    @Test
    void testCreateHeadFile() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree();
        tree.init();
        Commit head = new Commit("ronan", "first commit");

        File headfile = new File("head");
        assertTrue(headfile.exists());

        String headContent = Blob.readFile("head");

        String commitContent = Blob.readFile("commit");
        String hash = Blob.SHA1(commitContent);

        assertEquals(hash, headContent);
    }

    @Test
    void testUpdateHeadFile() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree();
        tree.add("testfile1.txt");
        tree.add("testfile2.txt");
        
        Commit first = new Commit("ronan","first commit");
        File headfile = new File("head");
        String head = Blob.readFile("head");
        String commit1Content = Blob.readFile("commit");
        String com1Hash = Blob.SHA1(commit1Content);
        assertTrue(head.contains(com1Hash));


        //updateHeadFile is run within the constructor to update every time a commit is made
        Commit second = new Commit("12cd02b39ed05d7a9a06056fa75ee4db7fa6317f","ronan","second commit");
        String commit2Content = Blob.readFile("commit");
        String com2Hash = Blob.SHA1(commit2Content);
        head = Blob.readFile("head");
        assertTrue(head.contains(com2Hash));

    }
}
