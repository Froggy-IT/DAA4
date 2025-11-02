package graph.topo;

import graph.scc.SCCResult;
import graph.util.Graph;

import java.util.HashSet;
import java.util.Set;

public class CondensationGraph {

    public static Graph build(SCCResult scc, Graph orig) {
        int k = scc.numComponents();
        Graph dag = new Graph(k);
        int n = orig.size();
        Set<Long> seen = new HashSet<>(); // encoded pair (a<<32)|b to avoid duplicates
        for (int u = 0; u < n; u++) {
            int cu = scc.compOf[u];
            for (Graph.Edge e : orig.neighbors(u)) {
                int v = e.to;
                int cv = scc.compOf[v];
                if (cu != cv) {
                    long key = (((long) cu) << 32) | (cv & 0xffffffffL);
                    if (!seen.contains(key)) {
                        dag.addEdge(cu, cv, e.weight);
                        seen.add(key);
                    } else {
                        // Optionally update weight if you want min weight, but keep first for simplicity.
                    }
                }
            }
        }
        return dag;
    }
}
