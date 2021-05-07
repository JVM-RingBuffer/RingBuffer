package test.marshalling;

import org.ringbuffer.marshalling.*;
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

    LockfreeHeapRingBuffer getLockfreeHeapRingBuffer() {
        return (LockfreeHeapRingBuffer) dataStructure;
    }

    DirectClearingRingBuffer getDirectClearingRingBuffer() {
        return (DirectClearingRingBuffer) dataStructure;
    }

    DirectRingBuffer getDirectRingBuffer() {
        return (DirectRingBuffer) dataStructure;
    }

    LockfreeDirectRingBuffer getLockfreeDirectRingBuffer() {
        return (LockfreeDirectRingBuffer) dataStructure;
    }
}
