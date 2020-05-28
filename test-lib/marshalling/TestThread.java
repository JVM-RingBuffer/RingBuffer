package test.marshalling;

import org.ringbuffer.AbstractRingBuffer;
import org.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import org.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import org.ringbuffer.marshalling.MarshallingRingBuffer;
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
