import java.io.File;
import java.io.PrintWriter;

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
    void testAdd() {
        String blob1ToAdd = "blob : " + index.getBlobHash("junit_example_test1.txt") + " : junit_example_test1.txt";
        String blob2ToAdd = "blob : " + index.getBlobHash("junit_example_test2.txt") + " : junit_example_test2.txt";

        tree.add(blob1ToAdd);
        tree.add(blob2ToAdd);
    }

    @Test
    void testGenerateBlob() {

    }

    @Test
    void testGetSha1() {

    }

    @Test
    void testRemove() {

    }
}
