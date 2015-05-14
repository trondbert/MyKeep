import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BuildPathOrder {

    public static void main(String[] args) {
        try (
                InputStream file = BuildPathOrder.class.getClassLoader().getResourceAsStream("java8.properties");
                BufferedReader br = new BufferedReader(new InputStreamReader(file)); ) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
