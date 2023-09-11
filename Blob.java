import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {

    private String filePath;

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
        String hashed = generateSHA(sb.toString());
        write(hashed, sb);

        return hashed;
    }

    public static void write(String hashed, StringBuilder inside) throws IOException {
        String newFile = hashed;
        FileWriter write = new FileWriter("./objects/" + newFile);
        write.write(inside.toString());
        write.close();
    }

    public static String generateSHA(String input) throws NoSuchAlgorithmException {
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

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String file = "input.txt";
        blob(file);
    }
}