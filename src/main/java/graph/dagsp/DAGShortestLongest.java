package graph.dagsp;

import graph.util.Graph;
import graph.util.Metrics;
import graph.topo.KahnTopologicalSort;

import java.util.List;

public class DAGShortestLongest {

    public static PathResult shortest(Graph dag, int source, Metrics metrics) {
        if (metrics != null) metrics.start("DAG-SP-shortest");
        int n = dag.size();
        long[] dist = new long[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) { dist[i] = Long.MAX_VALUE; parent[i] = -1; }
        dist[source] = 0;

        List<Integer> topo = KahnTopologicalSort.sort(dag, metrics);
        for (int u : topo) {
            if (dist[u] == Long.MAX_VALUE) continue;
            for (Graph.Edge e : dag.neighbors(u)) {
                if (metrics != null) metrics.inc("relaxAttempts", 1);
                long nd = dist[u] + e.weight;
                if (nd < dist[e.to]) {
                    dist[e.to] = nd;
                    parent[e.to] = u;
                    if (metrics != null) metrics.inc("relaxations", 1);
                }
            }
        }
        if (metrics != null) metrics.stop("DAG-SP-shortest");
        return new PathResult(dist, parent);
    }

    public static PathResult longest(Graph dag, int source, Metrics metrics) {
        if (metrics != null) metrics.start("DAG-SP-longest");
        int n = dag.size();
        long[] dist = new long[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) { dist[i] = Long.MIN_VALUE; parent[i] = -1; }
        dist[source] = 0;

        List<Integer> topo = KahnTopologicalSort.sort(dag, metrics);
        for (int u : topo) {
            if (dist[u] == Long.MIN_VALUE) continue;
            for (Graph.Edge e : dag.neighbors(u)) {
                if (metrics != null) metrics.inc("relaxAttempts", 1);
                long nd = dist[u] + e.weight;
                if (nd > dist[e.to]) {
                    dist[e.to] = nd;
                    parent[e.to] = u;
                    if (metrics != null) metrics.inc("relaxations", 1);
                }
            }
        }

        long[] conv = new long[n];
        for (int i = 0; i < n; i++) {
            if (dist[i] == Long.MIN_VALUE) conv[i] = Long.MAX_VALUE;
            else conv[i] = dist[i];
        }
        if (metrics != null) metrics.stop("DAG-SP-longest");
        return new PathResult(conv, parent);
    }
}
