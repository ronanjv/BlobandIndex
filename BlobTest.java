import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BlobTest {

    private static final String TEST_FILE = "testfile.txt";
    private Blob blob;

    @BeforeEach
    void setUp() throws IOException {
        String content = "Test content";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write(content);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            Files.deleteIfExists(Path.of(TEST_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }

    @Test
    void testSHA1() throws IOException, NoSuchAlgorithmException {
        Blob blob = new Blob("testfile.txt");
        String content = blob.readFile(TEST_FILE);
        String hash = blob.SHA1(content);
        assertEquals("ae64e12636d3bd2d9e01a90f52989fdf373a83b4", hash);

    }

    @Test
    void testCreateObjectsFolder() throws NoSuchAlgorithmException, IOException {
        Blob blob = new Blob(TEST_FILE);
        blob.createObjectsFolder();
        
        assertTrue(Files.exists(Paths.get("objects")));
    }


    @Test
    void testReadFile() {

    }

    @Test
    void testWriteFile() {

    }
}
