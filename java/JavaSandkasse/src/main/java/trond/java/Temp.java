package trond.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by trond on 01/12/15.
 */
public class Temp {

    private static int baseX_0 = 1;

    private static int baseX_1 = 1;

    public static void main(String[] args) {
        System.out.println((125 + 26) & (int) Math.pow(5, 2));
        /*
        System.out.println(new Temp().getCombinationString(new Integer[] { 1, 1, 0, 0 }, new Integer[] { 1, 0, 1, 0 },
                                                           new Integer[] { 2, 0, 3, 0 }, new Integer[] { 0, 2, 0, 0 }));
                                                           */
    }

    private String getCombinationString(Integer[] row1, Integer[] row2, Integer[] row3, Integer[] row4) {
        Function<List<Integer[]>, Stream<String>> foo = (list) -> {
            Stream<String> integerStream = list.stream().map(intArr -> {
                ArrayList<Integer> digits = new ArrayList<Integer>() {{ addAll(Arrays.asList(intArr)); }};
                return digits.stream().sorted().map(String::valueOf).reduce("", (a, b) -> a + b);
            });
            return integerStream;
        };
        return foo.apply(Arrays.asList(row1, row2, row3, row4)).distinct().sorted().reduce("", (a, b) -> a + b);
    }
}
