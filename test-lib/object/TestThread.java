package test.object;

import org.ringbuffer.object.EmptyRingBuffer;
import org.ringbuffer.object.PrefilledOverwritingRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.RingBuffer;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, RingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @SuppressWarnings("unchecked")
    RingBuffer<Event> getRingBuffer() {
        return (RingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    EmptyRingBuffer<Event> getEmptyRingBuffer() {
        return (EmptyRingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    PrefilledOverwritingRingBuffer<Event> getPrefilledOverwritingRingBuffer() {
        return (PrefilledOverwritingRingBuffer<Event>) ringBuffer;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) ringBuffer;
    }
}
