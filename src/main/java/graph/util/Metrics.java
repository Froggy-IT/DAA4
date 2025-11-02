package graph.util;

public interface Metrics {
    void start(String op);
    void stop(String op);
    void inc(String counter, long delta);
    long getCounter(String counter);
    long getElapsedNanos(String op);
}
