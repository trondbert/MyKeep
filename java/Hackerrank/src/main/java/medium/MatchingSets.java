package medium;
import java.io.*;
import java.util.*;

public class MatchingSets {

        static int[] x;
        static int[] y;

        public static void main(String[] args) {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            try {
                br.readLine(); //Discarded
                String[] xStr = br.readLine().split(" ");
                String[] yStr = br.readLine().split(" ");

                x = new int[xStr.length];
                y = new int[xStr.length];
                for (int i = 0; i < xStr.length; i++) {
                    x[i] = Integer.parseInt(xStr[i]);
                    y[i] = Integer.parseInt(yStr[i]);
                }
                Arrays.sort(x);
                Arrays.sort(y);
                System.out.println(y[1]);

                for (int a = 0; a < xStr.length; a++) {
                    if (x[a] == y[a]) continue;

                    int diffA = y[a] - x[a];
                    int b = findFirstPartner(x, a);
                    if (b == -1) { System.out.println("-1"); return;}
                    int moves = Math.min(Math.abs(diffA), Math.abs(y[b] - x[b]))
                            * sign(diffA);
                }
            } catch (IOException e) {

            }
        }

        static int findFirstPartner(int[] x, int a) {
            int diffA = y[a] - x[a];
            int signDiffA = sign(diffA);
            for (int b = a+1; b < x.length; b++) {
                int diffB = y[b] - x[b];
                if (diffB != 0 && sign(diffB) != signDiffA) {
                    return b;
                }
            }
            return -1;
        }

        static int sign(int i) { return i == 0 ? 0 : i > 0 ? 1 : -1; }
    }
