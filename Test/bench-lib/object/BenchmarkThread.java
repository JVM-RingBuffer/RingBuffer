package bench.object;

import bench.AbstractBenchmarkThread;
import org.ringbuffer.object.*;

abstract class BenchmarkThread extends AbstractBenchmarkThread {
    BenchmarkThread(int numIterations, Object dataStructure) {
        super(numIterations, dataStructure);
    }

    @SuppressWarnings("unchecked")
    ObjectRingBuffer<Event> getObjectRingBuffer() {
        return (ObjectRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    RingBuffer<Event> getRingBuffer() {
        return (RingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    LockfreeRingBuffer<Event> getLockfreeRingBuffer() {
        return (LockfreeRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer2<Event> getPrefilledRingBuffer2() {
        return (PrefilledRingBuffer2<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    LockfreePrefilledRingBuffer<Event> getLockfreePrefilledRingBuffer() {
        return (LockfreePrefilledRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    Stack<Event> getStack() {
        return (Stack<Event>) dataStructure;
    }
}
