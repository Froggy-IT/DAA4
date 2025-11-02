package graph.util;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final List<List<Edge>> adj;

    public static class Edge {
        public final int to;
        public final long weight;
        public Edge(int to, long weight) { this.to = to; this.weight = weight; }
        @Override public String toString() { return String.format("(%d,w=%d)", to, weight); }
    }

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public int size() { return n; }

    public List<Edge> neighbors(int u) { return adj.get(u); }

    public void addEdge(int u, int v, long w) {
        adj.get(u).add(new Edge(v, w));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(i).append(": ").append(adj.get(i)).append("\n");
        }
        return sb.toString();
    }
}
