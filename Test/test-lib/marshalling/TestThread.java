package test.marshalling;

import org.ringbuffer.marshalling.DirectClearingRingBuffer;
import org.ringbuffer.marshalling.DirectRingBuffer;
import org.ringbuffer.marshalling.HeapClearingRingBuffer;
import org.ringbuffer.marshalling.HeapRingBuffer;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, Object ringBuffer) {
        super(numIterations, ringBuffer);
    }

    HeapClearingRingBuffer getHeapClearingRingBuffer() {
        return (HeapClearingRingBuffer) dataStructure;
    }

    HeapRingBuffer getHeapRingBuffer() {
        return (HeapRingBuffer) dataStructure;
    }

    DirectClearingRingBuffer getDirectClearingRingBuffer() {
        return (DirectClearingRingBuffer) dataStructure;
    }

    DirectRingBuffer getDirectRingBuffer() {
        return (DirectRingBuffer) dataStructure;
    }
}
