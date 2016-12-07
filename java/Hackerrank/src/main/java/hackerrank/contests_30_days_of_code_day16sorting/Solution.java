package hackerrank.contests_30_days_of_code_day16sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://www.hackerrank.com/contests/30-days-of-code/challenges/day-16-closest-numbers
 */
public class Solution {

    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            br.readLine(); // No need for this
            final String[] strings = br.readLine().split(" ");
            final ArrayList<Long> task = new ArrayList<>();
            for (final String string : strings) {
                task.add(Long.valueOf(string));
            }

            System.out.println(solve(task));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String solve(final ArrayList<Long> task) {
        task.sort(Long::compareTo);

        long minAbs = Long.MAX_VALUE;
        final List<Long> result = new ArrayList<>();

        for (int i = 0; i < task.size(); i++) {
            for (int j = i + 1; j < task.size(); j++) {
                final long abs = Math.abs(task.get(j) - task.get(i));
                if (abs <= minAbs) {
                    if (abs < minAbs)
                        result.clear();
                    minAbs = abs;
                    result.add(task.get(i));
                    result.add(task.get(j));
                } else {
                    break;
                }
            }
        }
        result.sort(Long::compare);
        return result.stream().map(Object::toString).collect(Collectors.joining(" "));
    }
}


