package difficult;

import static java.lang.Math.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * https://www.hackerrank.com/challenges/array-and-simple-queries
 */
public class ShuffleArray {

    static ListChunk listHead;

    static ListChunk listTail = listHead;

    static List<Integer> list;

    public static void main(final String[] args) {

        final long start = System.currentTimeMillis();
        solve1();
        final long duration = System.currentTimeMillis() - start;
        System.out.println("Duration (s): " + duration / 1000.0);
    }

    @SuppressWarnings("unused")
    private static void writeTestCase() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/tmp/inputShuffleArray.txt"))) {
            final int n = 100000;
            final int m = 100000;
            bw.write(n + " " + m + "\n");
            final int maxValue = 1000000000;
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

    private static void solve1() {
        list = new LinkedList<>();
        final Scanner scanner = new Scanner(System.in);
        final int elementCount = scanner.nextInt();
        final int operationCount = scanner.nextInt();

        for (int i = 0; i < elementCount; i++) {
            list.add(scanner.nextInt());
        }
        listHead = new ListChunk(list.get(0), list.size(), null);

        for (int i = 0; i < operationCount; i++) {
            final Integer type = scanner.nextInt();
            final Integer start = scanner.nextInt();
            final Integer end = scanner.nextInt();
            System.out.println("op " + i);
            performOperation(type, start, end);
        }
        System.out.println(abs(list.get(0) - list.get(list.size() - 1)));
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < elementCount; i++) {
            sb.append(list.get(i)).append(" ");
        }
        System.out.println(sb.toString().trim());
    }

    static void performOperation(final Integer type, final int start, final Integer end) {
        ListChunk previous = listHead;      // The chunk that will link to a new head. Contains head
        ListChunk next;                     // The chunk containing the end element
        int startOffset = start;            // 1-indexed
        int endOffset = end;                // 1-indexed

        //5 4 3: 6
        int sumOfChunkSizes = previous.size;
        while (start > sumOfChunkSizes + 1) {
            sumOfChunkSizes += previous.size;
            previous = previous.next;
        }
        startOffset = start - (sumOfChunkSizes - previous.size);

        next = previous;
        while (end > sumOfChunkSizes) {
            sumOfChunkSizes += next.size;
            next = next.next;
        }
        endOffset = end - (sumOfChunkSizes - next.size);

        if (type == 2) {
            final ListChunk gapFiller = (endOffset == next.size)?
                                        next.next :
                                        new ListChunk(next.head + endOffset, next.size - endOffset, next.next);
            final ListChunk startChunk = new ListChunk(previous.head + startOffset - 1,
                                                       min(end - start + 1, previous.size - startOffset + 1), previous.next);
            previous.size = startOffset - 1;
            previous.next = gapFiller;

            (gapFiller.next == null? gapFiller : listTail).next = startChunk;

            if (next != previous) {
                next.size = next.size - endOffset;
                next.next = null;
                listTail = next;
            }
            else {
                listTail = startChunk;
            }
        }
    }
    //move to end:
    //previous.next = beyond end (in next)
    //end chunk.next = start
    //start.end = null
    //move to start:
    //previous.next = beyond end (in next)
    //start.next = listHead
    // listHead = start

}


class ListChunk {

    final Integer head;

    Integer size;

    ListChunk next;

    public ListChunk(final Integer head, final int size, final ListChunk next) {
        this.head = head;
        this.size = size;
        this.next = next;
    }

    @Override
    public String toString() {
        return head + " s: " + size + " n: " + (next != null? next.head : null);
    }
}
