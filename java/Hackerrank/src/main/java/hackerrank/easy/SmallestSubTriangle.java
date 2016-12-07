package hackerrank.easy;

import com.sun.istack.internal.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class SmallestSubTriangle {

    private final boolean DEBUG = System.getProperty("subtriangles.debug") != null;

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String[] split = br.readLine().split(" ");
            int n = Integer.parseInt(split[0]);
            int k = Integer.parseInt(split[1]);

            ArrayList<List<Integer>> input = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                ArrayList<Integer> row = new ArrayList<>();
                input.add(row);
                row.addAll(asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(toList()));
            }
            List<Integer> minimum = new SmallestSubTriangle().solve(input, k);

            for (Integer min : minimum) {
                System.out.println(min);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Integer> liste = new LinkedList<>();
        Collections.binarySearch(liste, 4);

    }

    public List<Integer> solve(ArrayList<List<Integer>> inputState, int k) {
        int taskSize = inputState.size();
        ArrayList<Integer> dummyRow = new ArrayList<>(taskSize + 1);
        for (int i = 0; i < taskSize + 1; i++) { dummyRow.add(0); }
        inputState.add(dummyRow);

        List<Integer> minimum = new LinkedList<>();
        for (int i = 0; i < k; i++) { minimum.add(Integer.MAX_VALUE); }
        Integer greatestMinimum         = minimum.get(k - 1);
        Integer newMin;

        for (int i = 0; i < taskSize + 1; i++) {
            for (int j = 0; j <= i; j++) {
                if (inputState.get(i).get(j) < greatestMinimum) {
                    newMin = inputState.get(i).get(j);
                    minimum.add(findEqualOrHigher(minimum, newMin), newMin);
                    minimum.remove(k);
                    greatestMinimum = minimum.get(k - 1);
                }
            }
        }

        ArrayList<List<Integer>> state1 = deepClone(inputState);
        ArrayList<List<Integer>> state2 = deepClone(state1);
        ArrayList<List<Integer>> state3 = allZero(taskSize + 1);
        ArrayList<List<Integer>> tmpState;

        List<Integer> rowFromLastRound;
        List<Integer> rowFromRoundBeforeLast;
        List<Integer> inputRow;
        List<Integer> outputRow;
        Integer triangleLeft;
        Integer triangleRight;
        Integer shared;
        Integer candidate;
        for (int height = 2; height <= taskSize; height++) {
            for (int row = taskSize - height; row >= 0; row--) {
                rowFromLastRound = state2.get(row + 1);
                rowFromRoundBeforeLast = state3.get(row + 2);
                inputRow = inputState.get(row);
                outputRow = state1.get(row);
                for (int col = 0; col <= row; col++) {
                    triangleLeft    = rowFromLastRound.get(col);
                    triangleRight   = rowFromLastRound.get(col + 1);
                    shared          = rowFromRoundBeforeLast.get(col + 1);
                    candidate       = inputRow.get(col) + triangleLeft + triangleRight - shared;
                    outputRow.set(col, candidate);

                    if (candidate < greatestMinimum) {
                        minimum.add(findEqualOrHigher(minimum, candidate), candidate);
                        minimum.remove(k);
                        greatestMinimum = minimum.get(k - 1);

                        /*minimum.set(k - 1, candidate);
                        Collections.sort(minimum);
                        greatestMinimum = minimum.get(k - 1);*/
                    }
                }
            }

            if (DEBUG) {
                printTriangle(state1, "State1");
                printTriangle(state2, "State2");
                printTriangle(state3, "State3");
                printTriangle(inputState, "InputState");
            }
            tmpState = state3;
            state3 = state2;
            state2 = state1;
            state1 = tmpState;
        }
        return minimum;
    }

    private void printTriangle(ArrayList<List<Integer>> state, String heading) {
        System.out.println(heading + ":");
        for (List<Integer> row : state) {
            for (Integer cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private ArrayList<List<Integer>> deepClone(ArrayList<List<Integer>> original) {
        ArrayList<List<Integer>> clone = new ArrayList<>(original.size());

        for (List<Integer> row : original) {
            ArrayList<Integer> newRow = new ArrayList<>(row.size());
            clone.add(newRow);
            for (Integer integer : row) {
                newRow.add(integer);
            }
        }
        return clone;
    }

    private ArrayList<List<Integer>> allZero(int size) {
        ArrayList<List<Integer>> zeros = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ArrayList<Integer> row = new ArrayList<>(i);
            zeros.add(row);
            for (int j = 0; j <= i; j++) {
                row.add(0);
            }}
        return zeros;
    }

    /* Returns the position to insert a number at so that the list is still ordered by increasing numbers.
     * Takes an ordered list and a number. */
    private int findEqualOrHigher(@NotNull List<Integer> list, Integer value) {
        if (list.isEmpty())
            return 0;

        int minPos = 0;
        int maxPos = list.size() - 1;

        int pos = minPos + ((maxPos - minPos) / 2);

        while (minPos != maxPos) {
            if (list.get(pos) < value) {
                minPos = pos + 1;
            } else {
                maxPos = pos;
            }
            pos = minPos + ((maxPos - minPos) / 2);
        }
        return pos == list.size() ? pos :
                list.get(pos) >= value ? pos : -1;
    }
}

