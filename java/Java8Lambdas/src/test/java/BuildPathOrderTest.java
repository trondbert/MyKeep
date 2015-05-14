import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by trond on 14/05/15.
 */
public class BuildPathOrderTest {

    @Test
    public void testProperties() {
        try (
                        InputStream file = getClass().getClassLoader().getResourceAsStream("java8.properties");
                        BufferedReader br = new BufferedReader(new InputStreamReader(file)); ) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
