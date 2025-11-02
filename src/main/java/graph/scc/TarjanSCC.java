package graph.scc;

import graph.util.Graph;
import graph.util.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TarjanSCC {
    private final Graph g;
    private final Metrics metrics;

    private int[] index;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private int currentIndex;
    private final List<List<Integer>> components;

    private TarjanSCC(Graph g, Metrics metrics) {
        this.g = g;
        this.metrics = metrics;
        int n = g.size();
        index = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        components = new ArrayList<>();
        for (int i = 0; i < n; i++) index[i] = -1;
    }

    public static SCCResult run(Graph g, Metrics m) {
        TarjanSCC t = new TarjanSCC(g, m);
        if (m != null) m.start("SCC");
        int n = g.size();
        for (int v = 0; v < n; v++) {
            if (t.index[v] == -1) t.dfs(v);
        }
        if (m != null) m.stop("SCC");
        int[] compOf = new int[n];
        for (int i = 0; i < t.components.size(); i++) {
            for (int v : t.components.get(i)) compOf[v] = i;
        }
        return new SCCResult(t.components, compOf);
    }

    private void dfs(int v) {
        if (metrics != null) metrics.inc("dfsVisits", 1);
        index[v] = currentIndex;
        low[v] = currentIndex;
        currentIndex++;
        stack.push(v);
        onStack[v] = true;

        for (Graph.Edge e : g.neighbors(v)) {
            if (metrics != null) metrics.inc("dfsEdges", 1);
            int w = e.to;
            if (index[w] == -1) {
                dfs(w);
                low[v] = Math.min(low[v], low[w]);
            } else if (onStack[w]) {
                low[v] = Math.min(low[v], index[w]);
            }
        }

        if (low[v] == index[v]) {
            List<Integer> comp = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                comp.add(w);
            } while (w != v);
            components.add(comp);
        }
    }
}
