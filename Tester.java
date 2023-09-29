public class Tester {
    public static void main(String[] args) throws Exception {

        Blob blob = new Blob();
        Blob blob2 = new Blob();

        Index index = new Index();

        index.addBlob("input.txt");
        index.addBlob("example.txt");

        index.removeBlob("input.txt");
        index.addBlob("something.txt");
    }
}