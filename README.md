I implemented the edge-weight model because your tasks.json contains w on edges and weight_model is "edge". 
If you later need node-duration, change the DAG-SP code to sum durations stored per-node.
For condensation edges, I used the first encountered weight when compressing an inter-component edge. 
You can refine to use min/max etc. depending on semantics.
Tarjan is instrumented (dfsVisits, dfsEdges). Kahn counts pushes/pops, DAG-SP counts relaxAttempts and relaxations.
Timing uses System.nanoTime() in SimpleMetrics.
Path reconstruction is performed on the condensation DAG (i.e., component-level critical paths). If you prefer original-task-level critical path, you can expand compressed components into internal order (but in general SCCs represent cycles and you need to pick an intra-SCC scheduling rule).
