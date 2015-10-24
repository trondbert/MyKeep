package moderate.jumpingbunnies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

class Solution {

    public static void main(String... args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.readLine();
            Stream<Integer> jumps = asList(br.readLine().split(" ")).stream().map(Integer::parseInt);
            solve(jumps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void solve(Stream<Integer> jumps) {
        List<Integer>               sortedJumps         = jumps.sorted().collect(toList());
        Map<Integer, Integer>       primeFactorsList    = primeFactors(sortedJumps);

        Stream<Double> factorPowers = primeFactorsList.keySet().stream().map(key ->
                Math.pow(key, primeFactorsList.get(key)));
        System.out.println("end: " + factorPowers.reduce((a, b) -> a * b).get());
    }

    static Map<Integer, Integer> primeFactors(List<Integer> numbersIn) {
        List<Integer> numbers = new ArrayList<>(numbersIn);
        Map<Integer, Integer> result = new HashMap<>();

        Integer maxFactor = 1 + apply(it -> it % 2 == 0 ? it - 1 : it,
                numbers.get(numbers.size() - 1));
        int factor = 2;
        while (factor <= maxFactor) {
            int maxFactorMultiples = 0;
            for (int i = 0; i < numbers.size(); i++) {
                int factorExponent = 0;
                Integer number = numbers.get(i);
                while (number % factor == 0) {
                    number = number / factor;
                    factorExponent++;
                }
                numbers.set(i, number);
                maxFactorMultiples = max(maxFactorMultiples, factorExponent);
            }
            if (maxFactorMultiples > 0)
                result.put(factor, maxFactorMultiples);

            factor = factor == 2 ? 3 : factor + 2;
        }
        return result;
    }

    static <T> T apply(Function<T, T> function, T value) {
        return function.apply(value);
    }
}
