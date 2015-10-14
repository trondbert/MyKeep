package moderate.euler095_amicable_chains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.min;
import static java.lang.Math.pow;

public class SolutionGraphSearch {
    TreeSet<Integer> primes = new TreeSet<>();

    boolean[]   excluded;
    int[]       chainBases;
    short[]     chainLengths;
    int[]       divSums;
    short[]     maxFactorCounts;
    int[]       maxFactors;
    int[]       smallerDivsSum;

    int         maxChainMember;
    short       maxChainLength = 0;
    int         minBase = 0;
    short       s1 = (short)1;
    int         maxPrime;

    private static String readLine() {
        try (InputStreamReader isr = new InputStreamReader(System.in);
             BufferedReader br = new BufferedReader(isr)) {
            return br.readLine();
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    public SolutionGraphSearch(Integer chainLimit) {
        maxChainMember      = chainLimit;
        excluded            = new boolean[maxChainMember + 1];
        maxFactors          = new int[maxChainMember + 1];
        smallerDivsSum      = new int[maxChainMember + 1];
        divSums             = new int[maxChainMember + 1];
        maxFactorCounts     = new short[maxChainMember + 1];
        chainLengths        = new short[maxChainMember + 1];
        chainBases          = new int[maxChainMember + 1];

        for (int i = 0; i < maxChainMember +1; i++) {
            divSums[i] = i + 1;
            maxFactors[i] = MAX_VALUE;
        }
        Arrays.fill(chainBases, maxChainMember);
        primes.add(2); primes.add(3);
        for (Integer prime : primes) {
            excluded[prime] = true;
        }
        excluded[1] = true;
    }

    public static void main(String[] args) {
        int maxChainMember = Integer.parseInt(readLine());

        SolutionGraphSearch solution = new SolutionGraphSearch(maxChainMember);

        solution.maxPrime = solution.maxChainMember / 2;
        solution.generateVisits();
        solution.findChains();
        System.out.println(solution.minBase);
    }

    private void findChains() {
        for ( int currVisit = 1 ; currVisit <= maxChainMember; currVisit++) {
            if (excluded[currVisit]) continue;

            ArrayList<Integer> chain = new ArrayList<>();
            chain.add(currVisit);
            int baseFound;
            baseFound = followChain(chain, currVisit);
            if (baseFound < 0) continue;

            short chainLength = (short) (chain.size() - baseFound - 1);
            if (chainLength >= maxChainLength) {
                minBase = chainLength > maxChainLength ? chain.get(baseFound)
                                                       : min(minBase, chain.get(baseFound));
                maxChainLength = chainLength;
            }
            excluded[currVisit] = true;
        }
    }

    private void generateVisits() {
        maxFactors[1] = 1;
        maxFactorCounts[1] = 1;
        smallerDivsSum[1] = 1;
        divSums[1] = 1;

        while (!primes.isEmpty()) {
            multiplyWithNewPrimes();
            findNewPrimes();
        }
    }

    private void multiplyWithNewPrimes() {
        for (Integer prime : primes) {
            int maxVisit    = maxChainMember / prime;
            for (int currVisit = 1; currVisit <= maxVisit; currVisit++) {
                Integer maxFactor       = maxFactors[currVisit];
                if (prime < maxFactor) continue;

                int newVisit = currVisit * prime;
                int sumDiv = divSums[currVisit];
                maxFactorCounts[newVisit] = prime.equals(maxFactor) ? (short) (maxFactorCounts[currVisit] + s1) : s1;
                maxFactors[newVisit] = prime;
                smallerDivsSum[newVisit] = (prime.equals(maxFactor)) ? smallerDivsSum[currVisit] : sumDiv;
                divSums[newVisit] = (int) (smallerDivsSum[newVisit] * pow(prime, maxFactorCounts[newVisit])
                        + sumDiv);
                if (this.divSums[newVisit] - newVisit > this.maxChainMember) {
                    excluded[newVisit] = true;
                }
            }
        }
    }

    private void findNewPrimes() {
        int firstPrime = primes.last() + 2;
        while (maxFactors[firstPrime] != MAX_VALUE) {
            firstPrime++;
        }
        int maxCandPrime = min(maxPrime, firstPrime * 2 - 1);

        primes.clear();
        for (int jump = 2; firstPrime <= maxCandPrime; jump = 6-jump, firstPrime += 2) {
            if (maxFactors[firstPrime] == MAX_VALUE)
                primes.add(firstPrime);
        }
    }

    private int followChain(ArrayList<Integer> chain, int currVisit) {
        int nextInChain = divSums[currVisit] - currVisit;
        if (excluded[nextInChain]) return -1;

        int result;
        boolean loop = chain.contains(nextInChain);
        chain.add(nextInChain);
        if (loop)
            result = chain.indexOf(nextInChain);
        else
            result = followChain(chain, nextInChain);

        excluded[nextInChain] = true;
        return result;
    }
}
