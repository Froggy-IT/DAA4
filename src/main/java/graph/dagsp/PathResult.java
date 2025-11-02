package graph.dagsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathResult {
    public final long[] dist;
    public final int[] parent;

    public PathResult(long[] dist, int[] parent) {
        this.dist = dist;
        this.parent = parent;
    }

    public List<Integer> reconstruct(int source, int target) {
        if (dist[target] == Long.MAX_VALUE) return Collections.emptyList();
        List<Integer> path = new ArrayList<>();
        int cur = target;
        while (cur != -1 && cur != source) {
            path.add(cur);
            cur = parent[cur];
        }
        if (cur == -1) return Collections.emptyList();
        path.add(source);
        Collections.reverse(path);
        return path;
    }
}
