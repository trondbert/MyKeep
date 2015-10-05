import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class SolutionTest {

    TreeMap<Integer, Node> nodes = new TreeMap<Integer, Node>();

    Solution solution = new Solution(null);

    private void addShortcut(int node1Number, int node2Number) {
        if (nodes.get(node1Number) == null) nodes.put(node1Number, new Node(node1Number));
        if (nodes.get(node2Number) == null) nodes.put(node2Number, new Node(node2Number));
        nodes.get(node1Number).addEdgeTo(nodes.get(node2Number), 0);
    }

    @Test
    public void test1() {
        addShortcut(7, 3);
        int moves = solution.findMoves(1, 10, nodes);
        Assert.assertEquals(2, moves);
    }

    @Test
    public void test2() {
        addShortcut(7, 3);
        addShortcut(8, 4);
        addShortcut(10, 5);
        int moves = solution.findMoves(1, 14, nodes);
        Assert.assertEquals(3, moves); // 6 - 12 - 14
    }

    @Test
    public void test3() {
        addShortcut(7, 3);
        addShortcut(8, 4);
        addShortcut(9, 5);
        addShortcut(10, 5);
        addShortcut(11, 5);
        addShortcut(12, 5);
        int moves = solution.findMoves(1, 14, nodes);
        Assert.assertEquals(Solution.infinitish, moves);
    }

    @Test
    public void test4() {
        Map<Integer, Integer> shortcuts = new TreeMap<Integer, Integer>();
        addShortcut(6, 3);
        addShortcut(7, 4);
        addShortcut(11, 5);
        addShortcut(13, 5);
        int moves = solution.findMoves(1, 24, nodes);
        Assert.assertEquals(5, moves); // 5 - 10 - 16 - 22 -24
    }

    @Test
    public void test5() {
        Map<Integer, Integer> shortcuts = new TreeMap<Integer, Integer>();
        addShortcut(2, 3);
        addShortcut(3, 4);
        addShortcut(4, 5);
        addShortcut(5, 5);
        addShortcut(6, 5);
        addShortcut(8, 3);
        addShortcut(9, 4);
        addShortcut(10, 5);
        addShortcut(11, 5);
        addShortcut(12, 5);
        int moves = solution.findMoves(1, 7, nodes);
        Assert.assertEquals(1, moves); // 7 - 13
    }

    @Test
    public void test533() {
        TreeSet<Node> unvisited = new TreeSet<>((o1, o2) ->
                o1.tentativeCost != o2.tentativeCost ?
                        o1.tentativeCost - o2.tentativeCost : o1.number - o2.number
        );

        Node node = new Node(20) {{
            tentativeCost = Solution.infinitish;
        }};
        unvisited.add(new Node(24) {{
            tentativeCost = 1;
        }});
        unvisited.add(node);
        unvisited.add(new Node(100) {{
            tentativeCost = 10;
        }});
        node.tentativeCost = 5;

        unvisited.remove(node);

        System.out.println(unvisited.size());
    }

    @Test
    public void wholeTask() {
        List<String> lines = new ArrayList<>(asList("2", "3", "32 62", "42 68", "12 98", "7", "95 13", "97 25", "93 37", "79 27", "75 19", "49 47", "67 17", "4", "8 52", "6 80", "26 42", "2 72", "9", "51 19", "39 11", "37 29", "81 3", "59 5", "79 23", "53 7", "43 33", "77 21"));
        Supplier<String> supplier = () -> {return lines.remove(0);};
        List<Integer> solutions = new Solution(supplier).solve();

        Assert.assertArrayEquals((asList(3, 5)).toArray(), solutions.toArray());
    }
}
