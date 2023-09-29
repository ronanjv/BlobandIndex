import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TreeTest {
    static String pathToWorkSpace = "C:\\Users\\danie\\OneDrive\\Desktop\\Topics Repos\\BlobandIndexRonanUpdated";
    static Index index;
    static Tree tree;

    @BeforeAll
    static void setUpBefore() throws Exception {
        tree = new Tree();
        index = new Index();
        File file1 = new File(pathToWorkSpace + "\\junit_example_test1.txt");
        File file2 = new File(pathToWorkSpace + "\\junit_example_test2.txt");
        file1.createNewFile();
        file2.createNewFile();
        PrintWriter pw1 = new PrintWriter(file1);
        PrintWriter pw2 = new PrintWriter(file2);
        pw1.write("some content for file 1");
        pw2.write("some content for file 2");
        pw1.close();
        pw2.close();
    }

    @AfterAll
    static void tearDownAfter() throws Exception {
        Utils.deleteFile("junit_example_test1.txt");
        Utils.deleteFile("junit_example_test2.txt");
        Utils.deleteFile("index");
        Utils.deleteDirectory("objects");
    }

    @Test
    void testAdd() throws Exception {
        String blob1ToAdd = "blob : f5cda28ce12d468c64a6a2f2224971f894442f1b : junit_example_test1.txt";
        String blob2ToAdd = "blob : 50d4b41eed4faffe212d8cf6ec89d7889dfeff9e : junit_example_test2.txt";

        tree.add(blob1ToAdd);
        tree.add(blob2ToAdd);
        List<String> entries = tree.getEntries();
        assertTrue(entries.contains(blob1ToAdd));
        assertTrue(entries.contains(blob2ToAdd));
        tree.remove(blob1ToAdd);
        tree.remove(blob2ToAdd);
    }

    @Test
    void testGenerateBlob() throws Exception {
        String blob1ToAdd = "blob : f5cda28ce12d468c64a6a2f2224971f894442f1b : junit_example_test1.txt";
        String blob2ToAdd = "blob : 50d4b41eed4faffe212d8cf6ec89d7889dfeff9e : junit_example_test2.txt";

        tree.add(blob1ToAdd);
        tree.add(blob2ToAdd);
        tree.generateBlob();

        File f1 = new File(pathToWorkSpace + "\\objects", tree.getSha1());
        BufferedReader br = new BufferedReader(new FileReader(f1));

        // assert that the correct string is added to the tree file which should also
        // have the correct hash as its name
        assertEquals("blob : f5cda28ce12d468c64a6a2f2224971f894442f1b : junit_example_test1.txt",
                br.readLine());
        assertEquals("blob : 50d4b41eed4faffe212d8cf6ec89d7889dfeff9e : junit_example_test2.txt",
                br.readLine());
        br.close();

        // delete the tree file within the objects folder
        Path p1 = Paths.get(
                "C:\\Users\\danie\\OneDrive\\Desktop\\Topics Repos\\BlobandIndexRonanUpdated\\objects\\3caac0f41752bcce36e05d509612cff335688ccf");
        Files.delete(p1);
        tree.remove(blob1ToAdd);
        tree.remove(blob2ToAdd);
    }

    @Test
    void testGetSha1() throws Exception {
        String blob1ToAdd = "blob : f5cda28ce12d468c64a6a2f2224971f894442f1b : junit_example_test1.txt";
        String blob2ToAdd = "blob : 50d4b41eed4faffe212d8cf6ec89d7889dfeff9e : junit_example_test2.txt";

        tree.add(blob1ToAdd);
        tree.add(blob2ToAdd);
        tree.generateBlob();

        // assert that the getSha1 method returns the proper hash for the tree file
        assertEquals("3caac0f41752bcce36e05d509612cff335688ccf", tree.getSha1());

        // delete the tree file within the objects folder
        Path p1 = Paths.get(
                "C:\\Users\\danie\\OneDrive\\Desktop\\Topics Repos\\BlobandIndexRonanUpdated\\objects\\3caac0f41752bcce36e05d509612cff335688ccf");
        Files.delete(p1);
        tree.remove(blob1ToAdd);
        tree.remove(blob2ToAdd);
    }

    @Test
    void testRemove() throws Exception {
        String blob1ToAdd = "blob : f5cda28ce12d468c64a6a2f2224971f894442f1b : junit_example_test1.txt";
        String blob2ToAdd = "blob : 50d4b41eed4faffe212d8cf6ec89d7889dfeff9e : junit_example_test2.txt";

        tree.add(blob1ToAdd);
        tree.add(blob2ToAdd);
        tree.remove(blob1ToAdd);
        tree.generateBlob();

        File f1 = new File(pathToWorkSpace + "\\objects", tree.getSha1());
        BufferedReader br = new BufferedReader(new FileReader(f1));

        // assert that the tree file has removed the correct blob if the first blob was
        // removed than the first line of the file should contain the second file
        // created
        assertEquals("blob : 50d4b41eed4faffe212d8cf6ec89d7889dfeff9e : junit_example_test2.txt",
                br.readLine());
        br.close();

        // delete the tree file within the objects folder
        Path p1 = Paths.get(
                "C:\\Users\\danie\\OneDrive\\Desktop\\Topics Repos\\BlobandIndexRonanUpdated\\objects\\04ee905523810ffee3e5fe7f73e2825844674bbb");
        Files.delete(p1);
        tree.remove(blob2ToAdd);
    }

    @Test
    void testAddDirectory() throws IOException, NoSuchAlgorithmException {
        // Create a temporary directory with some files and subdirectories
        Path tempDir = Files.createTempDirectory("test_tree");
        Path file1 = tempDir.resolve("file1.txt");
        Files.createFile(file1);
        Path file2 = tempDir.resolve("file2.txt");
        Files.createFile(file2);
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectory(subDir);
        Path subFile = subDir.resolve("subfile.txt");
        Files.createFile(subFile);

        // Create a Tree instance
        Tree tree = new Tree();

        // Add the directory to the tree
        String directorySha1 = tree.addDirectory(tempDir.toString());

        // Generate the blob for the tree
        tree.generateBlob();

        // Get the SHA-1 hash of the generated tree blob
        String treeSha1 = tree.getSha1();

        // Assert that the returned SHA-1 hash matches the expected value
        assertEquals(directorySha1, treeSha1);

        // Optionally, you can assert other properties of the tree structure here

        // Clean up the temporary directory
        Files.deleteIfExists(subFile);
        Files.deleteIfExists(subDir);
        Files.deleteIfExists(file2);
        Files.deleteIfExists(file1);
        Files.deleteIfExists(tempDir);
    }
}
