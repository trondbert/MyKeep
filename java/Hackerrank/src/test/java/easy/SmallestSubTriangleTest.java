package easy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by trond on 16/08/16.
 */
public class SmallestSubTriangleTest {

    @Test
    public void testIt() {
        ArrayList<List<Integer>> task = new ArrayList<>(3);
        task.add(Arrays.asList(1));
        task.add(Arrays.asList(-4, 6));
        task.add(Arrays.asList(-3, 6, 9));

        new SmallestSubTriangle().solve(task, 3);
    }

    @Test
    public void adhoc() {
        int i = 0;
        ArrayList<List<Integer>> original = new ArrayList<List<Integer>>() {{
            add(new ArrayList<Integer>() {{
                add(1);
                add(2);
            }});
        }};
        List<List<Integer>> clone = (List<List<Integer>>) original.clone();

        original.set(0, new ArrayList<Integer>() {{ add(12); add(23); }});
        System.out.println(clone.get(0).get(1));
    }
}
