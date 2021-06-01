package bench.object;

import bench.BenchmarkThreadGroup;
import eu.menzani.benchmark.Profiler;
import org.ringbuffer.object.ObjectRingBuffer;
import org.ringbuffer.object.RingBuffer;

class Writer extends BenchmarkThread {
    static BenchmarkThreadGroup startGroupAsync(ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        BenchmarkThreadGroup group = new BenchmarkThreadGroup(numIterations -> new Writer(numIterations, ringBuffer));
        group.start(profiler);
        return group;
    }

    static void runGroupAsync(ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        startGroupAsync(ringBuffer, profiler).waitForCompletion(null);
    }

    static Writer startAsync(int numIterations, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        Writer writer = new Writer(numIterations, ringBuffer);
        writer.startNow(profiler);
        return writer;
    }

    static void runAsync(int numIterations, ObjectRingBuffer<Event> ringBuffer, Profiler profiler) {
        startAsync(numIterations, ringBuffer, profiler).waitForCompletion(null);
    }

    private Writer(int numIterations, ObjectRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        RingBuffer<Event> ringBuffer = getRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            ringBuffer.put(new Event(numIterations));
        }
    }
}
