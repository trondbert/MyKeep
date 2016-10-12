import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author trond.
 */
public class ReadBytes {

    public static void main(String[] args) {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(
                    //"/Users/trond/Desktop/sample.txt"));
                    "/Users/trond/Desktop/2016.04.27_18-13-13-GMT+2.wcap"));

            int read = isr.read();
            do {
                if (read >= 65 && read <= 122)
                    System.out.print((char) read);
                else
                    System.out.println();
                read = isr.read();
            } while (read != -1);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
