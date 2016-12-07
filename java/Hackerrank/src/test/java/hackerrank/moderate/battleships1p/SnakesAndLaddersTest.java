package hackerrank.moderate.battleships1p;

import java.util.Random;

import org.junit.Test;

/**
 * @author trond.
 */
public class SnakesAndLaddersTest {

    @Test
    public void testOne() throws Exception {
        final String exampleField = "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "-h--------\n" +
                                    "-h--------\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert (nextMove.equals("7 1")) : nextMove;
    }

    @Test
    public void testTwo() throws Exception {
        final String exampleField = "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "--------hh\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert (nextMove.equals("6 7")) : nextMove;
    }

    @Test
    public void testThreeMiss() throws Exception {
        final String exampleField = "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "------m---\n" +
                                    "------h---\n" +
                                    "------h---\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert (nextMove.equals("6 6")) : nextMove;
    }

    @Test
    public void testFindALargeOpenSpan() throws Exception {
        final String exampleField = "-----m----\n" +
                                    "--ddddd---\n" +
                                    "----------\n" +
                                    "---d--d---\n" +
                                    "---d--d---\n" +
                                    "---d------\n" +
                                    "---d------\n" +
                                    "---ddd----\n" +
                                    "----------\n" +
                                    "----dd----\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert nextMove.equals("2 5") : nextMove;
    }

    @Test
    public void testFindALargeOpenSpan2() throws Exception {
        final String exampleField = "----------\n" +
                                    "m---------\n" +
                                    "m---------\n" +
                                    "m---------\n" +
                                    "m---------\n" +
                                    "mmmmmmmmmm\n" +
                                    "----m-----\n" +
                                    "----m-----\n" +
                                    "----m-----\n" +
                                    "----m-----\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert nextMove.equals("0 5") : nextMove;
    }

    @Test
    public void testMissesPlaced() throws Exception {
        final String exampleField = "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "----m-----\n" +
                                    "---m------\n" +
                                    "--m-------\n" +
                                    "-------m--\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert (nextMove.equals("5 0")) : nextMove;
    }

    @Test
    public void testHitsPlaced() throws Exception {
        final String exampleField = "----------\n" +
                                    "----------\n" +
                                    "m---------\n" +
                                    "h---------\n" +
                                    "hm--------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n" +
                                    "----------\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert (nextMove.equals("5 0")) : nextMove;
    }

    @Test
    public void testCorrectDivideSpanInTwo() throws Exception {
        final String exampleField = "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "mmmmmmmmmm\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n" +
                                    "-----m----\n";

        final String nextMove = new Solution(exampleField).nextMove();
        assert (nextMove.equals("0 2")) : nextMove;
    }

    @Test
    public void testName() throws Exception {
        String signs = "mmmdddddddddddddhh---------";
        for (int run = 0; run < 50; run++) {

            final String[] lines = new String[10];
            for (int i = 0; i < 10; i++) {
                lines[i] = "";
                for (int j = 0; j < 10; j++) {
                    final int charIndex = new Random().nextInt(signs.length());
                    lines[i] += signs.substring(charIndex, charIndex + 1);
                }
            }
            String field = "";
            for (int i = 0; i < 10; i++) {
                field += lines[i] + "\n";
            }
            field = field.substring(0, 110);
            System.out.println(new Solution(field).nextMove());
        }
    }
}
