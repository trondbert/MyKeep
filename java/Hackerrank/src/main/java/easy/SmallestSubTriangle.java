package easy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class SmallestSubTriangle {

    private final boolean DEBUG = false;

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
            ArrayList<Integer> minimum = new SmallestSubTriangle().solve(input, k);

            for (Integer min : minimum) {
                System.out.println(min);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> solve(ArrayList<List<Integer>> inputState, int k) {
        int taskSize = inputState.size();
        ArrayList<Integer> dummyRow = new ArrayList<>(taskSize + 1);
        for (int i = 0; i < taskSize + 1; i++) { dummyRow.add(0); }
        inputState.add(dummyRow);


        ArrayList<Integer> minimum = new ArrayList<>(k);
        for (int i = 0; i < k; i++) { minimum.add(Integer.MAX_VALUE); }
        Integer greatestMinimum         = minimum.get(k - 1);
        for (int i = 0; i < taskSize + 1; i++) {
            for (int j = 0; j <= i; j++) {
                if (inputState.get(i).get(j) < greatestMinimum) {
                    minimum.set(k - 1, inputState.get(i).get(j));
                    Collections.sort(minimum);
                    greatestMinimum = minimum.get(k - 1);
                }
            }
        }

        ArrayList<List<Integer>> state = deepClone(inputState);
        ArrayList<List<Integer>> state2 = deepClone(state);
        ArrayList<List<Integer>> state3 = allZero(taskSize + 1);

        for (int height = 2; height <= taskSize; height++) {

            for (int row = taskSize - height; row >= 0; row--) {
                for (int col = 0; col <= row; col++) {
                    Integer triangleLeft  = state2.get(row + 1).get(col);
                    Integer triangleRight = state2.get(row + 1).get(col + 1);
                    Integer shared        = state3.get(row + 2).get(col + 1);
                    int candidate         = state.get(row).get(col) + triangleLeft + triangleRight - shared;
                    state.get(row).set(col, candidate);

                    if (candidate < greatestMinimum) {
                        minimum.set(k - 1, candidate);
                        Collections.sort(minimum);
                        greatestMinimum = minimum.get(k - 1);
                    }
                }
            }

            if (DEBUG) {
                printTriangle(state, "State");
                printTriangle(state2, "State2");
                printTriangle(state3, "State3");
            }

            reset(state3, inputState);
            ArrayList<List<Integer>> tmpState = state3;
            state3 = state2;
            state2 = state;
            state = tmpState;
        }
        return minimum;
    }

    private void reset(ArrayList<List<Integer>> state, ArrayList<List<Integer>> inputState) {
        for (int i = 0; i < state.size(); i++) {
            List<Integer> row = state.get(i);
            List<Integer> inputRow = inputState.get(i);
            for (int j = 0; j < row.size(); j++) {
                row.set(j, inputRow.get(j));
            }
        }
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
}
