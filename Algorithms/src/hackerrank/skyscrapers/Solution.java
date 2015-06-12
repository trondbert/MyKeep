package hackerrank.skyscrapers;

import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        System.out.println(solve());
    }

    private static Integer solve() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            bufferedReader.readLine();
            return solve(bufferedReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer solve(String task) {
        List<Integer> nums = new ArrayList<>();
        Arrays.asList(task.split(" ")).forEach((String val) -> nums.add(Integer.parseInt(val)));
        Map<Integer, List<Integer>> runs = buildRuns(nums);

        return combinations(runs);
    }

    private static Integer combinations(Map<Integer, List<Integer>> runs) {
        int jumps = 0;
        for (Integer height : runs.keySet()) {
            for (Integer run : runs.get(height)) {
                if (run > 1) {
                    jumps += run*run - run;
                }
            }
        }
        return jumps;
    }

    private static Map<Integer, List<Integer>> buildRuns(List<Integer> nums) {
        Map<Integer, List<Integer>> runs = new TreeMap<Integer, List<Integer>>();

        for (Integer height : nums) {
            addOneMoreToRun(runs, height);

            restartLowerHeightRuns(runs, height);
        }
        return runs;
    }

    private static void restartLowerHeightRuns(Map<Integer, List<Integer>> runs, Integer height) {
        Iterator<Integer> iterator = runs.keySet().iterator();
        while (iterator.hasNext()) {
            Integer lowerHeight = iterator.next();
            if (lowerHeight < height) {
                runs.get(lowerHeight).add(0);
            } else {
                break;
            }
        }
    }

    private static void addOneMoreToRun(Map<Integer, List<Integer>> runs, Integer height) {
        if (runs.get(height) == null) {
            ArrayList<Integer> list = new ArrayList<>();
            list.add(1);
            runs.put(height, list);
        }
        else {
            List<Integer> intervaller = runs.get(height);
            intervaller.set(intervaller.size()-1,
                    intervaller.get(intervaller.size() - 1) + 1);
        }
    }
}

class Intervall {
    Integer from;
    Integer to;

    Intervall to(Integer to) {
        this.to = to; return this;
    }
    Intervall from(Integer from) {
        this.from = from; return this;
    }
}