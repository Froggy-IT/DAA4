import graph.util.Graph;
import org.junit.jupiter.api.Test;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void testSmallGraph() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        assertEquals(3, g.size());
    }

    @Test
    void testTarjanSCC() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        TarjanSCC t = new TarjanSCC();
        var sccs = t.findSCCs(g);
        assertEquals(1, sccs.size());
    }

    @Test
    void testTopologicalSort() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        KahnSort k = new KahnSort();
        var order = k.topologicalSort(g);
        assertEquals(List.of(0, 1, 2), order);
    }

    @Test
    void testDAGShortestPath() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        DAGShortestPath sp = new DAGShortestPath();
        var dist = sp.shortestPath(g, 0);
        assertEquals(0, dist.get(0));
        assertEquals(1, dist.get(1));
        assertEquals(3, dist.get(2));
    }
}
