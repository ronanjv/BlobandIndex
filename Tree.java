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

    public void add(String entry) throws IOException, NoSuchAlgorithmException {
        File tree = new File("tree");
        if (!tree.exists()) {
            tree.createNewFile();
        }
    
        String treeContent;
        if (entry.startsWith("tree :")) {
            treeContent = entry;
        } else {
            String fileContent = Blob.readFile(entry);
            String hash = Blob.SHA1(fileContent);
            String blobFileName = "objects" + File.separator + hash;
            writeFile(blobFileName, fileContent);
            treeContent = "blob : " + hash + " : " + entry;
        }
        treeStrings.add(treeContent);

        try (FileWriter fileWriter = new FileWriter("tree", true)) {
            if (tree.exists()) {
                fileWriter.write("\n" + treeContent);
            } else {
                fileWriter.write(treeContent);
            }
        }
    }
    

    private void writeFile(String fileName, String content) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }

    public void remove(String entry) throws NoSuchAlgorithmException, IOException{
            for (int i=0; i<treeStrings.size(); i++){
                if (treeStrings.get(i).contains(entry))
                treeStrings.remove(i);
            }


        writeToFile(treeStrings, "tree");

    }

    public void save() throws IOException, NoSuchAlgorithmException{
        String fileContent = Blob.readFile("tree");
        String hash = Blob.SHA1(fileContent);

        String blobFileName = "objects" + File.separator + hash;
        writeToFile(treeStrings, blobFileName);
    }

    public String generateBlob() throws IOException, NoSuchAlgorithmException {
        String fileContent = Blob.readFile("tree");
        String hash = Blob.SHA1(fileContent);
        return Blob.readFile("objects" + File.separator + hash);
    }

    public static void writeToFile(List<String> treeStrings2, String fileName) {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
	        boolean firstLine = true;
	        for (String line : treeStrings2) {
	            if (!firstLine) {
	                writer.println();
	            } else {
	                firstLine = false;
	            }
	            writer.print(line);
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
