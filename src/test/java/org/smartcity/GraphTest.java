import graph.util.Graph;
import graph.scc.TarjanSCC;
import graph.scc.SCCResult;
import graph.topo.KahnTopologicalSort;
import graph.dagsp.DAGShortestLongest;
import graph.dagsp.PathResult;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void testSmallGraph() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        assertEquals(3, g.size());
    }
    @Test
    void testTarjanSCC() {
        Graph g = new Graph(2);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        SCCResult res = TarjanSCC.run(g, null);

        List<List<Integer>> comps = res.components;

        assertEquals(1, comps.size(), "Expected 1 SCC for 0↔1 cycle");
        assertTrue(comps.get(0).containsAll(List.of(0, 1)));
    }

    @Test
    void testTopologicalSort() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        var order = KahnTopologicalSort.sort(g, null);
        assertEquals(List.of(0, 1, 2), order);
    }

    @Test
    void testDAGShortestPath() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        PathResult res = DAGShortestLongest.shortest(g, 0, null);

        assertEquals(0, res.dist[0]);
        assertEquals(1, res.dist[1]);
        assertEquals(3, res.dist[2]);
    }

    @Test
    void testDAGLongestPath() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        PathResult res = DAGShortestLongest.longest(g, 0, null);

        assertEquals(0, res.dist[0]);
        assertEquals(2, res.dist[1]);
        assertEquals(5, res.dist[2]);
    }
}
