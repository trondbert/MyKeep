package hackerrank.difficult;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * https://www.hackerrank.com/challenges/array-and-simple-queries
 */
public class ShuffleArray {

    static ListChunk listHead;

    static ListChunk listTail = listHead;

    static List<Integer> list = new LinkedList<Integer>();

    public static void main(final String[] args) {
        if (args.length > 0) {
            try {
                System.setIn(new FileInputStream(args[0]));
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        final long start = System.currentTimeMillis();
        solve1();
        final long duration = System.currentTimeMillis() - start;
        //System.out.println("Duration (s): " + duration / 1000.0);
    }

    private static void solve1() {
        final Scanner scanner = new Scanner(System.in);
        final int elementCount = scanner.nextInt();
        final int operationCount = scanner.nextInt();

        for (int i = 0; i < elementCount; i++) {
            list.add(scanner.nextInt());
        }
        listHead = new ListChunk(1, list.size(), null);
        listTail = listHead;

        for (int i = 0; i < operationCount; i++) {
            final Integer type = scanner.nextInt();
            final Integer start = scanner.nextInt();
            final Integer end = scanner.nextInt();
            //System.out.println("op " + i);
            performOperation(type, start, end);
            //debugCheckChunks();
        }

        System.out.println(abs(list.get(listHead.head - 1) -
                               list.get((listTail.head + listTail.getSize() - 1) - 1)));
        printShuffledList();
    }

    private static void debugCheckChunks() {
        int sumChunkSize = 0;
        ListChunk current = listHead;
        final TreeSet<Integer> heads = new TreeSet<>();
        do {
            sumChunkSize += current.getSize();
            if (heads.contains(current.head)) {
                System.out.println("Duplicate head: " + current.head);
                System.exit(0);
            }
            heads.add(current.head);
        } while ((current = current.next) != null);
        if (sumChunkSize != list.size()) {
            System.out.println("Chunks have total size of " + sumChunkSize + ". List size is " + list.size());
        }
    }

    static void performOperation(final Integer type, final int start, final Integer end) {
        ListChunk firstInRange = listHead;   // Contains start, or start is the head of the next chunk
        ListChunk lastInRange;               // Contains end, or end is the head of the next chunk
        final int startIndex;                      // 1-indexed
        final int endIndex;                        // 1-indexed

        int sumOfChunkSizes = 0;
        do {
            sumOfChunkSizes += firstInRange.getSize();
        } while (sumOfChunkSizes < start - 1 && (firstInRange = firstInRange.next) != null);
        assert firstInRange != null;
        startIndex = start - (sumOfChunkSizes - firstInRange.getSize());

        sumOfChunkSizes -= firstInRange.getSize();
        lastInRange = firstInRange;
        do {
            sumOfChunkSizes += lastInRange.getSize();
        } while (sumOfChunkSizes < end - 1 && (lastInRange = lastInRange.next) != null);
        assert lastInRange != null;
        endIndex = end - (sumOfChunkSizes - lastInRange.getSize());

        if (type == 1) {
            if (start == 1)
                return;
            shuffleType1(start, end, firstInRange, lastInRange, startIndex, endIndex);
        }
        else if (type == 2) {
            if (end == list.size())
                return;
            shuffleType2(start, end, firstInRange, lastInRange, startIndex, endIndex);
        }
    }

    private static void shuffleType1(final int start, final Integer end, final ListChunk firstInRange,
                                     final ListChunk lastInRange, final int startIndex, final int endIndex) {
        final ListChunk postRange = createPostRange(lastInRange, endIndex);
        final ListChunk startRange = createStartRange(start, end, firstInRange, startIndex);
        if (end - start + 1 <= startRange.getSize()) {
            startRange.next = listHead;
        }
        else {
            final ListChunk endRange = (endIndex > lastInRange.getSize())? lastInRange.next : lastInRange;
            endRange.setSize((endIndex > lastInRange.getSize())? 1 : endIndex);
            endRange.next = listHead;
        }

        listHead = startRange;

        firstInRange.setSize(startIndex - 1);
        firstInRange.next = postRange;
        if (postRange == null)
            listTail = firstInRange;
        else if (postRange.next == null)
            listTail = postRange;

        listTail.next = null;
    }

    private static void shuffleType2(final int start, final Integer end, final ListChunk firstInRange,
                                     final ListChunk lastInRange, final int startIndex, final int endIndex) {
        final ListChunk postRange = createPostRange(lastInRange, endIndex);
        final ListChunk startRange = createStartRange(start, end, firstInRange, startIndex);
        assert postRange != null;
        if (postRange.next == null)
            postRange.next = startRange;
        else
            listTail.next = startRange;

        if (end - start + 1 <= startRange.getSize()) {
            listTail = startRange;
        }
        else {
            final ListChunk endRange = (endIndex > lastInRange.getSize())? lastInRange.next : lastInRange;
            endRange.setSize((endIndex > lastInRange.getSize())? 1 : endIndex);
            endRange.next = null;
            listTail = endRange;
        }
        if (firstInRange == listHead && start == 1) {
            listHead = postRange;
        }
        else {
            firstInRange.setSize(startIndex - 1);
            firstInRange.next = postRange;
        }
        listTail.next = null;
    }

    private static ListChunk createStartRange(final int start, final Integer end, final ListChunk firstInRange,
                                              final int startIndex) {
        return (startIndex > firstInRange.getSize())?
               new ListChunk(firstInRange.next.head,
                             min(end - start + 1, firstInRange.next.getSize()),
                             firstInRange.next.next) :
               new ListChunk(firstInRange.head + startIndex - 1,
                             min(end - start + 1, firstInRange.getSize() - startIndex + 1),
                             firstInRange.next);
    }

    private static ListChunk createPostRange(final ListChunk lastInRange, final int endIndex) {
        if (endIndex > lastInRange.getSize()) {
            if (lastInRange.next.getSize() == 1)
                return new ListChunk(lastInRange.next.next.head, lastInRange.next.next.getSize(), lastInRange.next.next.next);
            return new ListChunk(lastInRange.next.head + 1, lastInRange.next.getSize() - 1, lastInRange.next.next);
        }
        if (endIndex == lastInRange.getSize()) {
            if (lastInRange.next == null)
                return null;
            return new ListChunk(lastInRange.next.head, lastInRange.next.getSize(), lastInRange.next.next);
        }
        return new ListChunk(lastInRange.head + endIndex, lastInRange.getSize() - endIndex, lastInRange.next);
    }

    private static void printShuffledList() {
        ListChunk current = listHead;
        final StringBuilder sb = new StringBuilder();
        do {
            for (int i = 0; i < current.getSize(); i++) {
                sb.append(list.get(current.head + i - 1)).append(" ");
            }
        } while ((current = current.next) != null);
        System.out.println(sb.toString().trim());
    }
}


class ListChunk {

    final Integer head;

    private Integer size;

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

    public Integer getSize() {
        return size;
    }
    public void setSize(final Integer size) {
        this.size = size;
    }
}
