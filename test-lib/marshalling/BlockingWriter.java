package test.marshalling;

import eu.menzani.ringbuffer.marshalling.MarshallingBlockingRingBuffer;
import test.TestThreadGroup;

import static eu.menzani.ringbuffer.marshalling.Offsets.*;

class BlockingWriter extends TestThread {
    static TestThreadGroup startGroupAsync(MarshallingBlockingRingBuffer ringBuffer) {
        TestThreadGroup group = new TestThreadGroup(numIterations -> new BlockingWriter(numIterations, ringBuffer));
        group.start();
        return group;
    }

    static void runGroupAsync(MarshallingBlockingRingBuffer ringBuffer) {
        startGroupAsync(ringBuffer).waitForCompletion();
    }

    static BlockingWriter startAsync(int numIterations, MarshallingBlockingRingBuffer ringBuffer) {
        BlockingWriter writer = new BlockingWriter(numIterations, ringBuffer);
        writer.startNow();
        return writer;
    }

    static void runAsync(int numIterations, MarshallingBlockingRingBuffer ringBuffer) {
        startAsync(numIterations, ringBuffer).waitForCompletion();
    }

    private BlockingWriter(int numIterations, MarshallingBlockingRingBuffer ringBuffer) {
        super(numIterations, ringBuffer);
    }

    @Override
    protected String getProfilerName() {
        return "BlockingWriter";
    }

    @Override
    protected void loop() {
        MarshallingBlockingRingBuffer ringBuffer = getMarshallingBlockingRingBuffer();
        for (int numIterations = getNumIterations(); numIterations > 0; numIterations--) {
            int offset = ringBuffer.next(INT);
            ringBuffer.writeInt(offset, numIterations);
            ringBuffer.put(offset + INT);
        }
    }
}
