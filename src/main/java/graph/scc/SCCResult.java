package graph.scc;

import java.util.ArrayList;
import java.util.List;

public class SCCResult {
    public final List<List<Integer>> components;
    public final int[] compOf;

    public SCCResult(List<List<Integer>> components, int[] compOf) {
        this.components = components;
        this.compOf = compOf;
    }

    public int numComponents() { return components.size(); }
}
