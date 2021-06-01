package bench.competitors;

import bench.AbstractReader;
import bench.BenchmarkThreadGroup;
import bench.object.Event;
import eu.menzani.benchmark.Profiler;

import java.util.Queue;

class Reader extends BenchmarkThread implements AbstractReader {
    static long runGroupAsync(Queue<Event> queue, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new Reader(numIterations, queue));
        group.start(null);
        group.waitForCompletion(profiler);
        return group.getReaderSum();
    }

    static long runAsync(int numIterations, Queue<Event> queue, Profiler profiler) {
        Reader reader = new Reader(numIterations, queue);
        reader.startNow(null);
        reader.waitForCompletion(profiler);
        return reader.getSum();
    }

    private long sum;

    Reader(int numIterations, Queue<Event> queue) {
        super(numIterations, queue);
    }

    @Override
    public long getSum() {
        return sum;
    }

    @Override
    protected void loop() {
        sum = collect();
    }

    long collect() {
        Queue<Event> queue = getQueue();
        Event element;
        long sum = 0L;
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            while ((element = queue.poll()) == null) {
                Thread.onSpinWait();
            }
            sum += element.getData();
        }
        return sum;
    }
}
