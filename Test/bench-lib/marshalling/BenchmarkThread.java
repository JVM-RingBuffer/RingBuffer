package bench.marshalling;

import bench.AbstractBenchmarkThread;
import org.ringbuffer.marshalling.*;

abstract class BenchmarkThread extends AbstractBenchmarkThread {
    BenchmarkThread(int numIterations, Object ringBuffer) {
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
