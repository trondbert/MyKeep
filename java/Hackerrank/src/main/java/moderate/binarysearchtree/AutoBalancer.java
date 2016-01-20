package moderate.binarysearchtree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

class Node {

    int val;

    int ht;

    Node left;

    Node right;

    @Override
    public String toString() {
        return "" + val;
    }
}


/**
 * @author trond.
 */
@SuppressWarnings("SuspiciousNameCombination") public class AutoBalancer {

       /* Class node is defined as :
    class Node
       int val;   //Value
       int ht;      //Height
       Node left;   //Left child
       Node right;   //Right child

   */

    public static void main(final String[] args) {
        final Node left2 = createNode(2, 0, null, null);
        final Node right5 = createNode(5, 0, null, null);
        final Node right4 = createNode(4, 1, null, right5);
        createNode(3, 2, left2, right4);
        //insert(root, 6);

        final Node n3 = createNode(3, 0, null, null);
        final Node n5 = createNode(5, 1, n3, null);
        root = insert(n5, 4);
        printTree(root);
    }

    static boolean DEBUG = true;

    static Node root;

    static int graphVersion = 0;

    static Node insert(final Node node, final int val) {
        root = node;
        if (DEBUG)
            System.out.println(String.format("insert value %d into node %d", val, node.val));
        if (val >= node.val) {
            if (node.right != null)
                node.right = insert(node.right, val);
            else {
                node.right = createNode(val, 0, null, null);
            }
        }
        else {
            if (node.left != null)
                node.left = insert(node.left, val);
            else {
                node.left = createNode(val, 0, null, null);
            }
        }
        reCalculateHeight(node);
        printTree(root);
        if (DEBUG)
            System.out.println(node.val + " " + height(node.left) + " " + height(node.right));

        Node newTop = node;
        while (Math.abs(height(newTop.left) - height(newTop.right)) > 1) {
            if (DEBUG)
                System.out.println("Unbalanced node: " + newTop.val);
            newTop = balance(newTop);
        }

        return newTop;
    }

    // Returns new top.
    private static Node balance(final Node node) {
        if (height(node.right) - height(node.left) > 1 && height(node.right.right) > height(node.right.left)) {
            final Node nodeRightLeft = node.right.left;
            final Node top = node.right;

            top.left = node;
            node.right = nodeRightLeft;

            reCalculateHeight(node.right);
            reCalculateHeight(node);
            reCalculateHeight(top);
            return top;
        }
        else if (height(node.left) - height(node.right) > 1 && height(node.left.left) > height(node.left.right)) {
            final Node nodeLeftRight = node.left.right;
            final Node top = node.left;

            top.right = node;
            node.left = nodeLeftRight;

            reCalculateHeight(node.left);
            reCalculateHeight(node);
            reCalculateHeight(top);
            return top;
        }
        else if (height(node.right) - height(node.left) > 1 && height(node.right.left) > height(node.right.right)) {
            final Node newMiddle = node.right.left;
            final Node newMiddleRight = newMiddle.right;
            newMiddle.right = node.right;
            newMiddle.right.left = newMiddleRight;

            node.right = newMiddle;
            reCalculateHeight(newMiddle.right);
            reCalculateHeight(newMiddle);
            reCalculateHeight(node);
            return node;
        }
        else if (height(node.left) - height(node.right) > 1 && height(node.left.right) > height(node.left.left)) {
            final Node newMiddle = node.left.right;
            final Node newMiddleLeft = newMiddle.left;
            newMiddle.left = node.left;
            newMiddle.left.right = newMiddleLeft;

            node.left = newMiddle;
            reCalculateHeight(newMiddle.left);
            reCalculateHeight(newMiddle);
            reCalculateHeight(node);
            return node;
        }
        return node;
    }

    private static int height(final Node node) {
        return node != null? node.ht : -1;
    }

    private static void reCalculateHeight(final Node node) {
        if (node == null)
            return;
        node.ht = 1 + Math.max(height(node.left), height(node.right));
    }

    @SuppressWarnings("unused")
    static void print(final Node node) {
        System.out.print(node.val + ":");
        System.out.print(" ht -> " + node.ht);
        System.out.print(", left -> " + (node.left != null? node.left.val : null));
        System.out.print(", right -> " + (node.right != null? node.right.val : null));
        System.out.println("\n-----");

        if (node.left != null)
            print(node.left);

        if (node.right != null)
            print(node.right);
    }

    static Node createNode(final int val, final int ht, final Node left, final Node right) {
        final Node nee = new Node();
        nee.ht = ht;
        nee.right = right;
        nee.left = left;
        nee.val = val;
        return nee;
    }

    private static void printTree(final Node root) {
        if (!DEBUG)
            return;
        graphVersion++;
        String versionString = "00000" + graphVersion;
        versionString = versionString.substring(versionString.length() - 4);
        final String fileName = "/Users/trond/trondtmp/dot-files/balance-tree_" + versionString + ".dot";
        System.out.println(fileName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("digraph G {\n");
            printGraphNode(root, bw);
            bw.write("}\n");
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void printGraphNode(final Node node, final BufferedWriter bw) throws IOException {
        final String format = "\"%d\" -> %d [label=\"%s\"]\n";
        if (node.left != null) {
            bw.write(String.format(format, node.val, node.left.val, "left"));
            printGraphNode(node.left, bw);
        }
        if (node.right != null) {
            bw.write(String.format(format, node.val, node.right.val, "right"));
            printGraphNode(node.right, bw);
        }
        bw.write(String.format("%d [label=\"%d h:%d\"]\n", node.val, node.val, node.ht));
    }

}
