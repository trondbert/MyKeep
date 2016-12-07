package hackerrank.easy.finddigits;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

    public static void main(String[] args) {
        try (                                       InputStreamReader isr = new InputStreamReader(System.in);
                                                    BufferedReader br = new BufferedReader(isr)) {
            int taskCount = Integer.parseInt(br.readLine());
            for (int i = 0; i < taskCount; i++) {
                solveNextLine(br);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solveNextLine(BufferedReader br) throws IOException {
        String numberStr = br.readLine();
        long number = Long.parseLong(numberStr);

        int result = 0;
        char[] chars = numberStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int digit = Integer.parseInt("" + chars[i]);
            if (digit != 0 && number % digit == 0) {
                result++;
            }
        }
        System.out.println(result);
    }
}
