import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tree {
    private StringBuilder treeContent;

    public Tree() {
        treeContent = new StringBuilder();
    }

    public void writeToFile() throws IOException, NoSuchAlgorithmException {
        String sha1 = calculateSHA1(treeContent.toString());
        String filePath = "./objects/" + sha1;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(treeContent.toString());
        }
    }

    public String getSHA1() throws NoSuchAlgorithmException {
        return calculateSHA1(treeContent.toString());
    }

    public void add(String entry) {
        if (!treeContent.toString().contains(entry)) {
            if (treeContent.length() > 0) {
                treeContent.append("\n");
            }
            treeContent.append(entry);
        }
    }

    public void remove(String entry) {
        String[] lines = treeContent.toString().split("\n");
        treeContent.setLength(0);
        for (String line : lines) {
            if (!line.equals(entry)) {
                if (treeContent.length() > 0) {
                    treeContent.append("\n");
                }
                treeContent.append(line);
            }
        }
    }

    public String getTree() {
        return treeContent.toString();
    }

    public String addDirectory(String directoryPath) throws IOException, NoSuchAlgorithmException {
        File rootDir = new File(directoryPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            throw new IOException("Invalid directory path: " + directoryPath);
        }

        Tree mainTree = new Tree();

        for (File fileDir : rootDir.listFiles()) {
            if (fileDir.isFile()) {
                String filePath = fileDir.getAbsolutePath();
                String fileName = fileDir.getName();
                String shaOfFile = calculateSHA1(Blob.fileReader(Paths.get(filePath)));
                mainTree.add("blob : " + shaOfFile + " : " + fileName);
            } else if (fileDir.isDirectory()) {
                String dirPath = fileDir.getAbsolutePath();
                String dirName = fileDir.getName();
                Tree childTree = new Tree();
                String shaOfSubDir = childTree.addDirectory(dirPath);
                mainTree.add("tree : " + shaOfSubDir + " : " + dirName);
            }
        }

        mainTree.writeToFile();
        return mainTree.getSHA1();
    }

    private String calculateSHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 40) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}
