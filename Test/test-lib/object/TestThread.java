package test.object;

import org.ringbuffer.object.*;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, Object dataStructure) {
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
    PrefilledRingBuffer<Event> getPrefilledRingBuffer() {
        return (PrefilledRingBuffer<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    PrefilledRingBuffer2<Event> getPrefilledRingBuffer2() {
        return (PrefilledRingBuffer2<Event>) dataStructure;
    }

    @SuppressWarnings("unchecked")
    Stack<Event> getStack() {
        return (Stack<Event>) dataStructure;
    }
}
