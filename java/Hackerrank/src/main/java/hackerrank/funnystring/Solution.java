package hackerrank.funnystring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Math.abs;

public class Solution {


    public static void main(String[] args) {

        try (InputStreamReader isr = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(isr)) {
            int taskCount = Integer.parseInt(br.readLine());

            for (int i = 0; i < taskCount; i++) {
                String input = br.readLine();
                System.out.println(hahaThatsFunny(input) ? "Funny" : "Not Funny");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hahaThatsFunny(String input) {
        int charCount = input.length();
        for (int j = 1; j <= charCount-1; j++) {
            if (abs(input.charAt(j) - input.charAt(j - 1)) !=
                    abs(input.charAt(charCount-j) - input.charAt(charCount-j-1))) {
                return false;
            }
        }
        return true;
    }
}
