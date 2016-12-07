package hackerrank.moderate.binarysearchtree;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author trond.
 */
public class AutoBalancerTest {

    @Test
    public void testBalancing() throws Exception {
        //    5
        //   /
        //  3
        // insert 4

        final Node n3 = AutoBalancer.createNode(3, 0, null, null);
        final Node n5 = AutoBalancer.createNode(5, 1, n3, null);
        Node root = AutoBalancer.insert(n5, 4);

        assertEquals(4, root.val);
        assertEquals(3, root.left.val);
        assertEquals(5, root.right.val);
    }

    @Test
    public void testBalancing2() {
        //    2
        //      \
        //       4
        // insert 3

        final Node n4 = AutoBalancer.createNode(4, 0, null, null);
        final Node n2 = AutoBalancer.createNode(2, 1, null, n4);
        Node root = AutoBalancer.insert(n2, 3);

        assertEquals(3, root.val);
        assertEquals(2, root.left.val);
        assertEquals(4, root.right.val);

    }
}
