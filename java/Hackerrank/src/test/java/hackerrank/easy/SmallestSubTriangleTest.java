package hackerrank.easy;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by trond on 16/08/16.
 */
public class SmallestSubTriangleTest {

    @Before
    public void setup() {
        //System.setProperty("subtriangles.debug", "yes");
    }

    @Test
    public void testIt() {
        ArrayList<List<Integer>> task = new ArrayList<>(4);
        task.add(Arrays.asList(1));
        task.add(Arrays.asList(-4, 6));
        task.add(Arrays.asList(-3, 6, 9));
        task.add(Arrays.asList(-2, -4, 8, -3));

        new SmallestSubTriangle().solve(task, 3);
    }

    @Test
    public void bigTest() {
        int N = 350;
        ArrayList<List<Integer>> task = new ArrayList<>(N);
        double entryLimit = Math.pow(10, 5);
        for (int i = 0; i < N; i++) {
            ArrayList<Integer> row = new ArrayList<>(i);
            task.add(row);
            for (int j = 0; j < N; j++) {
                row.add((int) (-1 * entryLimit + Math.random() * 2 * entryLimit));
            }
        }
        int K = (int) (Math.pow(10, 4) * 5);
        long timeStart = System.currentTimeMillis() / 100;
        System.out.println(timeStart);
        new SmallestSubTriangle().solve(task, K);

        long desiSecondsSpent = (System.currentTimeMillis() / 100) - timeStart;
        System.out.println(desiSecondsSpent / 10  + "." + desiSecondsSpent % 10 + " s");
    }

    @Test
    public void adhoc() {
        ArrayList<String> strings = new ArrayList<>();
        Integer integer = new Integer(2);
        System.out.println(strings.contains(integer));


        int i = 0;
        ArrayList<List<Integer>> original = new ArrayList<List<Integer>>() {{
            add(new ArrayList<Integer>() {{
                add(1);
                add(2);
            }});
        }};
        @SuppressWarnings("unchecked")
        List<List<Integer>> clone = (List<List<Integer>>) original.clone();

        original.set(0, new ArrayList<Integer>() {{ add(12); add(23); }});
        System.out.println(clone.get(0).get(1));
    }

    @Test
    public void doIt() {
        LinkedList<Integer> integers = new LinkedList<>(Arrays.asList(2, 4, 5, 7));

        System.out.println(Collections.binarySearch(integers, 3));
        System.out.println(Collections.binarySearch(integers, 4));
        System.out.println(Collections.binarySearch(integers, 5));

        ArrayList<Integer> integers1 = new ArrayList<>();
        integers1.add(3);
        integers1.add(1, 5);
        System.out.println(integers1.get(1));
        //String sfsd = integers.stream().reduce("", (String s, int integer) -> {return "";});
    }



}