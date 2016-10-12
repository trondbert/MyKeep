package medium;

import java.io.*;
import java.util.*;

public class  MatchingSets {

    public static void main(String[] args) {
        System.out.println(
            solve());
    }

    static int solve() {
        try {
            SortedInputArrays input = readTask();
            return solve(input);
        } catch (IOException e) {
            return -1;
        }
    }

    static int solve(SortedInputArrays input) throws IOException {
        int[] x = input.x;
        int[] y = input.y;

        int movesTotal = 0;
        for (int a = 0; a < x.length; a++) {
            do {
                int diffA = y[a] - x[a];
                if (diffA == 0) break;

                int moves = modifyX(input, a);
                movesTotal += moves;
                if (moves == -1)
                    return -1;
            } while (true);
        }
        return movesTotal;
    }

    static int modifyX(SortedInputArrays input, int a) {
        int diffA = input.y[a] - input.x[a];
        int b = findFirstPartner(input, a);
        if (b == -1) {
            return -1;
        }

        int moves = Math.min(Math.abs(diffA), Math.abs(input.y[b] - input.x[b]));
        int increaseForA = moves * sign(diffA);
        input.x[a] += increaseForA;
        input.x[b] -= increaseForA;

        return moves;
    }

    static SortedInputArrays readTask() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        br.readLine(); //Discarded
        String[] xRaw = br.readLine().split(" ");
        String[] yRaw = br.readLine().split(" ");

        ArrayList<String> xMembers = new ArrayList<>(Arrays.asList(xRaw));
        ArrayList<String> yMembers = new ArrayList<>(Arrays.asList(yRaw));

        List<String> xCopy = new ArrayList<>(xMembers);
        for (String s : xCopy) {
            if (yMembers.contains(s)) {
                xMembers.remove(s);
                yMembers.remove(s);
            }
        }

        int[] x = new int[xMembers.size()];
        int[] y = new int[xMembers.size()];
        for (int i = 0; i < x.length; i++) {
            x[i] = Integer.parseInt(xMembers.get(i));
            y[i] = Integer.parseInt(yMembers.get(i));
        }
        Arrays.sort(x);
        Arrays.sort(y);

        SortedInputArrays inputArrays = new SortedInputArrays();
        inputArrays.x = x;
        inputArrays.y = y;

        return inputArrays;
    }

    static int findFirstPartner(SortedInputArrays input, int a) {
        int[] x = input.x;
        int[] y = input.y;

        int diffA = y[a] - x[a];
        int signDiffA = sign(diffA);
        for (int b = a + 1; b < x.length; b++) {
            int diffB = y[b] - x[b];
            if (diffB != 0 && sign(diffB) != signDiffA) {
                return b;
            }
        }
        return -1;
    }

    static int sign(int i) {
        return i == 0 ? 0 : i > 0 ? 1 : -1;
    }
}

class SortedInputArrays {
    int[] x;
    int[] y;
}