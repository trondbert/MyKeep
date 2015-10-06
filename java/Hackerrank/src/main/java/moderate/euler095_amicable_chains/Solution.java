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
    List<Integer> primes = new ArrayList<>();
    private Set<Integer> emptyIntegerSet;

    private String readLine() { try { return br.readLine(); }
        catch (IOException e) { throw new RuntimeException(e); }}

    public Solution() {
        isr = new InputStreamReader(System.in); br = new BufferedReader(isr);
        divisorsSumMap.put(1, 1);
        emptyIntegerSet = Collections.emptySet();
        divisorsMap.put(1, emptyIntegerSet);
        primes.add(2); primes.add(3);
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
            currentMember = sumDivisors2(currentMember);
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
        int initialDivisor = 1;
        do {
            initialDivisor++;
        }
        while (base % initialDivisor != 0);
        int divisor = Math.max(1, base / initialDivisor--);
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

    int sumDivisors2(int base) {
        if (base == 1) return 1;

        Set<Integer> divisors = divisorsRaw(base);
        divisors.remove(base);
        divisors.add(1);
        int sum = 0;
        for (Integer divisor : divisors) {
            sum += divisor;
        }
        return sum;
    }

    Set<Integer> divisorsRaw(int base) {
        if (divisorsMap.containsKey(base)) return divisorsMap.get(base);
        TreeSet<Integer> divisors = new TreeSet<>();
        divisors.add(base);
        int primesIndex = 0;
        int divisor;
        do {
            divisor = primes.size() > primesIndex ? primes.get(primesIndex) : findNewPrime();
            primesIndex++;
        }
        while (base % divisor != 0);
        if (divisor != base && divisor != 1) {
            divisors.add(divisor);
            int remainder = base / divisor;
            divisors.add(remainder);

            Set<Integer> rightSet = divisorsRaw(remainder);
            divisors.addAll(rightSet);
            for (Integer factorRight : rightSet) {
                divisors.add(divisor * factorRight);
            }
        }
        return divisors;
    }

    int findNewPrime() {
        int candidate = primes.get(primes.size()-1) + 2;
        boolean prime;
        do {
            Integer maxDivisor = (int)Math.sqrt(candidate);
            prime = true;
            for (Integer primeI : primes) {
                if (primeI > maxDivisor) break;
                if (candidate % primeI == 0) {
                    prime = false;
                    break;
                }
            }
        } while ( ! prime && (candidate += 2) != 0);
        primes.add(candidate);
        return candidate;
    }
}
