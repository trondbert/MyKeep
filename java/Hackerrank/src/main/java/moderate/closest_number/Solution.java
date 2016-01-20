package moderate.closest_number;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Long.valueOf;

public class Solution {


    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int tasksCount = Integer.parseInt(br.readLine());
            for (int i = 0; i < tasksCount; i++) {
                String[] strings = br.readLine().split(" ");
                long a = valueOf(strings[0]);
                long b = valueOf(strings[1]);
                long x = valueOf(strings[2]);

                double ratio = Math.pow(a, b) / x;

                if (ratio - Math.floor(ratio) < 0.5)
                    System.out.println(x * (int) ratio);
                else
                    System.out.println(x * (((int) ratio) + 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
