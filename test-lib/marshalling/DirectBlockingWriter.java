package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingBlockingRingBuffer;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.DirectOffsets.*;

class DirectBlockingWriter extends TestThread {
    static TestThreadGroup startGroupAsync(DirectMarshallingBlockingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectBlockingWriter(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(DirectMarshallingBlockingRingBuffer ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static DirectBlockingWriter startAsync(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer) {
        DirectBlockingWriter writer = new DirectBlockingWriter(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private DirectBlockingWriter(int numIterations, DirectMarshallingBlockingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        DirectMarshallingBlockingRingBuffer ringBuffer = getDirectMarshallingBlockingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next(INT);
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
