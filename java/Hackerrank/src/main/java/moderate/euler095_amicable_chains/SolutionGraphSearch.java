package moderate.euler095_amicable_chains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;
import static java.lang.Math.pow;

public class SolutionGraphSearch {

    static BufferedReader br;
    static InputStreamReader isr;
    List<Integer> primes = new ArrayList<>(500);

    boolean[]   excluded;
    int[]       maxFactor;
    short[]     maxFactorCount;
    int[]       sumSmallerDiv;
    int[]       sumDiv;
    short[]     chainLength;
    int[]       chainBase;
    int         maxChainMember;

    int         minBase = 0;
    short       maxChainLength = 0;
    short       s1 = (short)1;

    static boolean debug = false;

    private static String readLine() { try { return br.readLine(); }
    catch (IOException e) { throw new RuntimeException(e); }}

    public SolutionGraphSearch(Integer maxChainMember) {
        this.maxChainMember = maxChainMember;
        excluded =      new boolean[maxChainMember + 1];
        maxFactor =      new int[maxChainMember + 1];
        sumSmallerDiv =  new int[maxChainMember + 1];
        sumDiv =         new int[maxChainMember + 1];
        maxFactorCount = new short[maxChainMember + 1];
        chainLength =   new short[maxChainMember + 1];
        chainBase    =   new int[maxChainMember + 1];

        for (int i = 0; i < sumDiv.length; i++) {
            sumDiv[i] = i + 1;
        }
        Arrays.fill(chainBase, maxChainMember);
        primes.add(2); primes.add(3);
        for (Integer prime : primes) {
            exclude(prime);
        }
        exclude(1);
    }

    public static void main(String[] args) {
        isr = new InputStreamReader(System.in); br = new BufferedReader(isr);
        long startTime = System.currentTimeMillis();
        Integer maxChainMember = Integer.parseInt(readLine());
        SolutionGraphSearch solution = new SolutionGraphSearch(maxChainMember);

        solution.solve();
        System.out.println(solution.minBase);
        long endTime = System.currentTimeMillis();
        if (debug)
            System.out.println(String.format("%.1f", (endTime-startTime) / 1000.0));
    }

    private int solve() {
        int maxPrime = maxChainMember / 12;
        generatePrimes(maxPrime);

        int currVisit = 1;
        maxFactor[currVisit] = 1;
        maxFactorCount[currVisit] = 1;
        sumSmallerDiv[currVisit] = 1;
        sumDiv[currVisit] = 1;

        generateVisits();
        findChains();

        return 0;
    }

    private void findChains() {
        int currVisit;
        for ( currVisit = 1 ; currVisit < maxChainMember; currVisit++) {
            if (excluded[currVisit]) continue;

            ArrayList<Integer> chain = new ArrayList<>();
            int baseFound = followChain(chain, currVisit);
            if (baseFound < 0) continue;

            short chainLength = (short) chain.size();
            if (chainLength >= maxChainLength) {
                minBase = chainLength > maxChainLength ? chain.get(baseFound)
                                                       : min(minBase, chain.get(baseFound));
                maxChainLength = chainLength;
            }
            exclude(currVisit);
        }
    }

    private void generateVisits() {
        int currVisit;
        for ( currVisit = 1 ; currVisit < maxChainMember; currVisit++) {
            int sumDivCurr          = sumDiv[currVisit];
            int maxFactorCurrVisit  = maxFactor[currVisit];
            int maxPrimeForVisit    = maxChainMember / currVisit;
            int newVisit = 0;
            for (Integer prime : primes) {
                if (prime < maxFactorCurrVisit) continue;
                newVisit = currVisit * prime;
                if (prime > maxPrimeForVisit) break;

                maxFactorCount[newVisit] = prime == maxFactorCurrVisit ?
                        (short)(maxFactorCount[currVisit] + s1) : s1;
                maxFactor[newVisit] = prime;
                sumSmallerDiv[newVisit] = prime == maxFactorCurrVisit ?
                        sumSmallerDiv[currVisit] : sumDivCurr;
                sumDiv[newVisit] = (int) (sumSmallerDiv[newVisit] * pow(prime, maxFactorCount[newVisit])
                        + sumDivCurr);

                if (sumDiv[newVisit] - newVisit > maxChainMember) {
                    exclude(newVisit);
                }
            };
        }
    }

    private void exclude(int visit) {
        excluded[visit] = true;
    }

    private int followChain(ArrayList<Integer> chain, int currVisit) {
        chain.add(currVisit);
        int nextInChain = sumDiv[currVisit] - currVisit;
        if (excluded[nextInChain]) return -1;
        boolean outOfBounds = nextInChain <= 1 || nextInChain > maxChainMember;
        if (outOfBounds) {
            return -1;
        }
        if (chain.contains(nextInChain)) {
            exclude(nextInChain);
            return chain.indexOf(nextInChain);
        }
        //System.out.println(nextInChain)
        int result = followChain(chain, nextInChain);
        exclude(nextInChain);
        return result;
    }


    SolutionGraphSearch generatePrimes(int maxPrime) {
        int candidate = primes.get(primes.size()-1) + 2;
        int largestPrime = 0;
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
            if (prime) {
                primes.add(candidate);
                exclude(candidate);
                largestPrime = candidate;
            }
            candidate += 2;
        } while ( largestPrime < maxPrime);
        return this;
    }
}
