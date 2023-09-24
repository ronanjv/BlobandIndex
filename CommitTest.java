import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommitTest {
    private Commit commit;
    static String pathToWorkSpace = "C:\\Users\\danie\\OneDrive\\Desktop\\Topics Repos\\BlobandIndexRonanUpdated";

    @BeforeEach
    void setUp() throws Exception {
        commit = new Commit("prevSHA", "John Doe", "Initial commit");
    }

    @Test
    @DisplayName("[1] Test the createTree() method.")
    void testCreateTree() throws Exception {
        Assertions.assertNotNull(commit.tree);
    }

    @Test
    @DisplayName("[2] Test the generateSha1() method.")
    void testGenerateSha1() throws Exception {
        String sha1 = commit.generateSha1();
        Assertions.assertNotNull(sha1);
        Assertions.assertEquals(40, sha1.length());
    }

    @Test
    @DisplayName("[3] Test the getDate() method.")
    void testGetDate() throws Exception {
        String date = commit.getDate();
        Assertions.assertNotNull(date);
    }

    @Test
    @DisplayName("[4] Test the save() method.")
    void testSave() throws Exception {
        // You should customize this test based on how your save() method works.
        // Here, I'm assuming you have a save() method that writes to a file,
        // so you can check if the file exists after saving.
        // You will need to import java.nio.file.Path and java.nio.file.Paths for this.
        Path commitPath = Paths.get(pathToWorkSpace + "\\objects", commit.generateSha1());
        Assertions.assertTrue(Files.exists(commitPath));
    }
}
