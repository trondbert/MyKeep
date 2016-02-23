package difficult;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author trond.
 */
public class ShuffleArrayTest {

    @Test
    public void testOperationRoot() throws Exception {
        setupTestClass(asList(1, 4, 2, 8));

        ShuffleArray.performOperation(1, 2, 3);
    }

    @Test
    public void testOperationRoot2() throws Exception {
        setupTestClass(asList(1, 4, 2, 8, 3222, 10, 256));

        ShuffleArray.performOperation(2, 3, 4);

        final ListChunk tail = new Asserter()
                .head(1, 2, 5)
                .next(5, 3, 3)
                .next(3, 2, null).get();
        Assert.assertTrue(tail == ShuffleArray.listTail);
    }

    @Test
    public void testOperationRoot2b() throws Exception {
        setupTestClass(asList(1, 2, 3, 4));

        ShuffleArray.performOperation(1, 2, 3);

        final Asserter asserter = asserter();
        asserter.head(2, 2, 1);
        asserter.next(1, 1, 4);
        assertEquals(ShuffleArray.listTail, asserter.next(4, 1, null).get());
    }

    @Test
    public void testOperationRoot2c() throws Exception {
        setupTestClass(asList(1, 2, 3, 4));

        ShuffleArray.performOperation(2, 1, 3);

        final ListChunk tail = asserter()
                .head(4, 1, 1)
                .next(1, 3, null).get();
        assertEquals(ShuffleArray.listTail, tail);
    }

    @Test
    public void testOperationRoot1() throws Exception {
        setupTestClass(asList(1, 2, 3, 4, 5));

        ShuffleArray.performOperation(1, 3, 4); // 3 4 1 2 5

        final Asserter asserter = asserter();
        asserter.head(3, 2, 1);
        asserter.next(1, 2, 5);
        final ListChunk tail = asserter.next(5, 1, null).get();
        Assert.assertTrue(tail == ShuffleArray.listTail);
    }

    @Test
    public void testOperationRoot1b() throws Exception {
        setupTestClass(asList(1, 2, 3, 4, 5));

        ShuffleArray.performOperation(1, 3, 4); // 3 4, 1 2, 5
        ShuffleArray.performOperation(1, 3, 4); // 1 2, 3 4, 5

        final Asserter asserter = asserter();
        asserter.head(1, 2, 3);
        asserter.next(3, 2, 5);
        final ListChunk tail = asserter.next(5, 1, null).get();
        Assert.assertTrue(tail == ShuffleArray.listTail);
    }

    @Test
    public void testOperationRoot1c() throws Exception {
        setupTestClass(asList(1, 2, 3, 4, 5));

        ShuffleArray.performOperation(1, 3, 5); // 3 4 5, 1 2

        final Asserter asserter = asserter();
        asserter.head(3, 3, 1);
        final ListChunk tail = asserter.next(1, 2, null).get();
        Assert.assertTrue(tail == ShuffleArray.listTail);
    }

    private Asserter asserter() {
        return new Asserter();
    }

    private void setupTestClass(final List<Integer> list) {
        ShuffleArray.list = new ArrayList<>(list);
        ShuffleArray.listHead = new ListChunk(1, list.size(), null);
        ShuffleArray.listTail = ShuffleArray.listHead;
    }
}


class Asserter {

    ListChunk current;

    Asserter head(Integer position, Integer size, Integer nextPosition) {
        current = ShuffleArray.listHead;
        assertChunk(position, size, nextPosition);
        return this;
    }

    Asserter next(Integer position, Integer size, Integer nextPosition) {
        current = current.next;
        assertChunk(position, size, nextPosition);
        return this;
    }

    ListChunk get() {
        return current;
    }

    private void assertChunk(final Integer position, final Integer size, final Integer nextPosition) {
        assertEquals(position, current.head);
        assertEquals(size, current.getSize());
        if (nextPosition != null)
            assertEquals(nextPosition, current.next.head);
        else
            Assert.assertNull(current.next);
    }
}
