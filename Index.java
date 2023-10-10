import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Index {

    private HashMap<String, String> files = new HashMap<>();
    private String path = "objects";

    public Index() throws IOException {
        init();
    }

    public void init() throws IOException {
        java.nio.file.Path folderPath = Paths.get(path);
        if (!Files.exists(folderPath)) {
            Files.createDirectory(folderPath);
        }

        Path indexPath = folderPath.resolve("index");
        if (!Files.exists(indexPath)) {
            Files.createFile(indexPath);
        }
    }

    public void addBlob(String fileName) throws IOException, NoSuchAlgorithmException {
        String hashName = Blob.blob(fileName);

        files.put(fileName, hashName);

        Path indexPath = Paths.get(path + File.separator + "index");
        try (BufferedWriter writer = Files.newBufferedWriter(indexPath, StandardOpenOption.CREATE,
                StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            writer.write(fileName + " : " + hashName);
            writer.newLine();
        }
    }

    public void removeBlob(String fileName) throws IOException {
        files.remove(fileName);

        Path indexPath = Paths.get(path + File.separator + "index");
        List<String> lines = Files.readAllLines(indexPath);
        lines.removeIf(line -> line.startsWith(fileName + " : "));
        Files.write(indexPath, lines);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Index index = new Index();
        index.addBlob("file1.txt");
        index.removeBlob("file1.txt");
    }
}