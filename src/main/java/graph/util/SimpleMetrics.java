package graph.util;

import java.util.HashMap;
import java.util.Map;

public class SimpleMetrics implements Metrics {
    private final Map<String, Long> counters = new HashMap<>();
    private final Map<String, Long> startTimes = new HashMap<>();
    private final Map<String, Long> elapsed = new HashMap<>();

    @Override
    public void start(String op) {
        startTimes.put(op, System.nanoTime());
    }

    @Override
    public void stop(String op) {
        Long s = startTimes.remove(op);
        if (s != null) {
            long delta = System.nanoTime() - s;
            elapsed.put(op, elapsed.getOrDefault(op, 0L) + delta);
        }
    }

    @Override
    public void inc(String counter, long delta) {
        counters.put(counter, counters.getOrDefault(counter, 0L) + delta);
    }

    @Override
    public long getCounter(String counter) {
        return counters.getOrDefault(counter, 0L);
    }

    @Override
    public long getElapsedNanos(String op) {
        return elapsed.getOrDefault(op, 0L);
    }
}
