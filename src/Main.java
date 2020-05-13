import java.io.IOException;

/**
 * Created by Hubaishi on 02.06.17.
 * last updated on 07.06.2017 at 18:57
 */
public class Main {

    public static void main(String[] args) throws IOException {


        HuffmanTree huff = new HuffmanTree();
        huff.commpress("text.txt");
        huff.decommpress();

    }
}
