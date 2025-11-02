package graph.topo;

import graph.util.Graph;
import graph.util.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class KahnTopologicalSort {

    public static List<Integer> sort(Graph dag, Metrics metrics) {
        int n = dag.size();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (Graph.Edge e : dag.neighbors(u)) {
                indeg[e.to]++;
            }
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                q.add(i);
                if (metrics != null) metrics.inc("pushes", 1);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            if (metrics != null) metrics.inc("pops", 1);
            order.add(u);
            for (Graph.Edge e : dag.neighbors(u)) {
                indeg[e.to]--;
                if (indeg[e.to] == 0) {
                    q.add(e.to);
                    if (metrics != null) metrics.inc("pushes", 1);
                }
            }
        }
        // if order.size() < n then there was a cycle (shouldn't happen for condensation)
        return order;
    }
}
