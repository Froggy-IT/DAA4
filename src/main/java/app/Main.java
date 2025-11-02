package app;

import graph.dagsp.DAGShortestLongest;
import graph.dagsp.PathResult;
import graph.scc.SCCResult;
import graph.scc.TarjanSCC;
import graph.topo.CondensationGraph;
import graph.topo.KahnTopologicalSort;
import graph.util.Graph;
import graph.util.JsonGraphIO;
import graph.util.Metrics;
import graph.util.SimpleMetrics;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        File f = new File("data/tasks.json");
        if (args.length > 0) f = new File(args[0]);
        JsonGraphIO.Loaded loaded = JsonGraphIO.load(f);
        Graph g = loaded.g;
        Integer source = loaded.source;
        String weightModel = loaded.weightModel;
        System.out.println("Loaded graph (n=" + g.size() + "), weightModel=" + weightModel + ", source=" + source);
        System.out.println(g);

        Metrics m = new SimpleMetrics();

        // 1. SCC
        SCCResult scc = TarjanSCC.run(g, m);
        System.out.println("SCCs found: " + scc.numComponents());
        for (int i = 0; i < scc.components.size(); i++) {
            List<Integer> comp = scc.components.get(i);
            System.out.println(" Component " + i + ": " + comp + " size=" + comp.size());
        }
        System.out.println("SCC metrics: dfsVisits=" + m.getCounter("dfsVisits") + ", dfsEdges=" + m.getCounter("dfsEdges")
                + ", timeNs=" + m.getElapsedNanos("SCC"));

        // 2. Condensation DAG
        Graph dag = CondensationGraph.build(scc, g);
        System.out.println("\nCondensation DAG (nodes=components):");
        System.out.println(dag);

        // 3. Topological order (components)
        List<Integer> topo = KahnTopologicalSort.sort(dag, m);
        System.out.println("Topological order of components: " + topo);
        System.out.println("Kahn metrics: pushes=" + m.getCounter("pushes") + ", pops=" + m.getCounter("pops"));

        // 4. DAG shortest & longest paths on condensation DAG, using source's component if source exists
        if (source != null) {
            int srcComp = scc.compOf[source];
            System.out.println("\nUsing source node " + source + " (component " + srcComp + ") on condensation DAG.");

            PathResult sp = DAGShortestLongest.shortest(dag, srcComp, m);
            System.out.println("Shortest distances from component " + srcComp + ":");
            for (int i = 0; i < dag.size(); i++) {
                long d = sp.dist[i];
                System.out.printf(" comp %d : %s\n", i, (d == Long.MAX_VALUE ? "INF" : d));
            }

            // Example: reconstruct path to each comp
            for (int t = 0; t < dag.size(); t++) {
                if (sp.dist[t] != Long.MAX_VALUE) {
                    System.out.println("  Path to comp " + t + " : " + sp.reconstruct(srcComp, t));
                }
            }
            System.out.println("DAG-SP shortest metrics: relaxAttempts=" + m.getCounter("relaxAttempts") + ", relaxations=" + m.getCounter("relaxations")
                    + ", timeNs=" + m.getElapsedNanos("DAG-SP-shortest"));

            // Longest (critical) path
            PathResult lp = DAGShortestLongest.longest(dag, srcComp, m);
            System.out.println("\nLongest distances (critical path lengths) from component " + srcComp + ":");
            for (int i = 0; i < dag.size(); i++) {
                long d = lp.dist[i];
                System.out.printf(" comp %d : %s\n", i, (d == Long.MAX_VALUE ? "UNREACHABLE" : d));
            }
            // find global max reachable
            long best = Long.MIN_VALUE;
            int bestIdx = -1;
            for (int i = 0; i < dag.size(); i++) {
                long val = lp.dist[i];
                if (val != Long.MAX_VALUE && val > best) { best = val; bestIdx = i; }
            }
            if (bestIdx != -1) {
                System.out.println("Critical path (component-level) ends at comp " + bestIdx + " length=" + best);
                System.out.println("Reconstructed critical path comps: " + lp.reconstruct(srcComp, bestIdx));
            } else {
                System.out.println("No reachable nodes from source for longest path.");
            }
            System.out.println("DAG-SP longest metrics: relaxAttempts=" + m.getCounter("relaxAttempts") + ", relaxations=" + m.getCounter("relaxations")
                    + ", timeNs=" + m.getElapsedNanos("DAG-SP-longest"));
        } else {
            System.out.println("No source provided in JSON; skipping DAG-SP.");
        }
    }
}
