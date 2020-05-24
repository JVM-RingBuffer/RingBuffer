package test;

public class Profiler {
    private final Benchmark.Result result;
    private final double divideBy;
    private long start;

    public Profiler(String name, int divideBy) {
        this.divideBy = divideBy;
        result = Benchmark.current().getResult(name);
    }

    public void start() {
        start = System.nanoTime();
    }

    public void stop() {
        final long end = System.nanoTime();
        result.update(Math.round((end - start) / divideBy));
    }
}
