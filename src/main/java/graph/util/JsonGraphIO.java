package graph.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonGraphIO {
    public static class Loaded {
        public final Graph g;
        public final Integer source;
        public final String weightModel;
        public Loaded(Graph g, Integer source, String weightModel) {
            this.g = g; this.source = source; this.weightModel = weightModel;
        }
    }

    public static Loaded load(File f) throws Exception {
        ObjectMapper m = new ObjectMapper();
        JsonNode root = m.readTree(f);
        int n = root.get("n").asInt();
        Graph g = new Graph(n);
        JsonNode edges = root.get("edges");
        if (edges != null && edges.isArray()) {
            for (JsonNode e : edges) {
                int u = e.get("u").asInt();
                int v = e.get("v").asInt();
                long w = e.has("w") ? e.get("w").asLong() : 1L;
                g.addEdge(u, v, w);
            }
        }
        Integer source = root.has("source") ? root.get("source").asInt() : null;
        String weightModel = root.has("weight_model") ? root.get("weight_model").asText() : "edge";
        return new Loaded(g, source, weightModel);
    }
}
