import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {

    private String filePath;
    private String sha;

    public Blob(String inputFile) throws IOException {
        try {
            File file = new File(inputFile);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder fileInfo = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                fileInfo.append(line).append("");
            }
            reader.close();
            String hashed = hashStringToSHA1(fileInfo.toString());
            write(hashed, fileInfo);
            sha = hashed;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // google
    public static String hashStringToSHA1(String input) {
        try {
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            byte[] inputBytes = input.getBytes();
            sha1Digest.update(inputBytes);
            byte[] hashBytes = sha1Digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xFF & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String blob(String inputFile) throws IOException, NoSuchAlgorithmException {
        File file = new File(inputFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            sb.append(line).append("");
        }
        reader.close();
        String hashed = SHA1(sb.toString());
        write(hashed, sb);

        return hashed;
    }

    public static void write(String hashed, StringBuilder inside) {
        try {
            String newFile = hashed;
            File objects = new File("./objects");
            objects.mkdirs();
            FileWriter write = new FileWriter("./objects/" + newFile);
            write.write(inside.toString());
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String SHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest objSHA = MessageDigest.getInstance("SHA-1");
        byte[] bytSHA = objSHA.digest(input.getBytes());
        BigInteger intNumber = new BigInteger(1, bytSHA);
        String strHashCode = intNumber.toString(16);

        // pad with 0 if the hexa digits are less then 40.
        while (strHashCode.length() < 40) {
            strHashCode = "0" + strHashCode;
        }
        return strHashCode;
    }

    public String getSha() {
        return sha;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String file = "input.txt";
        blob(file);

    }

    public static String fileReader(Path p) throws IOException {
        StringBuilder str = new StringBuilder();
        // BufferedReader br = new BufferedReader(file);
        BufferedReader br = Files.newBufferedReader(p);
        while (br.ready()) {
            str.append((char) br.read());
        }
        br.close();
        return str.toString();
    }
}