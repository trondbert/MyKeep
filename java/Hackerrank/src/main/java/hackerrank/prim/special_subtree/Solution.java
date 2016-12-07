package hackerrank.prim.special_subtree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.Integer.parseInt;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.System.out;
import static java.util.Arrays.asList;

public class Solution {
    BufferedReader br; InputStreamReader isr;
    Map<Edge, Integer> weightsByEdge = new HashMap<>();
    Map<Integer, List<Edge>> edgesByNode = new HashMap<>();
    boolean[] inSubgraph;

    private String readLine() { try { return br.readLine(); }
        catch (IOException e) { throw new RuntimeException(e); }}

    public Solution() {
        isr = new InputStreamReader(System.in); br = new BufferedReader(isr);
    }
    public static void main(String[] args) {
        new Solution().solve();
    }
    void solve() {
        String[] nodesAndEdges = readLine().split(" ");
        int nodeCount = parseInt(nodesAndEdges[0]), edgeCount = parseInt(nodesAndEdges[1]);

        createGraph(edgeCount);
        int startNode = parseInt(readLine());
        Set<Integer> subgraphNodes = new TreeSet<>(asList(startNode));
        Set<Edge> subgraphEdges = new TreeSet<>();
        Set<Edge> bridgesToRestGraph = new HashSet<>();
        bridgesToRestGraph.addAll(edgesByNode.get(startNode));

        while (subgraphNodes.size() < nodeCount) {
            Edge cheapestEdge = bridgesToRestGraph.stream()
                    .filter((edge) -> !subgraphNodes.contains(edge.from) || !subgraphNodes.contains(edge.to))
                    .min((a, b) -> weightsByEdge.get(a) - weightsByEdge.get(b))
                    .get();
            int newNode = !subgraphNodes.contains(cheapestEdge.from) ? cheapestEdge.from : cheapestEdge.to;
            subgraphNodes.add(newNode);
            subgraphEdges.add(cheapestEdge);
            bridgesToRestGraph.addAll(edgesByNode.get(newNode));
            bridgesToRestGraph.remove(cheapestEdge);
        }
        out.println(subgraphEdges.stream().mapToInt(weightsByEdge::get).sum());
    }
    private void createGraph(int edgeCount) {
        for (int i = 0; i < edgeCount; i++) {
            String[] edgeString = (readLine().split(" "));
            int from = parseInt(edgeString[0]), to = parseInt(edgeString[1]), weight = parseInt(edgeString[2]);

            edgesByNode.putIfAbsent(from, new ArrayList<>());
            edgesByNode.get(from).add(new Edge(from, to));
            edgesByNode.putIfAbsent(to, new ArrayList<>());
            edgesByNode.get(to).add(new Edge(from, to));
            weightsByEdge.put(new Edge(from, to), weight);
        }
    }
    class Edge implements Comparable { int from; int to;
        Edge(int from, int to) { this.from = from; this.to = to; }

        @Override
        public boolean equals(Object obj) {
            return min(from, to) == min(((Edge) obj).from, ((Edge) obj).to) &&
                    max(from, to) == max(((Edge) obj).from, ((Edge) obj).to); }
        @Override
        public int hashCode() {
            return from * 3000 + to; // Max value for from and to = 3000
        }

        @Override
        public int compareTo(Object o) {
            int fromDiff = from - ((Edge)o).from;
            if (fromDiff != 0) {
                return fromDiff;
            }
            return to - ((Edge)o).to;
        }
    }
}
