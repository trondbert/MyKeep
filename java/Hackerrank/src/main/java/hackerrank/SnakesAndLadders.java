package hackerrank;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.util.stream.IntStream.rangeClosed;

public class SnakesAndLadders {
    public static final int infinitish = 10000000;
    private final Supplier<String> linesSupplier;

    private static List<String> mockedTask = new ArrayList<>(Arrays.<String>asList(
            "2","3","32 62","42 68","12 98","7","95 13","97 25","93 37","79 27","75 19","49 47","67 17","4","8 52","6 80","26 42","2 72","9","51 19","39 11","37 29","81 3","59 5","79 23","53 7","43 33","77 21"));
    static Supplier<String> linesSupplier2 = () -> mockedTask.size() > 0 ? mockedTask.remove(0) : null;

    public SnakesAndLadders(Supplier<String> linesSupplier) {
        this.linesSupplier = linesSupplier;
    }

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Supplier<String> supplierFromStdin = () -> {
            try {
                return reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        new SnakesAndLadders(supplierFromStdin).solve().forEach(System.out::println);
    }

    public List<Integer> solve() {
        List<Integer> solutions = new ArrayList<>();

        int numberOfCases = parseInt(nextLine());
        for (int caseIndex = 0; caseIndex < numberOfCases; caseIndex++) {
            TreeMap<Integer, Node> nodes = new TreeMap<Integer, Node>();
            nodes.put(1, new Node(1)); nodes.put(100, new Node(100));

            int numberOfLadders = parseInt(nextLine());
            rangeClosed(1, numberOfLadders).forEach(n -> {
                registerShortcutNodes(nextLine(), nodes);
            });

            int numberOfSnakes = parseInt(nextLine());
            rangeClosed(1, numberOfSnakes).forEach(n -> {
                registerShortcutNodes(nextLine(), nodes);
            });

            buildGraph(nodes);
            printGraph(nodes);
            Node solved = new Dijkstra(this).solve(nodes);
            int solution = solved.tentativeCost == infinitish ? -1 : solved.tentativeCost;
            solutions.add(solution);
        }
        return solutions;
    }

    void registerShortcutNodes(String nodePair, Map<Integer, Node> nodes) {
        int start   = parseInt(nodePair.split(" ")[0]);
        int end     = parseInt(nodePair.split(" ")[1]);
        if (nodes.get(start) == null)   nodes.put(start, new Node(start));
        if (nodes.get(end) == null)     nodes.put(end, new Node(end));

        nodes.get(start).addEdgeTo(nodes.get(end), 0);
    }

    void printGraph(Map<Integer, Node> nodes) {
        if (! new File("/Users/trond/tmp/dot-files/test.dot").exists()) {
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/trond/tmp/dot-files/test.dot"))) {
            writeLine(bw, "digraph G {");
            writeLine(bw, "node [shape=record]");
            Function<Integer, String> prettyCost = cost -> (cost == infinitish) ? "INF" : "" + cost;
            for (Node node : nodes.values()) {
                writeLine(bw, String.format("struct%d [label=\"<f0>%d | <f1> %s\"]",
                        node.number, node.number, prettyCost.apply(node.tentativeCost)));
                node.outgoingEdges.stream().filter(e -> e.cost != 0).forEach((edge) -> {
                    writeLine(bw, String.format("struct%d:f0 -> struct%d:f0 [label=\"%s\"]",
                            node.number, edge.target.number, prettyCost.apply(edge.cost)));
                });
            }
            writeLine(bw, "}");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void writeLine(BufferedWriter bw, String output) {
        try { bw.write(output + "\n"); }
        catch (IOException e) { e.printStackTrace();
        }
    }

    private void buildGraph(SortedMap<Integer, Node> nodes) {
        Node currentNode = nodes.get(nodes.firstKey());
        SortedMap<Integer, Node> tailMap = nodes.tailMap(currentNode.number + 1);

        if (!currentNode.isSnakeMouth() && !currentNode.isLadderBottom()) {
            for (Map.Entry<Integer, Node> otherNodes : tailMap.entrySet()) {
                Node otherNode = nodes.get(otherNodes.getKey());
                if (otherNode.isSnakeTail()) continue;

                int moves = findMoves(currentNode.number, otherNode.number, nodes);
                currentNode.addEdgeTo(otherNode.shortcutEndOrThis(), moves);
            }
        }
        if (! tailMap.isEmpty()) {
            buildGraph(tailMap);
        }
    }

    int findMoves(final int from, final int to, final Map<Integer, Node> nodes) {
        int reserveMoves = 0, currentGap = 0, moves = 0, currentCell = from;
        for (Node node : nodes.values()) {
            if (node.number > to) break;
            if (!node.isSnakeMouth() && !node.isLadderBottom()) continue;

            if (currentGap > 6) { return infinitish; }
            int distance = node.number - 1 - currentCell;
            int paidDistance = distance;

            if (distance < currentGap) {
                currentGap = max(2, currentGap + 1);
                continue;
            }
            if (reserveMoves >= currentGap) {
                paidDistance    -= Math.min(reserveMoves, distance);
                reserveMoves    -= Math.min(reserveMoves, distance);
            } else {
                reserveMoves = 0;
            }
            currentCell += distance;
            moves += (int) ceil(paidDistance / 6.0);
            currentGap = 2;
            if (paidDistance % 6 != 0) { reserveMoves = 6 - (paidDistance % 6); }
        }
        moves += ceil(max(0, to - currentCell - reserveMoves) / 6.0);
        return moves;
    }

    String nextLine() {
        return linesSupplier.get();
    }
}

class Node {
    final int number;
    List<Edge> outgoingEdges = new ArrayList<>();
    List<Edge> incomingEdges = new ArrayList<>();
    public int tentativeCost = SnakesAndLadders.infinitish;

    Node(int number) { this.number = number; }

    public void addEdgeTo(Node other, int cost) {
        Stream<Edge> betterEdges = outgoingEdges.stream().filter((edge) ->
                edge.target == other && edge.cost < cost);
        if (betterEdges.count() == 0) {
            outgoingEdges.add(new Edge(other, cost));
            other.incomingEdges.add(new Edge(this, cost));
        }
    }

    public boolean isSnakeMouth() {
        long snakes = outgoingEdges.stream().filter((edge) -> {
            return edge.target.number < this.number && edge.cost == 0;
        }).count();
        return snakes > 0;
    }

    public boolean isSnakeTail() {
        long snakes = incomingEdges.stream().filter((edge) -> {
            return edge.target.number > this.number && edge.cost == 0;
        }).count();
        return snakes > 0;
    }

    public Node shortcutEndOrThis() {
        Edge shortcut = outgoingEdges.stream().filter((edge) -> {
            return edge.cost == 0;
        }).findFirst().orElseGet(() -> {
            return null;
        });
        return shortcut != null ? shortcut.target : this;
    }

    public boolean isLadderBottom() {
        long ladders = outgoingEdges.stream().filter((edge) -> {
            return edge.target.number > this.number && edge.cost == 0;
        }).count();
        return ladders > 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this.number == ((Node)obj).number;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public String toString() {
        return "hackerrank.Node " + number;
    }
}

class Edge {
    Node target;
    int cost;

    Edge(Node target, int cost) {
        this.target = target;
        this.cost = cost;
    }
}

class Dijkstra {

    private SnakesAndLadders caller;

    Dijkstra(SnakesAndLadders caller) {
        this.caller = caller;
    }

    public Node solve(Map<Integer, Node> nodes) {
        TreeSet<Node> unvisited = new TreeSet<>((o1, o2) ->
                o1.tentativeCost != o2.tentativeCost ?
                        o1.tentativeCost - o2.tentativeCost : o1.number - o2.number
        );
        unvisited.addAll(nodes.values());
        nodes.get(1).tentativeCost = 0;

        while (!unvisited.isEmpty()) {
            Node first = unvisited.first();
            visit(first, unvisited);
            caller.printGraph(nodes);
            if (first.number == 100) {
                return first;
            }
        }
        return nodes.get(100);
    }

    private void visit(Node currentNode, TreeSet<Node> unvisited) {
        for (Edge outgoingEdge : currentNode.outgoingEdges) {
            if (outgoingEdge.cost == 0) continue;

            Node neighborNode = outgoingEdge.target;
            if (unvisited.contains(neighborNode)) {
                unvisited.remove(neighborNode);
                neighborNode.tentativeCost = Math.min(
                    currentNode.tentativeCost + outgoingEdge.cost, neighborNode.tentativeCost);
                unvisited.add(neighborNode);
            }
        }
        unvisited.remove(currentNode);
    }
}
