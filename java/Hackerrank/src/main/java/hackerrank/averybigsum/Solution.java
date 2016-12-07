package hackerrank.averybigsum;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class Solution {

    public static void main(String[] args) {
        try(InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr)) {
            br.readLine(); // Number of numbers, don't need it

            String[] parts = br.readLine().split(" ");
            Stream<Long> numbers = Arrays.asList(parts).stream().map(Long::parseLong);

            Optional<Long> sum = numbers.reduce((x,y) -> x+y);
            System.out.println(sum.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
