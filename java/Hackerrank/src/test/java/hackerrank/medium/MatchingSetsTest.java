package hackerrank.medium;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MatchingSetsTest {

    @Test
    public void shouldComputeSign() {
        Assert.assertEquals(1, MatchingSets.sign(3));
        Assert.assertEquals(0, MatchingSets.sign(0));
        Assert.assertEquals(-1, MatchingSets.sign(-4));
    }

    @Test
    public void shouldFindFirstPartner() {
        SortedInputArrays input = new SortedInputArrays();
        input.x = new int[] {1,  6, 7, 8};
        input.y = new int[] {-2, 6, 6, 12};

        Assert.assertEquals(3, MatchingSets.findFirstPartner(input, 0));
    }

    @Test
    public void testAll() throws IOException {
        System.setIn(new ByteArrayInputStream("3\n1 2 3\n-1 4 3".getBytes()));
        Assert.assertEquals(2, MatchingSets.solve());
    }

    @Test
    public void testReadTask() throws IOException {
        System.setIn(new ByteArrayInputStream("3\n1 2 3\n-1 4 3".getBytes()));
        SortedInputArrays inputArrays = MatchingSets.readTask();

        Assert.assertArrayEquals(new int[] { 1, 2},  inputArrays.x);
        Assert.assertArrayEquals(new int[] {-1, 4}, inputArrays.y);
    }

    @Test
    public void testSolve() throws IOException {
        SortedInputArrays input = new SortedInputArrays();
        input.x = new int[]{1, 2, 3};
        input.y = new int[]{-1, 3, 4};
        int solve = MatchingSets.solve(input);

        Assert.assertEquals(2, solve);
        Assert.assertArrayEquals(new int[]{-1, 3, 4}, input.x);

        input.x = new int[]{0, 2, 3, 5};
        input.y = new int[]{1, 1, 4, 5};
        solve = MatchingSets.solve(input);

        Assert.assertEquals(-1, solve);
    }

    @Test
    public void testModify() {
        SortedInputArrays input = new SortedInputArrays();
        input.x = new int[] { 1, 2};
        input.y = new int[] {-1, 4};

        int moves = MatchingSets.modifyX(input, 0);
        Assert.assertEquals(2, moves);
        Assert.assertArrayEquals(new int[] {-1, 4}, input.x);

        input.x = new int[] { 1, 4, 10, 12 };
        input.y = new int[] { 3, 6, 7, 12 };
        moves = MatchingSets.modifyX(input, 0);
        Assert.assertEquals(2, moves);
        Assert.assertArrayEquals(new int[] {3, 4, 8, 12}, input.x);
    }


}
