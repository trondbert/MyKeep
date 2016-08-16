package moderate.majorityoftheballs;

import java.util.Scanner;

class Solution {

    static void nextQuestion(int n, int plularity, int lies, int color, int exact_lies, int[][] query) {
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int plularity = in.nextInt();
        int lies = in.nextInt();
        int color = in.nextInt();
        int exact_lies = in.nextInt();
        int query_size = in.nextInt();
        int[][] query = new int[n][n];
        int x, y;
        String answer;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                query[i][j] = -1;
            }
        }

        for (int i = 0; i < query_size; i++) {
            x = in.nextInt();
            y = in.nextInt();
            answer = in.next();
            if (answer.equals("YES") == true) {
                query[x][y] = 1;
                query[y][x] = 1;
            }
            else {
                query[x][y] = 0;
                query[y][x] = 0;
            }
        }
        nextQuestion(n, plularity, lies, color, exact_lies, query);
    }
}
