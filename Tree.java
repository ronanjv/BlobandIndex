import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    private StringBuilder treeContent;
    List<String> treeStrings = new ArrayList<>();

    public Tree() {

    }


    public void init () throws IOException{
        createObjectsFolder();
        File tree = new File("tree");
        if (tree.exists()) {
            tree.delete();
        }
        if (!tree.exists()) {
            tree.createNewFile();
        }
    }

    public void add(String entry) throws IOException, NoSuchAlgorithmException{
        init();
        String fileContent = Blob.readFile(entry);
        String hash = Blob.SHA1(fileContent);

        String blobFileName = "objects" + File.separator + hash;
        writeFile(blobFileName, fileContent);
        String treeContent = "blob : " + hash + " : " + entry;
        treeStrings.add(treeContent);

        FileWriter fileWriter = new FileWriter("tree", true);
        fileWriter.write(treeContent + "\n");
        fileWriter.close();

    }

    private void writeFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }

    public void remove(String entry) throws NoSuchAlgorithmException, IOException{
        String fileContent = Blob.readFile(entry);
        String hash = Blob.SHA1(fileContent);
            for (int i=0; i<treeStrings.size(); i++){
                if (treeStrings.get(i).contains(entry))
                treeStrings.remove(i);
            }

        System.out.println(treeStrings);

        writeToFile(treeStrings, "tree");


    }

    public static void writeToFile(List<String> treeStrings2, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String line : treeStrings2) {
                writer.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createObjectsFolder() {
        File folder = new File("objects");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    
}
