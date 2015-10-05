package moderate.euler095_amicable_chains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Solution {
    BufferedReader br; InputStreamReader isr;
    TreeSet<Integer> excluded = new TreeSet<>();
    HashMap<Integer, Set<Integer>> divisorsMap = new HashMap<>();
    HashMap<Integer, Integer> divisorsSumMap = new HashMap<>();

    private String readLine() { try { return br.readLine(); }
        catch (IOException e) { throw new RuntimeException(e); }}

    public Solution() {
        isr = new InputStreamReader(System.in); br = new BufferedReader(isr);
    }

    public static void main(String[] args) {
        new Solution().solve();
    }

    private void solve() {
        Integer maxChainMember = Integer.parseInt(readLine());

        int currentBase = 6;
        int maxChainLength = 0;
        int smallestBaseOfLongestChain = 6;

        while (currentBase <= maxChainMember) {
            List<Integer> chain = getChain(currentBase, maxChainMember);
            if (chain.size() > maxChainLength) {
                maxChainLength = chain.size();
                smallestBaseOfLongestChain = chain.get(0);
            }
            do {
                currentBase++;
            } while (excluded.contains(currentBase)); //Don't evaluate again
        }
        System.out.println(smallestBaseOfLongestChain);
    }

    private List<Integer> getChain(int chainBase, int maxMember) {
        List<Integer> members = new ArrayList<>();
        boolean foundBase = buildChain(members, chainBase, maxMember);

        excluded.addAll(members);
        if (!foundBase) {
            return new ArrayList<Integer>();
        }
        return members;
    }

    private boolean buildChain(List<Integer> members, int chainBase, int maxMember) {
        int currentMember = chainBase;
        do {
            currentMember = sumOfDivisors(currentMember);
            if (excluded.contains(currentMember)) return false;
            if (members.contains(currentMember)) {
                int basePos = members.indexOf(currentMember);
                for (int i = 0; i < basePos; i++) {
                    excluded.add(members.remove(0));
                }
                return true;
            }
            members.add(currentMember);
        } while (currentMember != chainBase && currentMember <= maxMember);

        if (currentMember == chainBase) {
            members.add(0, chainBase);
            return true;
        }
        return false;
    }

    private int sumOfDivisors(int base) {
        if (divisorsMap.containsKey(base)) {
            return divisorsSumMap.get(base);
        }

        TreeSet<Integer> divisors = new TreeSet<>();
        int divisor = Math.max(1, base / 2);
        do {
            while (divisor > 1 && base % divisor != 0) {
                divisor--;
            }
            divisors.add(divisor);
            if (divisorsMap.containsKey(divisor)) {
                divisors.addAll(divisorsMap.get(divisor));
            }
            divisor--;
        } while (divisor >= 1);

        Integer sum = 0;
        for (Integer integer : divisors) {
            sum += integer;
        }
        divisorsMap.put(base, divisors);
        divisorsSumMap.put(base, sum);
        return sum;
    }
}
