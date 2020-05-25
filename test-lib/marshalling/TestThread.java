package test.marshalling;

import eu.menzani.ringbuffer.AbstractRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import eu.menzani.ringbuffer.marshalling.MarshallingRingBuffer;
import test.AbstractTestThread;

abstract class TestThread extends AbstractTestThread {
    TestThread(int numIterations, AbstractRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    MarshallingRingBuffer getMarshallingRingBuffer() {
        return (MarshallingRingBuffer) ringBuffer;
    }

    MarshallingBlockingRingBuffer getMarshallingBlockingRingBuffer() {
        return (MarshallingBlockingRingBuffer) ringBuffer;
    }

    DirectMarshallingRingBuffer getDirectMarshallingRingBuffer() {
        return (DirectMarshallingRingBuffer) ringBuffer;
    }

    DirectMarshallingBlockingRingBuffer getDirectMarshallingBlockingRingBuffer() {
        return (DirectMarshallingBlockingRingBuffer) ringBuffer;
    }
}
