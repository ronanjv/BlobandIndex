import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobTest {

    private static final String TEST_FILE = "testfile.txt";
    private Blob blob;

    @BeforeEach
    void setUp() throws IOException, NoSuchAlgorithmException {
        String content = "Test content";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write(content);
        }
        blob = new Blob(TEST_FILE);
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
    @DisplayName("testing SHA1")
    void testSHA1() throws IOException, NoSuchAlgorithmException {
        String content = blob.readFile(TEST_FILE);
        String hash = blob.SHA1(content);
        assertEquals("bca20547e94049e1ffea27223581c567022a5774", hash);
    }

    @Test
    void testCreateObjectsFolder() throws NoSuchAlgorithmException, IOException {
        blob.createObjectsFolder();
        assertTrue(Files.exists(Paths.get("objects")));
    }

    @Test
    void testReadFile() throws IOException {
        String content = blob.readFile(TEST_FILE);
        assertEquals("Test content", content);
    }

    @Test
    void testWriteFile() throws IOException {
        String testContent = "Test content";
        blob.writeFile(TEST_FILE, testContent);
        String content = blob.readFile(TEST_FILE);
        assertEquals(testContent, content);
    }
}
