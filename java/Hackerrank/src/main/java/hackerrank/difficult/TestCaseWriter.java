package hackerrank.difficult;

import static java.lang.Math.random;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author trond.
 */
public class TestCaseWriter {

    public static void main(final String[] args) {
        writeTestCase();
    }

    @SuppressWarnings("unused")
    private static void writeTestCase() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/tmp/inputShuffleArray2.txt"))) {
            final int n = 100;
            final int m = 10;
            bw.write(n + " " + m + "\n");
            final int maxValue = 100;
            for (int i = 0; i < n; i++) {
                bw.write((int) (random() * maxValue) + " ");
            }
            bw.write("\n");
            for (int i = 0; i < m; i++) {
                final int type = random() > 0.5? 2 : 1;
                final int start = (int) (random() * n);
                final int end = (int) (start + random() * (n - start));
                bw.write(type + " " + start + " " + end + "\n");
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
