package difficult;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author trond.
 */
public class ShuffleArrayTest {

    @Test
    public void testOperationRoot() throws Exception {
        ShuffleArray.list = new ArrayList<>(Arrays.asList(1, 4, 2, 8));
        ShuffleArray.listHead = new ListChunk(1, 4, null);

        ShuffleArray.performOperation(1, 2, 3);

    }

    @Test
    public void testOperationRoot2() throws Exception {
        ShuffleArray.list = new ArrayList<>(Arrays.asList(1, 4, 2, 8, 3222, 10, 256));
        ShuffleArray.listHead = new ListChunk(1, 7, null);

        ShuffleArray.performOperation(2, 3, 4);

        final ListChunk head = ShuffleArray.listHead;
        Assert.assertEquals(Integer.valueOf(1), head.head);
        Assert.assertEquals(Integer.valueOf(2), head.size);

        ListChunk next = ShuffleArray.listHead.next;
        Assert.assertEquals(Integer.valueOf(5), next.head);
        Assert.assertEquals(Integer.valueOf(3), next.size);

        next = next.next;
        Assert.assertEquals(Integer.valueOf(3), next.head);
        Assert.assertEquals(Integer.valueOf(2), next.size);
        Assert.assertNull(next.next);

        Assert.assertTrue(head.next.next == ShuffleArray.listTail);
    }

    @Test
    public void testOperationRoot2b() throws Exception {
        ShuffleArray.list = new ArrayList<>(Arrays.asList(1, 4, 2, 8, 3222, 10, 256));
        ShuffleArray.listHead = new ListChunk(1, 7, null);

        ShuffleArray.performOperation(2, 3, 4); // 1 4 3222 10 256 2 8 (1 > 5, 5 > 3, 3 > null
        ShuffleArray.performOperation(2, 2, 6); // 1 8 4 3222 10 256 2 (1 > 4, ...)

        final ListChunk head = ShuffleArray.listHead;
        Assert.assertEquals(Integer.valueOf(1), head.head);
        Assert.assertEquals(Integer.valueOf(1), head.size);

        ListChunk next1 = ShuffleArray.listHead.next;
        Assert.assertEquals(Integer.valueOf(4), next1.head);
        Assert.assertEquals(Integer.valueOf(1), next1.size);

        final ListChunk next2 = next1.next;
        Assert.assertEquals(Integer.valueOf(2), next2.head);
        Assert.assertEquals(Integer.valueOf(1), next2.size);

        final ListChunk next3 = next2.next;
        Assert.assertEquals(Integer.valueOf(5), next3.head);
        Assert.assertEquals(Integer.valueOf(3), next3.size);

        final ListChunk next4 = next3.next;
        Assert.assertEquals(Integer.valueOf(3), next4.head);
        Assert.assertEquals(Integer.valueOf(1), next4.size);
        Assert.assertNull(next4.next);

        Assert.assertTrue(next4 == ShuffleArray.listTail);
    }
}
