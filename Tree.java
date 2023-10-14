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

    public String addDirectory (String directoryPath) throws IOException, NoSuchAlgorithmException{
        File rootDir = new File(directoryPath);
        if (!rootDir.exists())
        {
            throw new IOException ("This Directory pathing doesn't exist");
        }
        if (!rootDir.canRead())
        {
            throw new IOException ("Invalid Directory pathing");
        }
        Tree rootTree = new Tree();
        for (String fileDir : rootDir.list())
        {
            System.out.println(fileDir);
            File file = new File(rootDir, fileDir);
            if (file.isFile())
            {
                String filePath = file.getPath();
                String fileName = file.getName();
                String shaOfFile = Blob.SHA1(dirReader(Paths.get(filePath)));
                rootTree.add("blob : " + shaOfFile + " : " + fileName);
            }
            else if (file.isDirectory())
            {
                String dirPath = file.getPath();
                String dirName = file.getName();
                Tree childTree = new Tree();
                String shaOfSubDir = childTree.addDirectory(dirPath);

                rootTree.add("tree : " + shaOfSubDir + " : " + dirName);
            }
        }

        rootTree.save();
        return rootTree.getSha();
    }

    private String getSha() throws IOException, NoSuchAlgorithmException {
		String fileContent = Blob.readFile("tree");
		String hash = Blob.SHA1(fileContent);
		return hash;
	}

    public static String dirReader(Path p) throws IOException {
        StringBuilder str = new StringBuilder();
        BufferedReader br = Files.newBufferedReader(p);
        while (br.ready()) {
            str.append((char) br.read());
        }
        br.close();
        return str.toString();
    }


    
    

    private void createObjectsFolder() {
        File folder = new File("objects");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    
}
