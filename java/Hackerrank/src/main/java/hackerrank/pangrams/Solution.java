package hackerrank.pangrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;
import java.util.stream.Stream;

public class Solution {

    public static void main(String[] args) {
        try(InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr)) {
            String input = br.readLine();

            TreeSet<Object> chars = new TreeSet<>();
            for (Object aChar : input.toLowerCase().toCharArray()) {
                chars.add(aChar);
            }
            Stream<Object> alphaStream = chars.stream().filter(c -> ("" + c).matches("[a-z]"));
            System.out.println(alphaStream.count() == 26 ? "pangram" : "not pangram");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
