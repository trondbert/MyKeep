package hackerrank.easy.diagonaldifference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

    public static void main(String[] args) {
        try (InputStreamReader isr = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(isr)) {
            int squareSize = Integer.parseInt(br.readLine());

            int northWestSum = 0, northEastSum = 0;

            for (int i = 0; i < squareSize; i++) {
                String[] line = br.readLine().split(" ");
                northWestSum += Integer.parseInt(line[i]);
                northEastSum += Integer.parseInt(line[squareSize-1-i]);
            }
            System.out.println(Math.abs(northEastSum - northWestSum));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
