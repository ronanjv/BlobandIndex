import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Tester {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Index indy = new Index();

        indy.addBlob("test2.txt");
        indy.addBlob("test.txt");
        indy.removeBlob("test.txt");
    }
}