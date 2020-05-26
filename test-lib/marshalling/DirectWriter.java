package test.marshalling;

import eu.menzani.ringbuffer.marshalling.DirectMarshallingRingBuffer;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.DirectOffsets.*;

class DirectWriter extends TestThread {
    static TestThreadGroup startGroupAsync(DirectMarshallingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new DirectWriter(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(DirectMarshallingRingBuffer ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static DirectWriter startAsync(int numIterations, DirectMarshallingRingBuffer ringBuffer) {
        DirectWriter writer = new DirectWriter(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, DirectMarshallingRingBuffer ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private DirectWriter(int numIterations, DirectMarshallingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected void loop() {
        DirectMarshallingRingBuffer ringBuffer = getDirectMarshallingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            long offset = ringBuffer.next();
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
