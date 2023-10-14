import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



public class TreeTest {

    private static final String TEST_FILE = "testfile1.txt";
    private static final String TEST_FILE2 = "testfile2.txt";
    private static Path tempDir;


    @BeforeEach
    void setUp() throws IOException, NoSuchAlgorithmException {
        String content = "File 1 content";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write(content);
        }
        String content2 = "File 2 content";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE2))) {
            writer.write(content2);
        }
    }

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
    }


    @Test
    @DisplayName("Adding one entry")
    void testAdd() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree();
        tree.add("testfile1.txt");
        assertTrue(tree.getTreeStrings().contains("blob : bf4d18642c948f5e19994966e7e2e9d2a26ba427 : testfile1.txt"));
        

    }

    @Test
    @DisplayName("Adding two entries")
    void testAdd2() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree();
        tree.add("testfile1.txt");
        tree.add("testfile2.txt");
        assertTrue(tree.getTreeStrings().contains("blob : bf4d18642c948f5e19994966e7e2e9d2a26ba427 : testfile1.txt") && tree.getTreeStrings().contains("blob : 234677a1877259e1f324358b0e05ea9a38d0590f : testfile2.txt"));
        

    }

    @Test
    void testAddDirectory() {

    }

    @Test
    void testDirReader() {

    }

    @Test
    void testInit() throws IOException, NoSuchAlgorithmException {
        Tree tree = new Tree();
        tree.init();
        File treeFile = new File("tree");
        assertTrue(treeFile.exists());
        assertTrue(treeFile.length() == 0);

        tree.add("testfile1.txt");
        tree.init();
        assertTrue(treeFile.exists());
        assertTrue(treeFile.length() == 0);
    }

    @Test
    void testRemove() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree();
        tree.init();
        tree.add("testfile1.txt");
        tree.add("testfile2.txt");

        tree.remove("testfile1.txt");

        assertFalse(tree.getTreeStrings().contains("blob : bf4d18642c948f5e19994966e7e2e9d2a26ba427 : testfile1.txt"));
    }

    @Test
    void testSave() throws NoSuchAlgorithmException, IOException {
        Tree tree = new Tree();
        tree.init();
            tree.add("testfile1.txt");
            tree.save();
            String blobContent = tree.generateBlob();

            assertNotNull(blobContent);
            assertFalse(blobContent.isEmpty());
            assertTrue(blobContent.contains("testfile1.txt"));
    }

    @Test
    void testWriteToFile() throws IOException {
        List<String> treeStrings = new ArrayList<>();
        treeStrings.add("line 1");
        treeStrings.add("line 2");
        treeStrings.add("line 3");

        String expectedContent = "line 1\nline 2\nline 3";
        String testFileName = "testfile.txt";

        Tree.writeToFile(treeStrings, testFileName);
        String actualContent = Blob.readFile(testFileName);

        assertEquals(expectedContent, actualContent);

    }
}
