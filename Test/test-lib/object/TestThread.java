package test.object;

import org.ringbuffer.object.ObjectRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer;
import org.ringbuffer.object.PrefilledRingBuffer2;
import org.ringbuffer.object.RingBuffer;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, ObjectRingBuffer<Event> ringBuffer) {
        super(numIterations, ringBuffer);
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
    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer2<Event> getPrefilledRingBuffer2() {
        return (PrefilledRingBuffer2<Event>) dataStructure;
    }
}
