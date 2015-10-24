package trond.java;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

/**
 * @author trond.
 */
public class MagiskKvadratTest {

    @Test
    public void testName() throws Exception {

        Set<Integer[][]> intsSet = new TreeSet<>(this::compareIntegerArrays);

        Integer[][] ints1 = { new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 3 } };
        Integer[][] ints2 = { new Integer[] { 1, 2, 3 }, new Integer[] { 1, 2, 9 } };

        intsSet.add(ints1);
        intsSet.add(ints2);

        System.out.println(intsSet.size());
    }

    int compareIntegerArrays(Integer[][] a, Integer[][] b) {
        for (int outerIndex = 0; outerIndex < a.length; outerIndex++) {
            for (int innerIndex = 0; innerIndex < a[0].length; innerIndex++) {
                if (a[outerIndex][innerIndex] != b[outerIndex][innerIndex]) {
                    return a[outerIndex][innerIndex] - b[outerIndex][innerIndex];
                }
            }
        }
        return 0;
    }
}
