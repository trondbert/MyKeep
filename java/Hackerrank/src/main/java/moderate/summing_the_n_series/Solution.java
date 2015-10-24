package moderate.summing_the_n_series;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Math.pow;

public class Solution {


    private static final Long modBase = (long) (pow(10, 9) + 7);

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            Long tasksCount = Long.valueOf(br.readLine());
            for (int i = 0; i < tasksCount; i++) {
                solve(Long.valueOf(br.readLine()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void solve(Long n) {
        long nMod = n % modBase;

        System.out.println((nMod * nMod) % modBase);
    }
}
